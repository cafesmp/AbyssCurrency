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
import net.abyssdev.abysslib.team.utils.TeamUtils;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.eclipse.collections.api.factory.Lists;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The block break listener
 * @author Relocation
 */
public final class BreakListener extends AbyssListener<AbyssEconomy> {

    private final Map<Material, List<CurrencyDrop>> drops = new EnumMap<>(Material.class);

    /**
     * Constructs a new BreakListener
     * @param plugin The abyss economy plugin
     */
    public BreakListener(final AbyssEconomy plugin) {
        super(plugin);

        for (final Currency currency : plugin.getCurrencyRegistry().values()) {
            if (!currency.getConfig().getBoolean("blocks.enabled")) {
                continue;
            }

            for (final String key : currency.getConfig().getStringList("blocks.rewards")) {
                final String[] data = key.split(";");

                final Material type = Material.valueOf(data[0]);

                final double min = Double.parseDouble(data[1]);
                final double max = Double.parseDouble(data[2]);
                final double chance = Double.parseDouble(data[3]);

                final List<CurrencyDrop> drops = this.drops.getOrDefault(type, Lists.mutable.empty());

                drops.add(new CurrencyDrop(currency, min, max, chance));

                this.drops.put(type, drops);
            }
        }
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {

        final Material type = event.getBlock().getType();

        if (!this.drops.containsKey(type)) {
            return;
        }

        final Player player = event.getPlayer();

        if (!TeamUtils.get().canBreak(player, event.getBlock().getLocation())) {
            return;
        }

        final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());

        for (final CurrencyDrop drop : this.drops.get(type)) {
            if (drop.getChance() < ThreadLocalRandom.current().nextDouble(100)) {
                continue;
            }

            double amount = RandomUtils.minMax(drop.getMin(), drop.getMax());

            if (drop.getCurrency().getEvent().isActive()) {
                amount *= drop.getCurrency().getEvent().getMultiplier();
            }

            profile.addCurrency(drop.getCurrency(), amount, CurrencyGainReason.NATURAL);

            if (profile.getCurrencies().get(drop.getCurrency().getName()).isMessagesToggled()) {
                drop.getCurrency().getMessageCache().sendMessage(player, "blocks.message", new PlaceholderReplacer()
                        .addPlaceholder("%amount%", FormatUtil.format(drop.getCurrency(), amount))
                        .addPlaceholder("%block%", WordUtils.formatText(type.name().replace("_", " "))));
            }

            new CurrencyGainEvent(player, drop.getCurrency(), profile, CurrencyGainReason.NATURAL, amount).fire(this.getPlugin());
        }

    }
}