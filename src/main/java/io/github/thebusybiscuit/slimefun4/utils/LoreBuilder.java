package io.github.thebusybiscuit.slimefun4.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This utility class provides a few handy methods and constants to build the lore of any
 * {@link SlimefunItemStack}. It is mostly used directly inside the class {@link SlimefunItems}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunItems
 *
 */
public final class LoreBuilder {

    public static final String HAZMAT_SUIT_REQUIRED = "&8\u21E8 &4Требуется защитный костюм!";

    public static final String RIGHT_CLICK_TO_USE = "&eПравый Клик&7, чтобы использовать";
    public static final String RIGHT_CLICK_TO_OPEN = "&eПравый Клик&7, чтобы открыть";
    public static final String CROUCH_TO_USE = "&eShift&7 чтобы использовать";

    private static final DecimalFormat hungerFormat = new DecimalFormat("#.0", DecimalFormatSymbols.getInstance(Locale.ROOT));

    private LoreBuilder() {}

    @Nonnull
    public static String radioactive(@Nonnull Radioactivity radioactivity) {
        return radioactivity.getLore();
    }

    @Nonnull
    public static String machine(@Nonnull MachineTier tier, @Nonnull MachineType type) {
        return tier + " " + type;
    }

    @Nonnull
    public static String speed(float speed) {
        return "&8\u21E8 &b\u26A1 &7Скорость: &b" + speed + 'x';
    }

    @Nonnull
    public static String powerBuffer(int power) {
        return power(power, " Энергии");
    }

    @Nonnull
    public static String powerPerSecond(int power) {
        return power(power, " Дж/с");
    }

    @Nonnull
    //public static String power(int power, @Nonnull String suffix) { return "&8\u21E8 &e\u26A1 &7" + power + " J" + suffix; }

    public static String power(int power, String suffix) {
        return "&8\u21E8 &e\u26A1 &7" + power /*+ " Дж"*/ + suffix;
    }

    @Nonnull
    public static String powerCharged(int charge, int capacity) {
        return "&8\u21E8 &e\u26A1 &7" + charge + " / " + capacity+ " Дж";
        //return "&8\u21E8 &e\u26A1 &7" + charge + " / " + capacity + " J";
    }

    @Nonnull
    public static String material(@Nonnull String material) {
        return "&8\u21E8 &77Материал: &b" + material;
    }

    @Nonnull
    public static String hunger(double value) {
        return "&7&oRestores &b&o" + hungerFormat.format(value) + " &7&oГолода";
    }

    @Nonnull
    public static String range(int blocks) {
        return "&7Диапазон: &c" + blocks + " блоков";
    }

}
