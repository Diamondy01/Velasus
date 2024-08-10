package me.diamondy.velasus.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TextColor {
    public static void main(String[] args) {
        Component component = Component.text("Hello, ")
                .append(Component.text("world", NamedTextColor.GREEN))
                .append(Component.text("!", NamedTextColor.RED));
        System.out.println(component);
    }
}