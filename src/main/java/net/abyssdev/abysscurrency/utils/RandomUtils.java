package net.abyssdev.abysscurrency.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for storing utils relating to randomization
 * @author Relocation
 */
@UtilityClass
public final class RandomUtils {

    public double minMax(final double min, final double max) {
        if (min == max) {
            return min;
        }

        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public int minMax(final int min, final int max) {
        if (min == max) {
            return min;
        }

        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public long minMax(final long min, final long max) {
        if (min == max) {
            return min;
        }

        return ThreadLocalRandom.current().nextLong(min, max);
    }
}