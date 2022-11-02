package net.abyssdev.abysseconomy.api.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.api.reason.CurrencyGainReason;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The event that fires when a currency is gained
 * @author Relocation
 */
@Getter
@RequiredArgsConstructor
public final class CurrencyGainEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final CurrencyPlayer profile;
    private final CurrencyGainReason reason;
    private final double amount;

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public void fire(final AbyssEconomy plugin) {
        plugin.getServer().getPluginManager().callEvent(this);
    }

}