package net.abyssdev.abysseconomy.shop;

import lombok.Getter;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;

@Getter
public abstract class Shop extends AbyssMenu {

    protected final AbyssEconomy plugin;
    protected final Currency currency;
    private final String name, time;

    public Shop(final AbyssEconomy plugin, final Currency currency, final String path, final String name) {
        super(currency.getConfig(), path);

        this.plugin = plugin;
        this.name = name;
        this.currency = currency;
        this.time = currency.getConfig().getString(path + "open-time");
    }
}
