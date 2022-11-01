package net.abyssdev.abysscurrency.currency.command.sub.commands;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.currency.Currency;
import net.abyssdev.abysscurrency.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysscurrency.player.CurrencyPlayer;
import net.abyssdev.abysscurrency.utils.format.FormatUtil;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.logger.AbyssLogger;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.scheduler.AbyssScheduler;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * The balance sub command
 * @author Relocation
 */
public final class BalanceSubCommand extends CurrencySubCommand {

    private final Set<String> aliases = Sets.immutable.of("bal", "balance").castToSet();

    /**
     * Constructs a new BalanceSubCommand
     * @param plugin The abyss currency plugin
     */
    public BalanceSubCommand(final AbyssCurrency plugin, final Currency currency) {
        super(plugin, currency, 0, currency.getConfig().getBoolean("sub-commands.balance"), "player-help");
    }

    @Override
    public Set<String> aliases() {
        return this.aliases;
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (context.getArguments().length == 0 && !(sender instanceof Player)) {
            AbyssLogger.log(Level.INFO, "You must be a player!");
            return;
        } else if (context.getArguments().length == 0) {
            final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(((Player) sender).getUniqueId());

            this.currency.getMessageCache().sendMessage(sender, "messages.balance", new PlaceholderReplacer()
                    .addPlaceholder("%amount%", FormatUtil.format(this.currency, profile.getBalance(this.currency))));
            return;
        }


        CompletableFuture.runAsync(() -> {
            final OfflinePlayer player = context.asOfflinePlayer(0);

            if (!player.hasPlayedBefore()) {
                this.currency.getMessageCache().sendMessage(sender, "messages.balance-other", new PlaceholderReplacer()
                        .addPlaceholder("%player%", player.getName())
                        .addPlaceholder("%amount%", "0"));
                return;
            }

            AbyssScheduler.sync().run(() -> {
                final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());

                this.currency.getMessageCache().sendMessage(sender, "messages.balance-other", new PlaceholderReplacer()
                        .addPlaceholder("%player%", player.getName())
                        .addPlaceholder("%amount%", FormatUtil.format(this.currency, profile.getBalance(this.currency))));
            });
        });
    }
}
