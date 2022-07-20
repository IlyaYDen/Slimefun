package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

public enum MachineTier {

    BASIC("&eБазовый"),
    AVERAGE("&6Простой"),
    MEDIUM("&aСредний"),
    GOOD("&2Хороший"),
    ADVANCED("&6Продвинутый"),
    END_GAME("&4лучший");

    private final String prefix;

    MachineTier(@Nonnull String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return prefix;
    }

}
