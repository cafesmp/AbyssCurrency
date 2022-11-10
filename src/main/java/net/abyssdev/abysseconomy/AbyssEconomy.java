package net.abyssdev.abysseconomy;

import com.google.common.io.Files;
import lombok.Getter;
import net.abyssdev.abysseconomy.auth.lllIlllIlIIIIlllIIllIlIIIIlllIIllIIIlllIIIl;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.currency.registry.CurrencyRegistry;
import net.abyssdev.abysseconomy.listeners.*;
import net.abyssdev.abysseconomy.placeholder.CurrencyPlaceholderExpansion;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.player.storage.PlayerJsonStorage;
import net.abyssdev.abysseconomy.player.storage.PlayerMongoStorage;
import net.abyssdev.abysseconomy.tasks.MultiplierEventTask;
import net.abyssdev.abysseconomy.tasks.RotatingTask;
import net.abyssdev.abysslib.patterns.registry.Registry;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import net.abyssdev.abysslib.storage.Storage;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * AbyssEconomy main class
 * @author Relocation
 */
@Getter
public final class AbyssEconomy extends AbyssPlugin {

    private static AbyssEconomy instance;

    private final PlaceholderReplacer placeholders = new PlaceholderReplacer().setUsePlaceholderAPI(true);
    private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private final Registry<String, Currency> currencyRegistry = new CurrencyRegistry();
    private final FileConfiguration settingsConfig = this.getConfig("settings");

    private Storage<UUID, CurrencyPlayer> playerStorage;
    private File currencyFolder;

    @Override
    public void onEnable() {
        AbyssEconomy.instance = this;

        this.currencyFolder = new File(this.getDataFolder(), "currencies");

        if (!this.currencyFolder.exists()) {
            this.currencyFolder.mkdir();
        }

        try {
            final Class<?> fileClazz = Class.forName("java.io.File");
            final Constructor<?> constructor = fileClazz.getConstructor(Class.forName("java.io.File"), Class.forName("java.lang.String"));
            final Object licenseFile = constructor.newInstance(this.getDataFolder(), "license.txt");

            if(!((Boolean) fileClazz.getMethod("exists").invoke(licenseFile))) {
                final Object parentFile = fileClazz.getMethod("getParentFile").invoke(licenseFile);
                fileClazz.getMethod("mkdir").invoke(parentFile);

                this.saveResource("license.txt", false);
            }

            final Class<?> scannerClazz = Class.forName("java.util.Scanner");
            final Constructor<?> scannerConstructor = scannerClazz.getConstructor(Class.forName("java.io.File"));
            final Object scanner = scannerConstructor.newInstance(licenseFile);
            final Object text = scannerClazz.getMethod("nextLine").invoke(scanner);
            new lllIlllIlIIIIlllIIllIlIIIIlllIIllIIIlllIIIl(this, (String) Class.forName("java.lang.String").cast(text));
        } catch (Exception exception) {
            exception.printStackTrace();
            try {
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
                Class.forName("java.lang.System").getDeclaredMethod("exit", int.class).invoke(null, 0);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
            System.exit(0);
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

        new RotatingTask(this);
        new MultiplierEventTask(this);

        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new CurrencyPlaceholderExpansion(this).register();
        }
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

    public static AbyssEconomy get() {
        return AbyssEconomy.instance;
    }

}