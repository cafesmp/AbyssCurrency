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
 * The give sub command
 * @author Relocation
 */
public final class GiveSubCommand extends CurrencySubCommand {

    private final Set<String> aliases = Sets.immutable.of("give").castToSet();

    /**
     * Constructs a new GiveSubCommand
     * @param plugin The abyss currency plugin
     */
    public GiveSubCommand(final AbyssCurrency plugin, final Currency currency) {
        super(plugin, currency,2, currency.getConfig().getBoolean("sub-commands.give"), "admin-help");
    }

    @Override
    public Set<String> aliases() {
        return this.aliases;
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("abysscurrency.admin")) {
            this.currency.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        final Player target = context.asPlayer(0);
        final double amount = context.asDouble(1);

        if (target == null || amount <= 0) {
            this.currency.getMessageCache().sendMessage(sender, "messages.admin-help");
            return;
        }

        this.plugin.getPlayerStorage().get(target.getUniqueId()).addCurrency(this.currency, amount);

        this.currency.getMessageCache().sendMessage(sender, "messages.given-admin", new PlaceholderReplacer()
                .addPlaceholder("%player%", target.getName())
                .addPlaceholder("%amount%", Utils.format(this.currency.isSolid() ? (long) amount : amount)));

        this.currency.getMessageCache().sendMessage(target, "messages.given-player", new PlaceholderReplacer()
                .addPlaceholder("%amount%", Utils.format(this.currency.isSolid() ? (long) amount : amount)));
    }
}
