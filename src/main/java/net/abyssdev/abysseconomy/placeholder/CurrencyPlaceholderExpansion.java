package net.abyssdev.abysseconomy.placeholder;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * The currency placeholder expansion
 * @author Relocation
 */
@RequiredArgsConstructor
public final class CurrencyPlaceholderExpansion extends PlaceholderExpansion {

    private static final NumberFormat FORMATTER = NumberFormat.getInstance(Locale.ENGLISH);

    static {
        FORMATTER.setMaximumFractionDigits(2);
        FORMATTER.setMinimumFractionDigits(0);
    }

    private static final String THOUSANDS_SUFFIX = "k";
    private static final String MILLIONS_SUFFIX = "M";
    private static final String BILLIONS_SUFFIX = "B";
    private static final String TRILLIONS_SUFFIX = "T";
    private static final String QUADRILLIONS = "Q";

    private final AbyssEconomy plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "abysseconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Relocation";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(final OfflinePlayer player, @NotNull final String params) {
        final String[] data = params.split("_");

        if (!this.plugin.getCurrencyRegistry().containsKey(data[0])) {
            return "null";
        }

        if (data.length <= 1) {
            final Currency currency = this.plugin.getCurrencyRegistry().get(params).get();
            return FormatUtil.format(currency, this.plugin.getPlayerStorage().get(player.getUniqueId()).getBalance(currency));
        }

        final Currency currency = this.plugin.getCurrencyRegistry().get(data[0]).get();

        switch (data[1].toLowerCase()) {
            default:
            case "formatted": {
                return this.format(currency, this.plugin.getPlayerStorage().get(player.getUniqueId()).getBalance(currency));
            }

            case "raw": {
                final double balance = this.plugin.getPlayerStorage().get(player.getUniqueId()).getBalance(currency);
                return currency.isSolid() ? String.valueOf((long) balance) : String.valueOf(balance);
            }
        }
    }

    private String format(final Currency currency, final double balance) {
        if (balance < 1000) {
            return FORMATTER.format(balance);
        } else if (balance < 1000000L) {
            return FORMATTER.format(balance / 1000) + THOUSANDS_SUFFIX;
        } else if (balance < 1000000000L) {
            return FORMATTER.format(balance / 1000000L) + MILLIONS_SUFFIX;
        } else if (balance < 1000000000000L) {
            return FORMATTER.format(balance / 1000000000L) + BILLIONS_SUFFIX;
        } else if (balance < 1000000000000000L) {
            return FORMATTER.format(balance / 1000000000000L) + TRILLIONS_SUFFIX;
        } else if (balance < 1000000000000000000L) {
            return FORMATTER.format(balance / 1000000000000000L) + QUADRILLIONS;
        }
        return FormatUtil.format(currency, balance);
    }

}