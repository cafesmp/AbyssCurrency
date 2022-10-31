package net.abyssdev.abysscurrency.player.storage;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.player.CurrencyPlayer;
import net.abyssdev.abysscurrency.player.registry.PlayerRegistry;
import net.abyssdev.abysslib.storage.credentials.Credentials;
import net.abyssdev.abysslib.storage.json.JsonStorage;
import net.abyssdev.abysslib.storage.mongo.MongoStorage;
import net.abyssdev.abysslib.utils.file.Files;

import java.util.UUID;

/**
 * The storage class for player profiles
 * @author Relocation
 */
public final class PlayerMongoStorage extends MongoStorage<UUID, CurrencyPlayer> {

    /**
     * Constructs a new PlayerJsonStorage
     * @param plugin The abyss currency plugin
     */
    public PlayerMongoStorage(final AbyssCurrency plugin) {
        super(
                plugin.getSettingsConfig().getString("storage.uri"),
                plugin.getSettingsConfig().getString("storage.database"),
                AbyssCurrency.class, CurrencyPlayer.class, new PlayerRegistry()
        );
    }

    @Override
    public CurrencyPlayer constructValue(final UUID key) {
        return new CurrencyPlayer(key);
    }

}