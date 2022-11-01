package net.abyssdev.abysseconomy.shop.impl;

import lombok.SneakyThrows;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.shop.Shop;
import net.abyssdev.abysseconomy.shop.item.ShopItem;
import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.abysslib.utils.tuple.ImmutablePair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The rotating shop
 * @author Relocation
 */
public final class RotatingShop extends Shop {

    // Too lazy to make a wrapper for pairs rn I'll change this later
    private final ShopItem[] shopItems;
    private final Map<String, ImmutablePair<ShopItem, Integer>> currentItems = Maps.mutable.empty();
    private final IntArrayList itemSlots = new IntArrayList();
    private final ImmutablePair<ItemBuilder, Integer> rotatingItem;

    private final int interval;
    private int time;

    /**
     * Constructs a new RotatingShop
     * @param plugin The abyss economy plugin
     * @param currency The currency
     * @param path The path
     * @param name The name
     */
    public RotatingShop(final AbyssEconomy plugin, final Currency currency, final String path, final String name) {
        super(plugin, currency, path, name);

        for (final int slot : currency.getConfig().getIntegerList(path + "item-slots")) {
            this.itemSlots.add(slot);
        }

        this.interval = currency.getConfig().getInt(path + "rotating-time");
        this.time = this.interval;

        this.rotatingItem = new ImmutablePair<>(
                new ItemBuilder(currency.getConfig(), path + "rotating-item"),
                currency.getConfig().getInt(path + "rotating-item.slot"));

        final ConfigurationSection section = currency.getConfig().getConfigurationSection(path + "items");
        final Set<String> keys = section.getKeys(false);

        this.shopItems = new ShopItem[keys.size()];

        int index = 0;

        for (final String key : keys) {
            this.shopItems[index] = new ShopItem(key, path + "items." + key, currency.getConfig());
            index++;
        }

        this.rotateItems();
    }

    @Override
    public void open(final Player player) {

        final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());
        final MenuBuilder builder = this.createBase();

        builder.setItem(this.rotatingItem.getValue(), this.rotatingItem.getKey().parse(new PlaceholderReplacer().addPlaceholder("%time%",
                Utils.getTimeFormat(this.time * 1000L))));

        for (final ImmutablePair<ShopItem, Integer> pair : this.currentItems.values()) {

            final ShopItem shopItem = pair.getKey();
            final int slot = pair.getValue();

            builder.setItem(slot, shopItem.getItem());
            builder.addClickEvent(slot, event -> {
                if (!profile.hasBalance(this.currency, shopItem.getPrice())) {
                    this.currency.getMessageCache().sendMessage(player, "messages.not-enough");
                    return;
                }

                for (final String command : pair.getKey().getCommands()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                            .replace("%player%", player.getName()));
                }

                profile.removeCurrency(this.currency, pair.getKey().getPrice());

                this.currency.getMessageCache().sendMessage(player, "messages.purchased", new PlaceholderReplacer()
                        .addPlaceholder("%cost%", Utils.format(pair.getKey().getPrice()))
                        .addPlaceholder("%item%", pair.getKey().getName()));
            });
        }

        player.openInventory(builder.build(player, this.plugin.getPlaceholders()));
    }

    public ShopItem getValidShopItem() {
        final ShopItem shopItem = this.shopItems[ThreadLocalRandom.current().nextInt(this.shopItems.length)];

        if (this.shopItems.length <= this.itemSlots.size() && this.shopItems.length == this.currentItems.size()) {
            return null;
        }

        if (this.currentItems.containsKey(shopItem.getName())) {
            return getValidShopItem();
        }

        return shopItem;
    }

    public void rotateItems() {
        this.currentItems.clear();

        for (final int i : this.itemSlots.toArray()) {
            final ShopItem shopItem = getValidShopItem();

            if (shopItem == null) {
                break;
            }

            this.currentItems.put(shopItem.getName(), new ImmutablePair<>(shopItem, i));
        }
    }

    public int getInterval() {
        return this.interval;
    }

    public int getTimeUntilRotation() {
        return this.time;
    }

    public void setTimeUntilRotation(final int time) {
        this.time = time;
    }

}