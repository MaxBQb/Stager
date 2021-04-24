package main.stager.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public final class Utilits {

    public static boolean isNullOrEmpty(Collection<?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrEmpty(Map<?, ?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isBlank(@NotNull String value) {
        return value.trim().isEmpty();
    }

    public static boolean isNullOrBlank(String value) {
        return value == null || isBlank(value);
    }

    public static <T> T getDefaultOnNull(T original, T defaultValue) {
        return original == null ? defaultValue : original;
    }

    public static String getDefaultOnNullOrEmpty(String original, String defaultValue) {
        return isNullOrEmpty(original) ? defaultValue : original;
    }

    public static String getDefaultOnNullOrBlank(String original, String defaultValue) {
        return isNullOrBlank(original) ? defaultValue : original;
    }
}
