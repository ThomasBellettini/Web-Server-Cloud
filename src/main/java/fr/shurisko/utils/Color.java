package fr.shurisko.utils;

public class Color {
    public static String getRGB(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
