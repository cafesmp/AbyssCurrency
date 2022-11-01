package net.abyssdev.abysscurrency.currency.drop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.abyssdev.abysscurrency.currency.Currency;

/**
 * Wrapper for currency drop data
 * @author Relocation
 */
@Getter
@RequiredArgsConstructor
public final class CurrencyDrop {

    private final Currency currency;
    private final double min, max, chance;

}