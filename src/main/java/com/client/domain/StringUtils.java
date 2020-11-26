package com.client.domain;

public class StringUtils {
    public static boolean isNotEmpty(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isMoreThanNull(String name) {
        if (name.matches("[0-9]+")) return Integer.parseInt(name) > 0;
        return false;
    }
}