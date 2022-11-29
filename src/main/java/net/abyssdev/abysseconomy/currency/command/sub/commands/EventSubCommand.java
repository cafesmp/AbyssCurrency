package net.abyssdev.abysseconomy.currency.command.sub.commands;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysseconomy.currency.multiplier.CurrencyMultiplierEvent;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.command.CommandSender;
import org.eclipse.collections.api.factory.Sets;

/**
 * The event sub command
 *
 * @author Relocation
 */
public final class EventSubCommand extends CurrencySubCommand {

    /**
     * Constructs a new EventSubCommand
     *
     * @param plugin The abyss economy plugin
     */
    public EventSubCommand(final AbyssEconomy plugin, final Currency currency) {
        super(plugin, currency, 0, currency.getConfig().getBoolean("sub-commands.event"), "admin-help", Sets.immutable.of("event"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("abysseconomy.admin")) {
            this.currency.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        final CurrencyMultiplierEvent event = this.currency.getEvent();

        if (context.getArguments().length == 0) {
            this.currency.getMessageCache().sendMessage(sender, "messages.event-time", new PlaceholderReplacer()
                    .addPlaceholder("%time%", Utils.getTimeFormat(event.getRunningTime() - System.currentTimeMillis())));

            return;
        }

        switch (context.asString(0)) {
            case "stop": {
                if (!event.isActive()) {
                    this.currency.getMessageCache().sendMessage(sender, "messages.event-already-stopped");
                    return;
                }

                event.end();
                return;
            }

            case "start": {
                if (context.getArguments().length < 2) {
                    this.currency.getMessageCache().sendMessage(sender, "messages.admin-help");
                    return;
                }

                if (event.isActive()) {
                    this.currency.getMessageCache().sendMessage(sender, "messages.event-already-active");
                    return;
                }

                event.start(context.asInt(1));
                return;
            }

            default: {
                this.currency.getMessageCache().sendMessage(sender, "messages.admin-help");
            }
        }

    }
}
