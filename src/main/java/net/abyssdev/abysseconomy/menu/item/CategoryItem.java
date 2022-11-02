package net.abyssdev.abysseconomy.menu.item;

import lombok.Getter;
import net.abyssdev.abysslib.builders.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

@Getter
public final class CategoryItem {

    private final String name;
    private final ItemStack openItem, closedItem;
    private final int slot;

    public CategoryItem(final FileConfiguration config, final String path, final String name) {

        System.out.println(config.contains(path ));
        this.openItem = new ItemBuilder(config, path + ".open").parse();
        this.closedItem = new ItemBuilder(config, path + ".closed").parse();
        this.slot = config.getInt(path + ".slot");
        this.name = name;
    }

}
