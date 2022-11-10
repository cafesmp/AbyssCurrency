package net.abyssdev.abysseconomy.tasks;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysslib.runnable.AbyssRunnable;

/**
 * The multiplier event task
 * @author Relocation
 */
public final class MultiplierEventTask extends AbyssRunnable {

    private final AbyssEconomy plugin;

    public MultiplierEventTask(final AbyssEconomy plugin) {
        this.plugin = plugin;
        this.runTaskTimer(this.plugin, 0L, 20L);
    }

    @Override
    public void run() {
        for (final Currency currency : this.plugin.getCurrencyRegistry().values()) {
            if (!currency.getEvent().isActive()) {
                continue;
            }

            if (currency.getEvent().getRunningTime() - System.currentTimeMillis() > 0) {
                continue;
            }

            currency.getEvent().end();
        }
    }

}