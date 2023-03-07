package net.abyssdev.abysseconomy.currency.registry;

import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysslib.patterns.registry.Registry;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;

/**
 * The currency registry
 * @author Relocation
 */
public final class CurrencyRegistry implements Registry<String, Currency> {

    private final Map<String, Currency> currencies = Maps.mutable.empty();

    @Override
    public Map<String, Currency> getRegistry() {
        return this.currencies;
    }
}