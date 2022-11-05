package net.abyssdev.abysseconomy.placeholder;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * The currency placeholder expansion
 * @author Relocation
 */
@RequiredArgsConstructor
public final class CurrencyPlaceholderExpansion extends PlaceholderExpansion {

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
        if (!this.plugin.getCurrencyRegistry().containsKey(params)) {
            return "null";
        }

        final Currency currency = this.plugin.getCurrencyRegistry().get(params).get();
        return FormatUtil.format(currency, this.plugin.getPlayerStorage().get(player.getUniqueId()).getBalance(currency));
    }

}