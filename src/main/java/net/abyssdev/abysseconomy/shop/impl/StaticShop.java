package net.abyssdev.abysseconomy.shop.impl;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.shop.Shop;
import net.abyssdev.abysseconomy.shop.item.ShopItem;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * The static shop
 * @author Relocation
 */
public final class StaticShop extends Shop {

    private final ShopItem[] items;

    /**
     * Constructs a new StaticShop
     * @param plugin The abyss economy plugin
     * @param currency The currency
     * @param path The path
     * @param name The name
     */
    public StaticShop(final AbyssEconomy plugin, final Currency currency, final String path, final String name) {
        super(plugin, currency, path, name);

        final ConfigurationSection section = currency.getConfig().getConfigurationSection(path + "items");
        final Set<String> keys = section.getKeys(false);

        this.items = new ShopItem[keys.size()];

        int index = 0;

        for (final String key : keys) {
            this.items[index] = new ShopItem(key, path + "items." + key, currency.getConfig());
            index++;
        }
    }

    @Override
    public void open(final Player player) {

        final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());
        final MenuBuilder builder = this.createBase();

        for (final ShopItem item : this.items) {
            builder.setItem(item.getSlot(), item.getItem());
            builder.addClickEvent(item.getSlot(), event -> {

                if (!profile.hasBalance(this.currency, item.getPrice())) {
                    this.currency.getMessageCache().sendMessage(player, "messages.not-enough");
                    return;
                }

                for (final String command : item.getCommands()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                            .replace("%player%", player.getName()));
                }

                profile.removeCurrency(this.currency, item.getPrice());

                this.currency.getMessageCache().sendMessage(player, "messages.purchased", new PlaceholderReplacer()
                        .addPlaceholder("%cost%", Utils.format(item.getPrice()))
                        .addPlaceholder("%item%", item.getName()));

            });
        }

        player.openInventory(builder.build(player, this.plugin.getPlaceholders()));
    }
}
