package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Freezer extends AContainer implements RecipeDisplayItem {

    public Freezer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        //super(category, item, recipeType, recipe);
        super(category, item, recipeType, recipe,44, 99, new int[]{ 19,20 }, new int[]{25, 24}, 270, "pickaxe");

    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(2, new ItemStack[] { new ItemStack(Material.WATER_BUCKET) }, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.ICE) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) }, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.OBSIDIAN) });
        registerRecipe(4, new ItemStack[] { new ItemStack(Material.ICE) }, new ItemStack[] { new ItemStack(Material.PACKED_ICE) });
        registerRecipe(6, new ItemStack[] { new ItemStack(Material.PACKED_ICE) }, new ItemStack[] { new ItemStack(Material.BLUE_ICE) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.BLUE_ICE) }, new ItemStack[] { SlimefunItems.REACTOR_COOLANT_CELL });
        registerRecipe(6, new ItemStack[] { new ItemStack(Material.SNOW_BLOCK, 2) }, new ItemStack[] { new ItemStack(Material.ICE) });
        registerRecipe(6, new ItemStack[] { new ItemStack(Material.MAGMA_CREAM) }, new ItemStack[] { new ItemStack(Material.SLIME_BALL) });
        registerRecipe(6, new ItemStack[] { new ItemStack(Material.MAGMA_BLOCK, 2) }, new ItemStack[] { new ItemStack(Material.SLIME_BLOCK) });
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);

        for (MachineRecipe recipe : recipes) {
            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(recipe.getOutput()[recipe.getOutput().length - 1]);
        }

        return displayRecipes;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Override
    public String getMachineIdentifier() {
        return "FREEZER";
    }

}
