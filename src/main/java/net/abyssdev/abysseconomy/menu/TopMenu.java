package net.abyssdev.abysseconomy.menu;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * The currency top menu for each currency
 * @author Relocation
 */
public final class TopMenu extends AbyssMenu {

    private final AbyssEconomy plugin;
    private final Currency currency;

    private final ItemStack invalid;
    private final ItemBuilder valid;

    //Too lazy to make a wrapper I'll change this later
    private final Pair<ItemBuilder, Integer> stats;

    private final LinkedList<CurrencyPlayer> sortedPlayers = new LinkedList<>();
    private final IntArrayList topSlots = new IntArrayList();

    /**
     * Constructs a new TopMenu
     * @param plugin The abyss economy plugin
     * @param currency The currency
     */
    public TopMenu(final AbyssEconomy plugin, final Currency currency) {
        super(currency.getConfig(), "menus.top-menu.");

        this.plugin = plugin;
        this.currency = currency;

        for (final int slot : currency.getConfig().getIntegerList("menus.top-menu.top-slots")) {
            this.topSlots.add(slot);
        }

        this.invalid = new ItemBuilder(currency.getConfig(), "menus.top-menu.invalid-item").parse();
        this.valid = new ItemBuilder(currency.getConfig(), "menus.top-menu.valid-item");
        this.stats = new Pair<>(
                new ItemBuilder(currency.getConfig(), "menus.top-menu.stats-item"),
                currency.getConfig().getInt("menus.top-menu.stats-item.slot"));

        this.recalculate();
    }

    @Override
    public void open(final Player player) {
        final MenuBuilder builder = this.createBase(player);

        int index = 0;

        for (final int slot : this.topSlots.toArray()) {

            if (index >= this.sortedPlayers.size()) {
                builder.setItem(slot, this.invalid);
                continue;
            }

            final CurrencyPlayer profile = this.sortedPlayers.get(index);
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(profile.getUuid());

            final ItemStack item = this.valid.parse(new PlaceholderReplacer()
                    .addPlaceholder("%rank%", String.valueOf(index + 1))
                    .addPlaceholder("%player%", offlinePlayer.getName())
                    .addPlaceholder("%amount%", FormatUtil.format(this.currency, profile.getBalance(this.currency))));

            if (this.isPlayerSkull(this.valid)) {
                final SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setOwner(offlinePlayer.getName());
            }

            builder.setItem(slot, item);
            index++;

        }

        final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());
        final ItemStack item = this.stats.getLeft().parse(new PlaceholderReplacer()
                .addPlaceholder("%rank%", String.valueOf(this.sortedPlayers.indexOf(profile) + 1))
                .addPlaceholder("%player%", player.getName())
                .addPlaceholder("%amount%", FormatUtil.format(this.currency, profile.getBalance(this.currency))));

        if (this.isPlayerSkull(this.stats.getLeft())) {
            final SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwner(player.getName());
        }

        builder.setItem(this.stats.getRight(), item);

        player.openInventory(builder.build());
    }

    public void recalculate() {
        this.currency.getMessageCache().getMessage("messages.top-updating").broadcast();

        final double current = System.currentTimeMillis();

        this.sortedPlayers.clear();
        this.sortedPlayers.addAll(this.plugin.getPlayerStorage().allValues());
        this.sortedPlayers.sort(Comparator.comparingDouble(profile -> -profile.getBalance(this.currency)));

        final double after = System.currentTimeMillis();

        this.currency.getMessageCache().getMessage("messages.top-updated").broadcast(new PlaceholderReplacer()
                .addPlaceholder("%time%", String.valueOf(after - current)));
    }

    private boolean isPlayerSkull(final ItemBuilder builder) {
        return (builder.getMaterial().contains("SKULL_ITEM") || builder.getMaterial().contains("PLAYER_HEAD"))
                && builder.getSkull() != null
                && builder.getSkull().equalsIgnoreCase("%player%");
    }
}
