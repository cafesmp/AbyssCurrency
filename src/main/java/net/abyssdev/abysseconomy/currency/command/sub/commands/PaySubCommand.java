package net.abyssdev.abysseconomy.currency.command.sub.commands;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.api.events.CurrencyGainEvent;
import net.abyssdev.abysseconomy.api.reason.CurrencyGainReason;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.logger.AbyssLogger;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;

import java.util.logging.Level;

/**
 * The pay sub command
 * @author Relocation
 */
public final class PaySubCommand extends CurrencySubCommand {

    /**
     * Constructs a new PaySubCommand
     * @param plugin The abyss economy plugin
     */
    public PaySubCommand(final AbyssEconomy plugin, final Currency currency) {
        super(plugin, currency,2, currency.getConfig().getBoolean("sub-commands.pay"), "player-help", Sets.immutable.of("pay"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!(sender instanceof Player)) {
            AbyssLogger.log(Level.INFO, "You must be a player!");
            return;
        }

        final Player player = (Player) sender;
        final Player target = context.asPlayer(0);

        final double amount = context.asDouble(1);

        if (target == null || amount <= 0 || target.getUniqueId().equals(player.getUniqueId())) {
            this.currency.getMessageCache().sendMessage(sender, "messages.player-help");
            return;
        }

        final CurrencyPlayer targetProfile = this.plugin.getPlayerStorage().get(target.getUniqueId());

        if (!targetProfile.isToggled(this.currency)) {
            this.currency.getMessageCache().sendMessage(player, "messages.target-payments-toggled-off", new PlaceholderReplacer()
                    .addPlaceholder("%player%", target.getName()));

            return;
        }

        final CurrencyPlayer playerProfile = this.plugin.getPlayerStorage().get(player.getUniqueId());

        if (!playerProfile.hasBalance(this.currency, amount)) {
            this.currency.getMessageCache().sendMessage(player, "messages.not-enough");
            return;
        }

        playerProfile.removeCurrency(this.currency, amount);
        targetProfile.addCurrency(this.currency, amount, CurrencyGainReason.PAID);

        this.currency.getMessageCache().sendMessage(sender, "messages.paid", new PlaceholderReplacer()
                .addPlaceholder("%player%", target.getName())
                .addPlaceholder("%amount%", FormatUtil.format(this.currency, amount)));

        this.currency.getMessageCache().sendMessage(target, "messages.received-payment", new PlaceholderReplacer()
                .addPlaceholder("%player%", sender.getName())
                .addPlaceholder("%amount%", FormatUtil.format(this.currency, amount)));

        new CurrencyGainEvent(target, this.currency, targetProfile, CurrencyGainReason.PAID, amount).fire(this.plugin);
    }
}
