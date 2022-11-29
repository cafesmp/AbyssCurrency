package net.abyssdev.abysseconomy.tasks;

import net.abyssdev.abysseconomy.AbyssEconomy;
import net.abyssdev.abysseconomy.currency.Currency;
import net.abyssdev.abysseconomy.player.CurrencyPlayer;
import net.abyssdev.abysseconomy.utils.format.FormatUtil;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.runnable.AbyssTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

/**
 * The gained task sends a message to players showing them the amount earned from each currency
 * in the last 60 seconds
 *
 * @author Relocation
 */
public final class GainedTask extends AbyssTask<AbyssEconomy> {

    /**
     * Constructs a new GainedTask
     * @param plugin The abyss economy plugin
     */
    public GainedTask(final AbyssEconomy plugin) {
        super(plugin, 1200, true);
    }

    @Override
    public void run() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final CurrencyPlayer profile = this.plugin.getPlayerStorage().get(player.getUniqueId());

            if (profile.getLastSixtySeconds().isEmpty()) {
                continue;
            }

            final PlaceholderReplacer replacer = new PlaceholderReplacer();

            for (final Currency currency : this.plugin.getCurrencyRegistry().values()) {
                replacer.addPlaceholder("%" + currency.getName() + "%", "0");
            }

            for (final Map.Entry<String, Double> entry : profile.getLastSixtySeconds().entrySet()) {
                final Optional<Currency> currencyOptional = this.plugin.getCurrencyRegistry().get(entry.getKey());

                if (!currencyOptional.isPresent()) {
                    continue;
                }

                final Currency currency = currencyOptional.get();

                replacer.addPlaceholder("%" + currency.getName() + "%", FormatUtil.format(currency, entry.getValue()));
            }

            this.plugin.getMessageCache().sendMessage(player, "messages.gained", replacer);
            profile.getLastSixtySeconds().clear();
        }
    }

}