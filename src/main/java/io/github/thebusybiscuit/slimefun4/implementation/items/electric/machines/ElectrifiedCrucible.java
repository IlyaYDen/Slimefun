package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

import java.util.ArrayList;
import java.util.List;

public class ElectrifiedCrucible extends AContainer implements RecipeDisplayItem {

    public ElectrifiedCrucible(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        //super(category, item, recipeType, recipe); edited
        super(category, item, recipeType, recipe,44, 99, new int[]{ 19,20 }, new int[]{25, 24}, 270, "pickaxe");
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.COBBLESTONE, 16) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.TERRACOTTA, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.OBSIDIAN) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });

        for (Material terracotta : SlimefunTag.TERRACOTTA.getValues()) {
            registerRecipe(8, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(terracotta, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        }

        for (Material leaves : Tag.LEAVES.getValues()) {
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(leaves, 16) }, new ItemStack[] { new ItemStack(Material.WATER_BUCKET) });
        }

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.BLACKSTONE, 8) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.BASALT, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        }

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.COBBLED_DEEPSLATE, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.DEEPSLATE, 10) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.TUFF, 8) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        }
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> recipes = new ArrayList<>();
        recipes.add(new ItemStack(Material.COBBLESTONE, 16));
        recipes.add(new ItemStack(Material.LAVA_BUCKET));

        recipes.add(new ItemStack(Material.TERRACOTTA, 12));
        recipes.add(new ItemStack(Material.LAVA_BUCKET));

        recipes.add(new ItemStack(Material.OBSIDIAN));
        recipes.add(new ItemStack(Material.LAVA_BUCKET));

        for (Material terracotta : SlimefunTag.TERRACOTTA.getValues()) {

            recipes.add(new ItemStack(terracotta, 12));
            recipes.add(new ItemStack(Material.LAVA_BUCKET));
        }

        for (Material leaves : Tag.LEAVES.getValues()) {

            recipes.add(new ItemStack(leaves, 16));
            recipes.add(new ItemStack(Material.WATER_BUCKET));
            }


        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            recipes.add(new ItemStack(Material.COBBLED_DEEPSLATE, 12));
            recipes.add(new ItemStack(Material.LAVA_BUCKET));

            recipes.add(new ItemStack(Material.DEEPSLATE, 10));
            recipes.add(new ItemStack(Material.LAVA_BUCKET));

            recipes.add(new ItemStack(Material.TUFF, 8));
            recipes.add(new ItemStack(Material.LAVA_BUCKET));
        }
        return recipes;
    }
    @Override
    public String getMachineIdentifier() {
        return "ELECTRIFIED_CRUCIBLE";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

}
