package net.abyssdev.abysscurrency.currency;

import lombok.Getter;
import net.abyssdev.abysscurrency.AbyssCurrency;
import net.abyssdev.abysscurrency.currency.command.CurrencyCommand;
import net.abyssdev.abysscurrency.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysscurrency.currency.command.sub.commands.*;
import net.abyssdev.abysscurrency.utils.file.FileUtils;
import net.abyssdev.abysslib.text.MessageCache;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * The main currency object
 * @author Relocation
 */
@Getter
public final class Currency {

    private final String name, baseCommand, commandDescription;
    private final List<String> aliases;

    private final File configFile;
    private final FileConfiguration config;

    private final CurrencyCommand command;
    private final MessageCache messageCache;

    private final boolean solid;

    /**
     * Constructs a new Currency
     * @param plugin The abyss currency plugin
     * @param name The currency name taken from the file name without the file extension
     */
    public Currency(final AbyssCurrency plugin, final String name) {
        this.name = name;
        this.configFile = new File(plugin.getCurrencyFolder(), name + ".yml");

        if (!this.configFile.exists()) {
            FileUtils.copy(FileUtils.getInputFromJar("mobcoins.yml"), this.configFile);
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        this.baseCommand = this.config.getString("base-command");
        this.commandDescription = this.config.getString("description");
        this.aliases = this.config.getStringList("aliases");

        this.messageCache = new MessageCache(this.config);

        for (final String key : this.config.getConfigurationSection("messages").getKeys(false)) {
            this.messageCache.loadMessage("messages." + key);
        }

        this.messageCache.loadMessage("mobs.message");
        this.messageCache.loadMessage("blocks.message");
        this.messageCache.loadMessage("on-exp-level-up.message");
        this.messageCache.loadMessage("on-exp-gain.message");
        this.messageCache.loadMessage("player-kill.message");

        this.solid = this.config.getString("currency-type").equalsIgnoreCase("solid");

        this.command = new CurrencyCommand(plugin, this);
        this.command.register();

        this.registerSubCommands(
                new GiveSubCommand(plugin, this),
                new HelpSubCommand(plugin, this),
                new BalanceSubCommand(plugin, this),
                new TakeSubCommand(plugin, this),
                new SetSubCommand(plugin, this),
                new PaySubCommand(plugin, this),
                new PayToggleSubCommand(plugin, this));
    }

    private void registerSubCommands(final CurrencySubCommand... commands) {
        for (final CurrencySubCommand command : commands) {
            if (command.isEnabled()) {
                this.command.register(command);
            }
        }
    }

}