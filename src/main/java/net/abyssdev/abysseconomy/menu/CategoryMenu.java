package net.abyssdev.abysseconomy.menu;

import lombok.SneakyThrows;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.menu.item.CategoryItem;
import net.abyssdev.abysseconomy.shop.Shop;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Category menu
 * @author Relocation
 */
public final class CategoryMenu extends AbyssMenu {

    private final AbyssEconomy plugin;
    private final Currency currency;
    private final CategoryItem[] categories;

    /**
     * Constructs a new CategoryMenu
     * @param plugin The abyss economy plugin
     * @param currency The currency
     */
    public CategoryMenu(final AbyssEconomy plugin, final Currency currency) {
        super(currency.getConfig(), "menus.main-menu.");

        this.plugin = plugin;
        this.currency = currency;

        final ConfigurationSection section = currency.getConfig().getConfigurationSection("menus.main-menu.categories");
        final Set<String> keys = section.getKeys(false);

        this.categories = new CategoryItem[keys.size()];

        int index = 0;

        for (final String key : currency.getConfig().getConfigurationSection("menus.main-menu.categories").getKeys(false)) {
            this.categories[index] = new CategoryItem(currency.getConfig(), "menus.main-menu.categories." + key, key);
            index++;
        }

    }

    @Override @SneakyThrows
    public void open(final Player player) {

        final MenuBuilder builder = this.createBase();

        for (final CategoryItem item : this.categories) {

            final Optional<Shop> shopOptional = this.currency.getShopRegistry().get(item.getName());

            if (!shopOptional.isPresent()) {
                continue;
            }

            final Shop shop = shopOptional.get();

            final Date current = new Date();
            final Date openTime = this.plugin.getDateFormat().parse(shop.getTime());

            if (current.after(openTime)) {
                builder.setItem(item.getSlot(), item.getOpenItem());
                builder.addClickEvent(item.getSlot(), event -> shop.open(player));
                continue;
            }

            builder.setItem(item.getSlot(), item.getClosedItem());
        }

        player.openInventory(builder.build(player, this.plugin.getPlaceholders()));
    }

}
