package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link ElectricFurnace} is an electric version of the {@link Furnace}.
 * As the name would probably suggest.
 * 
 * @author TheBusyBiscuit
 *
 */
public class ElectricFurnace extends AContainer implements NotHopperable {

    @ParametersAreNonnullByDefault
    public ElectricFurnace(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }
    @ParametersAreNonnullByDefault
    public ElectricFurnace(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int slot, int cmd, int[] in, int[] out,int t,String st) {
        super(category, item, recipeType, recipe, slot, cmd, in, out,t,st);

    }

    @Override
    public void registerDefaultRecipes() {
        SlimefunPlugin.getMinecraftRecipeService().subscribe(snapshot -> {
            for (FurnaceRecipe recipe : snapshot.getRecipes(FurnaceRecipe.class)) {
                RecipeChoice choice = recipe.getInputChoice();

                if (choice instanceof MaterialChoice) {
                    for (Material input : ((MaterialChoice) choice).getChoices()) {
                        registerRecipe(5, new ItemStack[] { new ItemStack(input) }, new ItemStack[] { recipe.getResult() });
                    }

                    registerRecipe(5, new ItemStack[] {SlimefunItems.LEAD_ORE}, new ItemStack[] { SlimefunItems.LEAD_INGOT });
                    registerRecipe(5, new ItemStack[] {SlimefunItems.TIN_ORE}, new ItemStack[] { SlimefunItems.TIN_INGOT });
                    registerRecipe(5, new ItemStack[] {SlimefunItems.ALUMINIUM_ORE}, new ItemStack[] { SlimefunItems.ALUMINIUM_ORE });
                    registerRecipe(5, new ItemStack[] {SlimefunItems.NICKEL_ORE}, new ItemStack[] { SlimefunItems.NICKEL_INGOT });
                    registerRecipe(5, new ItemStack[] {SlimefunItems.SILVER_ORE}, new ItemStack[] { SlimefunItems.SILVER_INGOT });
                }
            }
        });
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_FURNACE";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

}
