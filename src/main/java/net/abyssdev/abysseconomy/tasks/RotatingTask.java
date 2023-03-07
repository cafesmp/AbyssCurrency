package net.abyssdev.abysseconomy.tasks;

import lombok.SneakyThrows;
import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.shop.Shop;
import net.abyssdev.abysseconomy.shop.impl.RotatingShop;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.runnable.AbyssRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * Rotating shop task
 * @author Relocation
 */
public final class RotatingTask extends AbyssRunnable {

    private final AbyssEconomy plugin;

    /**
     * Constructs a new RotatingTask
     * @param plugin The abyss economy plugin
     */
    public RotatingTask(final AbyssEconomy plugin) {
        this.plugin = plugin;
        this.runTaskTimer(this.plugin, 20L, 20L);
    }

    @Override @SneakyThrows
    public void run() {
        for (final Currency currency : this.plugin.getCurrencyRegistry().values()) {
            for (final Shop shop : currency.getShopRegistry().values()) {
                if (!(shop instanceof RotatingShop)) {
                    continue;
                }

                final RotatingShop rotatingShop = (RotatingShop) shop;

                if (rotatingShop.getTimeUntilRotation() > 0) {
                    rotatingShop.setTimeUntilRotation(rotatingShop.getTimeUntilRotation() - 1);
                    continue;
                }

                final Date current = new Date();
                final Date openTime = this.plugin.getDateFormat().parse(rotatingShop.getTime());

                rotatingShop.setTimeUntilRotation(rotatingShop.getInterval());

                if (!current.after(openTime)) {
                    continue;
                }

                final PlaceholderReplacer replacer = new PlaceholderReplacer().addPlaceholder("%shop%", rotatingShop.getName());

                for (final Player player : Bukkit.getOnlinePlayers()) {
                    rotatingShop.getCurrency().getMessageCache().sendMessage(player, "messages.rotated", replacer);
                }

                rotatingShop.rotateItems();
            }
        }
    }
}