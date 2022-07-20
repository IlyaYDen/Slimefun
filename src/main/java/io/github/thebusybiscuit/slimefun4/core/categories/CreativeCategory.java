package io.github.thebusybiscuit.slimefun4.core.categories;

import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.time.LocalDate;

public class CreativeCategory extends Category {
    public CreativeCategory(NamespacedKey key, ItemStack item) {
        super(key, item);
    }
    @Override
    public boolean isHidden(@Nonnull Player p) {
            return true;
    }
}
