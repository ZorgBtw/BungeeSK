package fr.zorg.bungeesk.common.utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Utils {

    public static String randomString(final int length) {
        final String[] dataSet = {
                "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r",
                "s", "t", "u", "v", "w", "x", "y", "z", "0",
                "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "!", "§", "&", "$", "/", ":", ";", ",", "*"
        };
        final StringBuilder builder = new StringBuilder();
        for (int loop = 0; loop < length; loop++) {
            final String value = dataSet[new Random().nextInt(dataSet.length)];
            if (Math.random() > 0.5)
                builder.append(value.toUpperCase());
            else
                builder.append(value);
        }
        return builder.toString();
    }

    public static <T> List<T> copy(final List<T> original) {
        final List<T> result = new ArrayList<>();
        for (final T item : original)
            result.add(item);
        return result;
    }

    public static String toBase64(final byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

}
