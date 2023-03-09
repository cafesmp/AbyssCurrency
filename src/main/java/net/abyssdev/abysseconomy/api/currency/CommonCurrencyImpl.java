package net.abyssdev.abysseconomy.api.currency;

import net.abyssdev.abysseconomy.api.reason.CurrencyGainReason;
import net.abyssdev.abysseconomy.currency.Currency;
import org.bukkit.OfflinePlayer;

//TEMPORARY CLASS MADE FOR OBFUSCATION
public final class CommonCurrencyImpl {

    private final Currency currency;

    public CommonCurrencyImpl(final Currency currency) {
        this.currency = currency;
    }

    public String getName() {
        return this.currency.getName();
    }

    public double getBalance(final OfflinePlayer offlinePlayer) {
        return this.currency.getPlugin().getPlayerStorage().get(offlinePlayer.getUniqueId()).getBalance(this.currency);
    }

    public void addBalance(final OfflinePlayer offlinePlayer, final double v) {
        this.currency.getPlugin().getPlayerStorage().get(offlinePlayer.getUniqueId()).addCurrency(this.currency, v, CurrencyGainReason.PLUGIN);
    }

    public void withdrawBalance(final OfflinePlayer offlinePlayer, final double v) {
        this.currency.getPlugin().getPlayerStorage().get(offlinePlayer.getUniqueId()).removeCurrency(this.currency, v);
    }

    public boolean hasBalance(final OfflinePlayer offlinePlayer, final double v) {
        return this.currency.getPlugin().getPlayerStorage().get(offlinePlayer.getUniqueId()).hasBalance(this.currency, v);
    }



}
