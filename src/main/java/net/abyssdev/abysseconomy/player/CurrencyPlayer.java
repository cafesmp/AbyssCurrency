package net.abyssdev.abysseconomy.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.abyssdev.abysseconomy.api.reason.CurrencyGainReason;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.data.PlayerCurrencyData;
import net.abyssdev.abysslib.storage.id.Id;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;
import java.util.UUID;

/**
 * The main currency player profile
 *
 * @author Relocation
 */
@Getter
@RequiredArgsConstructor
public final class CurrencyPlayer {

    @Id
    @dev.morphia.annotations.Id
    private final UUID uuid;
    private final Map<String, PlayerCurrencyData> currencies = Maps.mutable.empty();
    private final Map<String, Double> lastSixtySeconds = Maps.mutable.empty();

    public void addCurrency(final Currency currency, final double amount, final CurrencyGainReason reason) {
        final String name = currency.getName();

        if (reason == CurrencyGainReason.NATURAL) {
            this.lastSixtySeconds.put(name, this.lastSixtySeconds.getOrDefault(name, 0D) + amount);
        }

        this.currencies.put(name, this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).add(amount));
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

    public void toggleMessages(final Currency currency) {
        final PlayerCurrencyData data = this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData());
        data.setMessagesToggled(!data.isMessagesToggled());
    }

    public boolean isToggled(final Currency currency) {
        return this.currencies.getOrDefault(currency.getName(), new PlayerCurrencyData()).isPaymentsToggled();
    }

}