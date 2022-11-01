package net.abyssdev.abysseconomy.player.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PlayerCurrencyData {

    private boolean paymentsToggled = true;
    private double amount;

    public PlayerCurrencyData add(final double amount) {
        this.amount += amount;
        return this;
    }

    public PlayerCurrencyData take(final double amount) {
        if (this.amount - amount < 0) {
            this.amount = 0;
            return this;
        }

        this.amount -= amount;
        return this;
    }
}