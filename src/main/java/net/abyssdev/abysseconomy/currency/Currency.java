package net.abyssdev.abysseconomy.currency;

import lombok.Getter;
import lombok.Setter;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.command.CurrencyCommand;
import net.abyssdev.abysseconomy.currency.command.sub.CurrencySubCommand;
import net.abyssdev.abysseconomy.currency.command.sub.commands.*;
import net.abyssdev.abysseconomy.menu.TopMenu;
import net.abyssdev.abysseconomy.shop.Shop;
import net.abyssdev.abysseconomy.shop.registry.ShopRegistry;
import net.abyssdev.abysseconomy.tasks.TopUpdateTask;
import net.abyssdev.abysseconomy.utils.file.FileUtils;
import net.abyssdev.abysslib.patterns.registry.Registry;
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
@Setter
public final class Currency {

    private final String name, baseCommand, commandDescription;
    private final List<String> aliases;

    private final File configFile;
    private final FileConfiguration config;

    private final CurrencyCommand command;
    private final MessageCache messageCache;

    private final Registry<String, Shop> shopRegistry;
    private final TopMenu topMenu;

    private final boolean solid;
    private boolean event;

    /**
     * Constructs a new Currency
     * @param plugin The abyss economy plugin
     * @param name The currency name taken from the file name without the file extension
     */
    public Currency(final AbyssEconomy plugin, final String name) {
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

        this.shopRegistry = new ShopRegistry(plugin, this);
        this.topMenu = new TopMenu(plugin, this);

        this.command = new CurrencyCommand(plugin, this);
        this.command.register();

        this.registerSubCommands(
                new GiveSubCommand(plugin, this),
                new HelpSubCommand(plugin, this),
                new BalanceSubCommand(plugin, this),
                new TakeSubCommand(plugin, this),
                new SetSubCommand(plugin, this),
                new PaySubCommand(plugin, this),
                new PayToggleSubCommand(plugin, this),
                new EventSubCommand(plugin, this),
                new TopSubCommand(plugin, this),
                new RecalcSubCommand(plugin, this));

        if (this.config.getBoolean("top.enabled")) {
            new TopUpdateTask(plugin, this, this.config.getInt("top.update-time") * 20L);
        }

    }

    public void toggleEvent() {
        this.event = !this.event;
        this.messageCache.getMessage("messages.event-" + this.event).broadcast();
    }

    private void registerSubCommands(final CurrencySubCommand... commands) {
        for (final CurrencySubCommand command : commands) {
            if (command.isEnabled()) {
                this.command.register(command);
            }
        }
    }

}