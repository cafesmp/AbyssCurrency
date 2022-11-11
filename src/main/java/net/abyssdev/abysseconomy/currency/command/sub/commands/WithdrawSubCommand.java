package net.abyssdev.abysseconomy.currency.command.sub.commands;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.logger.AbyssLogger;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.api.factory.Sets;

import java.util.Set;
import java.util.logging.Level;

/**
 * The withdraw sub command
 * @author Relocation
 */
public final class WithdrawSubCommand extends CurrencySubCommand {

    private final ItemBuilder item;
    private final Set<String> aliases = Sets.immutable.of("withdraw").castToSet();

    /**
     * Constructs a new WithdrawSubCommand
     * @param plugin The abyss economy plugin
     */
    public WithdrawSubCommand(final AbyssEconomy plugin, final Currency currency) {
        super(plugin, currency, 1, currency.getConfig().getBoolean("sub-commands.withdraw"), "admin-help");

        this.item = new ItemBuilder(currency.getConfig(), "withdraw-item");
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

        final Player player = (Player) sender;
        final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());

        final double amount = context.asDouble(0);

        if (amount <= 0 || !profile.hasBalance(this.currency, amount)) {
            this.currency.getMessageCache().sendMessage(sender, "messages.player-help");
            return;
        }

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%player%", player.getName())
                .addPlaceholder("%amount%", FormatUtil.format(this.currency, amount));

        profile.removeCurrency(this.currency, amount);

        this.currency.getMessageCache().sendMessage(sender, "messages.withdrawn", replacer);

        ItemStack item = this.item.parse(replacer);
        item = NBTUtils.get().setString(item, "ABYSSECONOMY_WITHDRAW", this.currency.getName() + ";" + amount);

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), item);
            return;
        }

        player.getInventory().addItem(item);
    }
}
