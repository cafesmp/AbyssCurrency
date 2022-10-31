package net.abyssdev.abysscurrency.currency.command.sub.commands;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.currency.Currency;
import net.abyssdev.abysscurrency.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;

/**
 * The help sub command
 * @author Relocation
 */
public final class HelpSubCommand extends CurrencySubCommand {

    private final Set<String> aliases = Sets.immutable.of("help").castToSet();

    /**
     * Constructs a new HelpSubCommand
     * @param plugin The abyss currency plugin
     */
    public HelpSubCommand(final AbyssCurrency plugin, final Currency currency) {
        super(plugin, currency,0, currency.getConfig().getBoolean("sub-commands.help"));
    }

    @Override
    public Set<String> aliases() {
        return this.aliases;
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("abysscurrency.admin")) {
            this.currency.getMessageCache().sendMessage(sender, "messages.player-help");
            return;
        }

        this.currency.getMessageCache().sendMessage(sender, "messages.admin-help");
    }
}
