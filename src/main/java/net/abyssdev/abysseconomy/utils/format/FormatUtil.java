package net.abyssdev.abysseconomy.utils.format;

import lombok.experimental.UtilityClass;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysslib.utils.Utils;

/**
 * Formatting utilities
 * @author Relocation
 */
@UtilityClass
public final class FormatUtil {

    public String format(final Currency currency, final double amount) {
        if (currency.isSolid()) {
            return Utils.format((long) amount);
        }

        return Utils.format(amount);
    }

}