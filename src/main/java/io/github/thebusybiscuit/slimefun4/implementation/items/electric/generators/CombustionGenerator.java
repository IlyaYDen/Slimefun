package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class CombustionGenerator extends AGenerator {

    @ParametersAreNonnullByDefault
    public CombustionGenerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        //super(category, item, recipeType, recipe);
        super(category, item, recipeType, recipe,44, 99, new int[]{ 19,20 }, new int[]{25, 24}, 270, "pickaxe");
    }

    @Override
    protected void registerDefaultFuelTypes() {
        registerFuel(new MachineFuel(30, SlimefunItems.OIL_BUCKET));
        registerFuel(new MachineFuel(90, SlimefunItems.FUEL_BUCKET));
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

}
