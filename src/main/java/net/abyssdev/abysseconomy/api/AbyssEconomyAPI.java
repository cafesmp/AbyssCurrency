package net.abyssdev.abysseconomy.api;

import lombok.experimental.UtilityClass;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;

import java.util.UUID;

/**
 * The api class
 * @author Relocation
 */
@UtilityClass
public final class AbyssEconomyAPI {

    /**
     * Gets the player currency profile
     * @param uuid The player's UUID
     * @return Returns the profile
     */
    public CurrencyPlayer getProfile(final UUID uuid) {
        return AbyssEconomy.get().getPlayerStorage().get(uuid);
    }

    /**
     * Gets a currency by name
     * @param currency The currency name
     * @return Returns the currency
     */
    public Currency getCurrency(final String currency) {
        return AbyssEconomy.get().getCurrencyRegistry().get(currency).orElse(null);
    }
}