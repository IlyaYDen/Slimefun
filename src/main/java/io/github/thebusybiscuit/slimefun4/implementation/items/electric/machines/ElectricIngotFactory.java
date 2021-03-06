package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ElectricIngotFactory extends AContainer implements RecipeDisplayItem {

    public ElectricIngotFactory(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int slot, int cmd, int[] in, int[] out,int t,String st) {
        super(category, item, recipeType, recipe, slot, cmd, in, out,t,st);

    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_INGOT_FACTORY";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

}
