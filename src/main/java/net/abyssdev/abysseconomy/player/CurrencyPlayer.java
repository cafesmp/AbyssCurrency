package net.abyssdev.abysseconomy.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.data.PlayerCurrencyData;
import net.abyssdev.abysslib.storage.id.Id;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;
import java.util.UUID;

/**
 * The main currency player profile
 * @author Relocation
 */
@Getter
@RequiredArgsConstructor
public final class CurrencyPlayer {

    @Id @dev.morphia.annotations.Id
    private final UUID uuid;
    private final Map<String, PlayerCurrencyData> currencies = Maps.mutable.empty();

    public void addCurrency(final Currency currency, final double amount) {
        this.currencies.put(currency.getName(), this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).add(amount));
    }

    public void setCurrency(final Currency currency, final double amount) {
        this.currencies.put(currency.getName(), this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).set(amount));
    }

    public void removeCurrency(final Currency currency, final double amount) {
        this.currencies.put(currency.getName(), this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).take(amount));
    }

    public double getBalance(final Currency currency) {
        return this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).getAmount();
    }

    public boolean hasBalance(final Currency currency, final double amount) {
        return this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).getAmount() >= amount;
    }

    public void togglePayments(final Currency currency) {
        final PlayerCurrencyData data = this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData());
        data.setPaymentsToggled(!data.isPaymentsToggled());
    }

    public boolean isToggled(final Currency currency) {
        return this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).isPaymentsToggled();
    }

}