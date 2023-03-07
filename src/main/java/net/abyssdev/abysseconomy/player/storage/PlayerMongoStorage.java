package net.abyssdev.abysseconomy.player.storage;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.player.registry.PlayerRegistry;
import net.abyssdev.abysslib.storage.mongo.MongoStorage;

import java.util.UUID;

/**
 * The storage class for player profiles
 * @author Relocation
 */
public final class PlayerMongoStorage extends MongoStorage<UUID, CurrencyPlayer> {

    /**
     * Constructs a new PlayerJsonStorage
     * @param plugin The abyss economy plugin
     */
    public PlayerMongoStorage(final AbyssEconomy plugin) {
        super(
                plugin.getSettingsConfig().getString("storage.uri"),
                plugin.getSettingsConfig().getString("storage.database"),
                AbyssEconomy.class, CurrencyPlayer.class, new PlayerRegistry()
        );
    }

    @Override
    public CurrencyPlayer constructValue(final UUID key) {
        return new CurrencyPlayer(key);
    }
}