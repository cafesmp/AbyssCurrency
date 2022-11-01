package net.abyssdev.abysseconomy.shop.item;

import lombok.Getter;
import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * The shop item wrapper
 * @author Relocatiom
 */
@Getter
public final class ShopItem {

    private final String name;
    private final ItemStack item;
    private final String[] commands;

    private final int slot;
    private final double price;

    /**
     * Constructs a new ShopItem
     * @param name The name
     * @param path The path
     * @param config The config file
     */
    public ShopItem(final String name, final String path, final FileConfiguration config) {
        this.name = WordUtils.formatText(name
                .toLowerCase()
                .replace("-", "")
                .replace("_", ""));

        this.item = new ItemBuilder(config, path).parse();
        this.slot = config.getInt(path + ".slot");
        this.price = config.getInt(path + ".price");
        this.commands = config.getStringList(path + ".commands").toArray(new String[0]);
    }

}
