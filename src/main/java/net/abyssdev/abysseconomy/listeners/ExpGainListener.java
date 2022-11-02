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
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.eclipse.collections.api.factory.Lists;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The entity death listener
 * @author Relocation
 */
public final class ExpGainListener extends AbyssListener<AbyssEconomy> {

    private final List<CurrencyDrop> drops = Lists.mutable.empty();

    /**
     * Constructs a new EntityDeathListener
     * @param plugin The abyss economy plugin
     */
    public ExpGainListener(final AbyssEconomy plugin) {
        super(plugin);

        for (final Currency currency : plugin.getCurrencyRegistry().values()) {
            if (!currency.getConfig().getBoolean("on-exp-gain.enabled")) {
                continue;
            }

            this.drops.add(new CurrencyDrop(currency,
                    currency.getConfig().getDouble("on-exp-gain.min"),
                    currency.getConfig().getDouble("on-exp-gain.max"),
                    currency.getConfig().getDouble("on-exp-gain.chance")));
        }
    }

    @EventHandler
    public void onEXPGain(final PlayerExpChangeEvent event) {

        final Player player = event.getPlayer();
        final CurrencyPlayer profile = this.getPlugin().getPlayerStorage().get(player.getUniqueId());

        for (final CurrencyDrop drop : this.drops) {
            if (drop.getChance() < ThreadLocalRandom.current().nextDouble(100)) {
                continue;
            }

            final double amount = RandomUtils.minMax(drop.getMin(), drop.getMax());
            profile.addCurrency(drop.getCurrency(), amount);

            drop.getCurrency().getMessageCache().sendMessage(player, "on-exp-gain.message", new PlaceholderReplacer()
                    .addPlaceholder("%amount%", FormatUtil.format(drop.getCurrency(), amount)));

            new CurrencyGainEvent(player, profile, CurrencyGainReason.NATURAL, amount).fire(this.getPlugin());
        }

    }

}