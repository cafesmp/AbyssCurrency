package net.abyssdev.abysseconomy.currency.command.sub;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysslib.command.AbyssSubCommand;
import org.eclipse.collections.api.set.ImmutableSet;

/**
 * The sub command abstract for all currency sub commands
 * @author Relocation
 */
public abstract class CurrencySubCommand extends AbyssSubCommand<AbyssEconomy> {

    protected final AbyssEconomy plugin;
    protected final Currency currency;
    protected final boolean enabled;

    /**
     * Constructs a new CurrencySubCommand
     * @param plugin The abyss economy plugin
     * @param currency The currency
     * @param required The required amount of arguments
     * @param enabled Whether the sub command is enabled in the config or not
     * @param aliases The sub command aliases
     */
    public CurrencySubCommand(final AbyssEconomy plugin,
                              final Currency currency,
                              final int required,
                              final boolean enabled,
                              final ImmutableSet<String> aliases
    ) {
        super(plugin, required, aliases);

        this.plugin = plugin;
        this.currency = currency;
        this.enabled = enabled;
    }

    /**
     * Constructs a new CurrencySubCommand
     * @param plugin The abyss economy plugin
     * @param currency The currency
     * @param required The required amount of arguments
     * @param enabled Whether the sub command is enabled in the config or not
     * @param message The message key from the currency config
     * @param aliases The sub command aliases
     */
    public CurrencySubCommand(final AbyssEconomy plugin,
                              final Currency currency,
                              final int required,
                              final boolean enabled,
                              final String message,
                              final ImmutableSet<String> aliases
    ) {
        super(plugin, required, currency.getMessageCache().getMessage("messages." + message), aliases);

        this.plugin = plugin;
        this.currency = currency;
        this.enabled = enabled;
    }


    public boolean isEnabled() {
        return this.enabled;
    }
}