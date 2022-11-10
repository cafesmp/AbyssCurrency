package net.abyssdev.abysseconomy.currency.multiplier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.abyssdev.abysseconomy.currency.Currency;

@Getter
@Setter
@RequiredArgsConstructor
public final class CurrencyMultiplierEvent {

    private final Currency currency;
    private final double multiplier;

    private long runningTime;
    private boolean active;

    public void start(final int seconds) {
        this.active = true;
        this.currency.getMessageCache().getMessage("messages.event-true").broadcast();
        this.runningTime = System.currentTimeMillis() + (seconds * 1000L);
    }

    public void end() {
        this.active = false;
        this.currency.getMessageCache().getMessage("messages.event-false").broadcast();
    }

}