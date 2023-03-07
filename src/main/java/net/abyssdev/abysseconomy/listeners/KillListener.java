package net.abyssdev.abysseconomy.listeners;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.api.events.CurrencyGainEvent;
import net.abyssdev.abysseconomy.api.reason.CurrencyGainReason;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.drop.CurrencyDrop;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.utils.RandomUtils;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.eclipse.collections.api.factory.Lists;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The entity death listener
 * @author Relocation
 */
public final class KillListener extends AbyssListener<AbyssEconomy> {

    private final List<CurrencyDrop> drops = Lists.mutable.empty();

    /**
     * Constructs a new EntityDeathListener
     * @param plugin The abyss economy plugin
     */
    public KillListener(final AbyssEconomy plugin) {
        super(plugin);

        for (final Currency currency : plugin.getCurrencyRegistry().values()) {
            if (!currency.getConfig().getBoolean("player-kill.enabled")) {
                continue;
            }

            this.drops.add(new CurrencyDrop(currency,
                    currency.getConfig().getDouble("player-kill.min"),
                    currency.getConfig().getDouble("player-kill.max"),
                    currency.getConfig().getDouble("player-kill.chance")));
        }
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {

        final Player player = event.getEntity();
        final Player attacker = player.getKiller();

        if (attacker == null) {
            return;
        }

        final CurrencyPlayer profile = this.getPlugin().getPlayerStorage().get(attacker.getUniqueId());

        for (final CurrencyDrop drop : this.drops) {
            if (drop.getChance() < ThreadLocalRandom.current().nextDouble(100)) {
                continue;
            }

            double amount = RandomUtils.minMax(drop.getMin(), drop.getMax());

            if (drop.getCurrency().getEvent().isActive()) {
                amount *= drop.getCurrency().getEvent().getMultiplier();
            }

            profile.addCurrency(drop.getCurrency(), amount, CurrencyGainReason.NATURAL);

            if (profile.getCurrencies().get(drop.getCurrency().getName()).isMessagesToggled()) {
                drop.getCurrency().getMessageCache().sendMessage(player, "player-kill.message", new PlaceholderReplacer()
                        .addPlaceholder("%amount%", FormatUtil.format(drop.getCurrency(), amount))
                        .addPlaceholder("%player%", player.getName()));
            }

            new CurrencyGainEvent(player, drop.getCurrency(), profile, CurrencyGainReason.NATURAL, amount).fire(this.getPlugin());
        }

    }
}