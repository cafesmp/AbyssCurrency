package net.abyssdev.abysseconomy.currency.command;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysslib.command.Command;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The currency command
 * @author Relocation
 */
public final class CurrencyCommand extends Command<CommandSender> {

    private final AbyssEconomy plugin;
    private final Currency currency;

    /**
     * Constructs a new CurrencyCommand
     * @param currency The currency object
     */
    public CurrencyCommand(final AbyssEconomy plugin, final Currency currency) {
        super(currency.getBaseCommand(), currency.getCommandDescription(), currency.getAliases(), CommandSender.class);

        this.plugin = plugin;
        this.currency = currency;
    }

    @Override
    public void execute(final CommandContext<CommandSender> context) {
        final CommandSender sender = context.getSender();

        if (!(sender instanceof Player)) {
            this.currency.getMessageCache().sendMessage(sender, "messages.admin-help");
            return;
        }

        final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(((Player) sender).getUniqueId());

        this.currency.getMessageCache().sendMessage(context.getSender(), "messages.balance", new PlaceholderReplacer()
                .addPlaceholder("%amount%", Utils.format(this.currency.isSolid()
                        ? (long) profile.getBalance(this.currency)
                        : profile.getBalance(this.currency))));
    }

}