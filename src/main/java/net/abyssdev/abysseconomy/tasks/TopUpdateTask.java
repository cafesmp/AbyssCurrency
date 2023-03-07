package net.abyssdev.abysseconomy.tasks;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysslib.runnable.AbyssRunnable;

/**
 * The top update task
 * @author Relocation
 */
public final class TopUpdateTask extends AbyssRunnable {

    private final Currency currency;

    /**
     * Constructs a new TopUpdateTask
     * @param plugin The abyss economy plugin
     * @param currency The currency
     * @param time The task period
     */
    public TopUpdateTask(final AbyssEconomy plugin, final Currency currency, final long time) {
        this.currency = currency;
        this.runTaskTimer(plugin, 0L, time);
    }

    @Override
    public void run() {
        this.currency.getTopMenu().recalculate();
    }
}