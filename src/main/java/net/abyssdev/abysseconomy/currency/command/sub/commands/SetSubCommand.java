package net.abyssdev.abysseconomy.currency.command.sub.commands;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysseconomy.player.data.PlayerCurrencyData;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;

/**
 * The set sub command
 * @author Relocation
 */
public final class SetSubCommand extends CurrencySubCommand {

    private final Set<String> aliases = Sets.immutable.of("set").castToSet();

    /**
     * Constructs a new SetSubCommand
     * @param plugin The abyss economy plugin
     */
    public SetSubCommand(final AbyssEconomy plugin, final Currency currency) {
        super(plugin, currency, 2, currency.getConfig().getBoolean("sub-commands.set"), "admin-help");
    }

    @Override
    public Set<String> aliases() {
        return this.aliases;
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("abysseconomy.admin")) {
            this.currency.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        final Player target = context.asPlayer(0);
        final double amount = context.asDouble(1);

        if (target == null || amount <= 0) {
            this.currency.getMessageCache().sendMessage(sender, "messages.admin-help");
            return;
        }

        this.plugin.getPlayerStorage()
                .get(target.getUniqueId()).getCurrencies()
                .getOrDefault(this.currency.getName(), new PlayerCurrencyData())
                .setAmount(amount);

        this.currency.getMessageCache().sendMessage(sender, "messages.set-admin", new PlaceholderReplacer()
                .addPlaceholder("%player%", target.getName())
                .addPlaceholder("%amount%", FormatUtil.format(this.currency, amount)));

        this.currency.getMessageCache().sendMessage(target, "messages.set-player", new PlaceholderReplacer()
                .addPlaceholder("%amount%", FormatUtil.format(this.currency, amount)));
    }
}
