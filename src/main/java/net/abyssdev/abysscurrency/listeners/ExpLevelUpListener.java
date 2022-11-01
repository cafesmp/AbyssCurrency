package net.abyssdev.abysscurrency.listeners;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.currency.Currency;
import net.abyssdev.abysscurrency.currency.drop.CurrencyDrop;
import net.abyssdev.abysscurrency.player.CurrencyPlayer;
import net.abyssdev.abysscurrency.utils.RandomUtils;
import net.abyssdev.abysscurrency.utils.format.FormatUtil;
import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.eclipse.collections.api.factory.Lists;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The entity death listener
 * @author Relocation
 */
public final class ExpLevelUpListener extends AbyssListener<AbyssCurrency> {

    private final List<CurrencyDrop> drops = Lists.mutable.empty();

    /**
     * Constructs a new EntityDeathListener
     * @param plugin The abyss currency plugin
     */
    public ExpLevelUpListener(final AbyssCurrency plugin) {
        super(plugin);

        for (final Currency currency : plugin.getCurrencyRegistry().values()) {
            if (!currency.getConfig().getBoolean("on-exp-level-up.enabled")) {
                continue;
            }

            this.drops.add(new CurrencyDrop(currency,
                    currency.getConfig().getDouble("on-exp-level-up.min"),
                    currency.getConfig().getDouble("on-exp-level-up.max"),
                    currency.getConfig().getDouble("on-exp-level-up.chance")));
        }
    }

    @EventHandler
    public void onEXPGain(final PlayerLevelChangeEvent event) {

        if (event.getNewLevel() < event.getOldLevel()) {
            return;
        }

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
        }

    }

}