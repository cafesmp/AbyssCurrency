package net.abyssdev.abysscurrency.currency.command.sub;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.currency.Currency;
import net.abyssdev.abysslib.command.SubCommand;

/**
 * The sub command abstract for all currency sub commands
 * @author Relocation
 */
public abstract class CurrencySubCommand extends SubCommand {

    protected final AbyssCurrency plugin;
    protected final Currency currency;
    protected final boolean enabled;

    public CurrencySubCommand(final AbyssCurrency plugin, final Currency currency, final int required, final boolean enabled) {
        super(required);

        this.plugin = plugin;
        this.currency = currency;
        this.enabled = enabled;
    }

    public CurrencySubCommand(final AbyssCurrency plugin, final Currency currency, final int required, final boolean enabled, final String message) {
        super(required, currency.getMessageCache().getMessage("messages." + message));

        this.plugin = plugin;
        this.currency = currency;
        this.enabled = enabled;
    }


    public boolean isEnabled() {
        return this.enabled;
    }
}