package net.abyssdev.abysseconomy.player.storage;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.player.registry.PlayerRegistry;
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
     * @param plugin The abyss economy plugin
     */
    public PlayerJsonStorage(final AbyssEconomy plugin) {
        super(Files.file("data.json", plugin), CurrencyPlayer.class, new PlayerRegistry());
    }

    @Override
    public CurrencyPlayer constructValue(final UUID key) {
        return new CurrencyPlayer(key);
    }
}