package net.abyssdev.abysseconomy.placeholder;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.abyssdev.abysseconomy.AbyssEconomy;
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

}