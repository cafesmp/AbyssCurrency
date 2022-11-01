package net.abyssdev.abysscurrency.listeners;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.currency.Currency;
import net.abyssdev.abysscurrency.currency.drop.CurrencyDrop;
import net.abyssdev.abysscurrency.player.CurrencyPlayer;
import net.abyssdev.abysscurrency.utils.RandomUtils;
import net.abyssdev.abysscurrency.utils.format.FormatUtil;
import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.eclipse.collections.api.factory.Lists;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The entity death listener
 * @author Relocation
 */
public final class KillListener extends AbyssListener<AbyssCurrency> {

    private final List<CurrencyDrop> drops = Lists.mutable.empty();

    /**
     * Constructs a new EntityDeathListener
     * @param plugin The abyss currency plugin
     */
    public KillListener(final AbyssCurrency plugin) {
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

            final double amount = RandomUtils.minMax(drop.getMin(), drop.getMax());
            profile.addCurrency(drop.getCurrency(), amount);

            drop.getCurrency().getMessageCache().sendMessage(player, "player-kill.message", new PlaceholderReplacer()
                    .addPlaceholder("%amount%", FormatUtil.format(drop.getCurrency(), amount))
                    .addPlaceholder("%player%", player.getName()));
        }

    }

}