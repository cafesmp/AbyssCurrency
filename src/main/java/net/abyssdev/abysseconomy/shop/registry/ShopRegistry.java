package net.abyssdev.abysseconomy.shop.registry;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.shop.Shop;
import net.abyssdev.abysseconomy.shop.impl.RotatingShop;
import net.abyssdev.abysseconomy.shop.impl.StaticShop;
import net.abyssdev.abysslib.patterns.registry.Registry;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;

/**
 * The shop registry
 * @author Relocation
 */
public final class ShopRegistry implements Registry<String, Shop> {

    private final Map<String, Shop> shops = Maps.mutable.empty();

    public ShopRegistry(final AbyssEconomy plugin, final Currency currency) {
        for (final String key : currency.getConfig().getConfigurationSection("shops").getKeys(false)) {
            if (currency.getConfig().getBoolean("shops." + key + ".rotating")) {
                this.shops.put(key, new RotatingShop(plugin, currency, "shops." + key + ".", key));
                continue;
            }

            this.shops.put(key, new StaticShop(plugin, currency, "shops." + key + ".", key));
        }
    }

    @Override
    public Map<String, Shop> getRegistry() {
        return this.shops;
    }
}