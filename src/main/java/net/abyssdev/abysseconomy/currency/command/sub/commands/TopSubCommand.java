package net.abyssdev.abysseconomy.currency.command.sub.commands;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.logger.AbyssLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;
import java.util.logging.Level;

/**
 * The top sub command
 * @author Relocation
 */
public final class TopSubCommand extends CurrencySubCommand {

    private final Set<String> aliases = Sets.immutable.of("top").castToSet();

    /**
     * Constructs a new TopSubCommand
     * @param plugin The abyss economy plugin
     */
    public TopSubCommand(final AbyssEconomy plugin, final Currency currency) {
        super(plugin, currency,0, currency.getConfig().getBoolean("sub-commands.top"), "player-help");
    }

    @Override
    public Set<String> aliases() {
        return this.aliases;
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!(sender instanceof Player)) {
            AbyssLogger.log(Level.INFO, "Only players can do this!");
            return;
        }

        this.currency.getTopMenu().open((Player) sender);
    }
}
