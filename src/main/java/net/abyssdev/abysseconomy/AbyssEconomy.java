package net.abyssdev.abysseconomy;

import com.google.common.io.Files;
import lombok.Getter;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.registry.CurrencyRegistry;
import net.abyssdev.abysseconomy.listeners.*;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.player.storage.PlayerJsonStorage;
import net.abyssdev.abysseconomy.player.storage.PlayerMongoStorage;
import net.abyssdev.abysseconomy.shop.Shop;
import net.abyssdev.abysseconomy.shop.registry.ShopRegistry;
import net.abyssdev.abysslib.patterns.registry.Registry;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import net.abyssdev.abysslib.storage.Storage;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * AbyssEconomy main class
 * @author Relocation
 */
@Getter
public final class AbyssEconomy extends AbyssPlugin {

    private final PlaceholderReplacer placeholders = new PlaceholderReplacer().setUsePlaceholderAPI(true);
    private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private final Registry<String, Currency> currencyRegistry = new CurrencyRegistry();
    private final FileConfiguration settingsConfig = this.getConfig("settings");

    private Storage<UUID, CurrencyPlayer> playerStorage;
    private File currencyFolder;

    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        this.currencyFolder = new File(this.getDataFolder(), "currencies");

        if (!this.currencyFolder.exists()) {
            this.currencyFolder.mkdir();
        }

        switch (this.settingsConfig.getString("storage.type").toUpperCase()) {
            case "JSON": {
                this.playerStorage = new PlayerJsonStorage(this);
                break;
            }

            case "MONGO": {
                this.playerStorage = new PlayerMongoStorage(this);
                break;
            }
        }

        this.loadCurrencies();

        new EntityDeathListener(this);
        new BreakListener(this);
        new KillListener(this);
        new ExpGainListener(this);
        new ExpLevelUpListener(this);
    }

    @Override
    public void onDisable() {
        this.playerStorage.write();
    }

    private void loadCurrencies() {
        for (final File file : this.currencyFolder.listFiles()) {
            final String name = Files.getNameWithoutExtension(file.getName());
            this.currencyRegistry.register(name, new Currency(this, name));
        }

        if (this.currencyRegistry.values().isEmpty()) {
            this.currencyRegistry.register("mobcoins", new Currency(this, "mobcoins"));
        }
    }

}