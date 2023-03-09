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
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.eclipse.collections.api.factory.Lists;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The entity death listener
 * @author Relocation
 */
public final class EntityDeathListener extends AbyssListener<AbyssEconomy> {

    private final Map<EntityType, List<CurrencyDrop>> drops = new EnumMap<>(EntityType.class);

    /**
     * Constructs a new EntityDeathListener
     * @param plugin The abyss economy plugin
     */
    public EntityDeathListener(final AbyssEconomy plugin) {
        super(plugin);

        for (final Currency currency : plugin.getCurrencyRegistry().values()) {
            if (!currency.getConfig().getBoolean("mobs.enabled")) {
                continue;
            }

            for (final String key : currency.getConfig().getStringList("mobs.rewards")) {
                final String[] data = key.split(";");

                final EntityType type = EntityType.valueOf(data[0]);

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
    public void onDeath(final EntityDeathEvent event) {

        final LivingEntity entity = event.getEntity();

        if (!this.drops.containsKey(entity.getType())) {
            return;
        }

        final Player player = entity.getKiller();

        if (player == null) {
            return;
        }

        final CurrencyPlayer profile = this.getPlugin().getPlayerStorage().get(player.getUniqueId());

        for (final CurrencyDrop drop : this.drops.get(entity.getType())) {
            if (drop.getChance() < ThreadLocalRandom.current().nextDouble(100)) {
                continue;
            }

            double amount = RandomUtils.minMax(drop.getMin(), drop.getMax());

            if (drop.getCurrency().getEvent().isActive()) {
                amount *= drop.getCurrency().getEvent().getMultiplier();
            }

            profile.addCurrency(drop.getCurrency(), amount, CurrencyGainReason.NATURAL);

            if (profile.getCurrencies().get(drop.getCurrency().getName()).isMessagesToggled()) {
                drop.getCurrency().getMessageCache().sendMessage(player, "mobs.message", new PlaceholderReplacer()
                        .addPlaceholder("%amount%", FormatUtil.format(drop.getCurrency(), amount))
                        .addPlaceholder("%mob", WordUtils.formatText(entity.getType().name().replace("_", " "))));
            }

            new CurrencyGainEvent(player, drop.getCurrency().getImplementation(), profile, CurrencyGainReason.NATURAL, amount).fire(this.getPlugin());
        }

    }
}