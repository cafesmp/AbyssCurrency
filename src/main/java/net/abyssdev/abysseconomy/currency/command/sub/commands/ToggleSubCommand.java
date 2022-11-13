package net.abyssdev.abysseconomy.currency.command.sub.commands;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.logger.AbyssLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;
import java.util.logging.Level;

/**
 * The recalculation sub command
 * @author Relocation
 */
public final class ToggleSubCommand extends CurrencySubCommand {

    private final Set<String> aliases = Sets.immutable.of("toggle", "togglemessages", "togglemsgs").castToSet();

    /**
     * Constructs a new RecalcSubCommand
     * @param plugin The abyss economy plugin
     */
    public ToggleSubCommand(final AbyssEconomy plugin, final Currency currency) {
        super(plugin, currency,0, currency.getConfig().getBoolean("sub-commands.toggle"), "admin-help");
    }

    @Override
    public Set<String> aliases() {
        return this.aliases;
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!(sender instanceof Player)) {
            AbyssLogger.log(Level.INFO, "You must be a player!");
            return;
        }

        final Player player = (Player) sender;
        final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());

        profile.toggleMessages(this.currency);

        this.currency.getMessageCache().sendMessage(player, "messages.messages-toggled-" + profile.isToggled(this.currency));
    }
}
