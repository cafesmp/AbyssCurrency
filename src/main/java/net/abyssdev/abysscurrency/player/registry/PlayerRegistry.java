package net.abyssdev.abysscurrency.player.registry;

import net.abyssdev.abysscurrency.player.CurrencyPlayer;
import net.abyssdev.abysslib.patterns.registry.Registry;
import org.eclipse.collections.api.factory.Maps;

import java.util.Map;
import java.util.UUID;

/**
 * The player profile registry
 * @author Relocation
 */
public final class PlayerRegistry implements Registry<UUID, CurrencyPlayer> {

    private final Map<UUID, CurrencyPlayer> players = Maps.mutable.empty();

    @Override
    public Map<UUID, CurrencyPlayer> getRegistry() {
        return this.players;
    }

}