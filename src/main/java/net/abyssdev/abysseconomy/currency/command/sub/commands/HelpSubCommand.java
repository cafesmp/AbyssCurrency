package net.abyssdev.abysseconomy.currency.command.sub.commands;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;

/**
 * The help sub command
 * @author Relocation
 */
public final class HelpSubCommand extends CurrencySubCommand {

    /**
     * Constructs a new HelpSubCommand
     * @param plugin The abyss economy plugin
     */
    public HelpSubCommand(final AbyssEconomy plugin, final Currency currency) {
        super(plugin, currency, 0, currency.getConfig().getBoolean("sub-commands.help"), Sets.immutable.of("help"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("abysseconomy.admin")) {
            this.currency.getMessageCache().sendMessage(sender, "messages.player-help");
            return;
        }

        this.currency.getMessageCache().sendMessage(sender, "messages.admin-help");
    }
}
