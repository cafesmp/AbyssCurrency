package net.abyssdev.abysseconomy.listeners;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * The interact listener used for Withdraw notes
 * @author Relocation
 */
public final class InteractListener extends AbyssListener<AbyssEconomy> {

    /**
     * Constructs a new InteractListener
     * @param plugin The abyss economy plugin
     */
    public InteractListener(final AbyssEconomy plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        final ItemStack item = event.getItem();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (!NBTUtils.get().contains(item, "ABYSSECONOMY_WITHDRAW")) {
            return;
        }

        final Player player = event.getPlayer();
        final String[] data = NBTUtils.get().getString(item, "ABYSSECONOMY_WITHDRAW").split(";");

        final Optional<Currency> optionalCurrency = this.getPlugin().getCurrencyRegistry().get(data[0]);

        if (!optionalCurrency.isPresent()) {
            return;
        }

        final Currency currency = optionalCurrency.get();
        final double amount = Double.parseDouble(data[1]);

        this.getPlugin().getPlayerStorage().get(player.getUniqueId()).addCurrency(currency, amount);
        currency.getMessageCache().sendMessage(player, "messages.redeemed", new PlaceholderReplacer()
                .addPlaceholder("%amount%", FormatUtil.format(currency, amount)));

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
            return;
        }

        player.setItemInHand(new ItemStack(Material.AIR));
    }
}