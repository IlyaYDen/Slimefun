package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Refinery extends AContainer implements RecipeDisplayItem {

    public Refinery(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        //super(category, item, recipeType, recipe);
        super(category, item, recipeType, recipe,44, 99, new int[]{ 19,20 }, new int[]{25, 24}, 270, "pickaxe");
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(40, SlimefunItems.OIL_BUCKET, SlimefunItems.FUEL_BUCKET);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Override
    public String getMachineIdentifier() {
        return "REFINERY";
    }

}
