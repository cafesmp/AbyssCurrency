package net.abyssdev.abysscurrency.player.storage;

import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.player.CurrencyPlayer;
import net.abyssdev.abysscurrency.player.registry.PlayerRegistry;
import net.abyssdev.abysslib.storage.json.JsonStorage;
import net.abyssdev.abysslib.utils.file.Files;

import java.util.UUID;

/**
 * The storage class for player profiles
 * @author Relocation
 */
public final class PlayerJsonStorage extends JsonStorage<UUID, CurrencyPlayer> {

    /**
     * Constructs a new PlayerJsonStorage
     * @param plugin The abyss currency plugin
     */
    public PlayerJsonStorage(final AbyssCurrency plugin) {
        super(Files.file("data.json", plugin), CurrencyPlayer.class, new PlayerRegistry());
    }

    @Override
    public CurrencyPlayer constructValue(final UUID key) {
        return new CurrencyPlayer(key);
    }

}