package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public class ElectricOreGrinder extends AContainer implements RecipeDisplayItem, NotHopperable {

    @ParametersAreNonnullByDefault
    public ElectricOreGrinder(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int slot, int cmd, int[] in, int[] out,int t,String st) {
        super(category, item, recipeType, recipe, slot, cmd, in, out,t,st);
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_ORE_GRINDER";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

}
