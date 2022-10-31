package net.abyssdev.abysscurrency.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.abyssdev.abysscurrency.currency.Currency;
import net.abyssdev.abysscurrency.player.data.PlayerCurrencyData;
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

    public void removeCurrency(final Currency currency, final double amount) {
        this.currencies.put(currency.getName(), this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).take(amount));
    }

    public double getBalance(final Currency currency) {
        return this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).getAmount();
    }

    public boolean hasBalance(final Currency currency, final double amount) {
        return this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).getAmount() >= amount;
    }

    public boolean togglePayments(final Currency currency) {
        final PlayerCurrencyData data = this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData());

        data.setPaymentsToggled(!data.isPaymentsToggled());

        return data.isPaymentsToggled();
    }

    public boolean isToggled(final Currency currency) {
        return this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).isPaymentsToggled();
    }

}