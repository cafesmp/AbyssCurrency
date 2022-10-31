package net.abyssdev.abysscurrency.currency.command.sub.commands;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.currency.Currency;
import net.abyssdev.abysscurrency.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysscurrency.player.CurrencyPlayer;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.logger.AbyssLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;
import java.util.logging.Level;

/**
 * The paytoggle sub command
 * @author Relocation
 */
public final class PayToggleSubCommand extends CurrencySubCommand {

    private final Set<String> aliases = Sets.immutable.of("togglepay", "paytoggle").castToSet();

    /**
     * Constructs a new PayToggleSubCommand
     * @param plugin The abyss currency plugin
     */
    public PayToggleSubCommand(final AbyssCurrency plugin, final Currency currency) {
        super(plugin, currency,0, currency.getConfig().getBoolean("sub-commands.paytoggle"), "player-help");
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

        profile.togglePayments(this.currency);

        this.currency.getMessageCache().sendMessage(player, "messages.payments-toggled-" + profile.isToggled(this.currency));
    }
}
