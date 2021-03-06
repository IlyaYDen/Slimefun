package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A quick and easy implementation of {@link SlimefunItem} that also implements the
 * interface {@link WitherProof}.
 * Items created with this class cannot be destroyed by a {@link Wither}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see WitherProof
 *
 */
public class WitherProofBlock extends SlimefunItem implements WitherProof {

    @ParametersAreNonnullByDefault
    public WitherProofBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @ParametersAreNonnullByDefault
    public WitherProofBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @ParametersAreNonnullByDefault
    public WitherProofBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,int t, String st) {
        super(category, item, recipeType, recipe,t,st);
    }

    @ParametersAreNonnullByDefault
    public WitherProofBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput,int t, String st) {
        super(category, item, recipeType, recipe, recipeOutput, t,  st);
    }

    @Override
    public void onAttack(Block block, Wither wither) {
        // In this implementation we simply do nothing.
    }

}
