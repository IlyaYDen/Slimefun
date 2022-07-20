package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreWasher;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@link ElectricDustWasher} serves as an electrical {@link OreWasher}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see OreWasher
 *
 */
public class ElectricDustWasher extends AContainer implements RecipeDisplayItem {

    private final OreWasher oreWasher = SlimefunItems.ORE_WASHER.getItem(OreWasher.class);
    private final boolean legacyMode;

    @ParametersAreNonnullByDefault
    public ElectricDustWasher(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int slot, int cmd, int[] in, int[] out,int t,String st) {
        super(category, item, recipeType, recipe, slot, cmd, in, out,t,st);
        //super(category, item, recipeType, recipe);

        legacyMode = SlimefunPlugin.getCfg().getBoolean("options.legacy-dust-washer");
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), SlimefunItems.SIFTED_ORE, true, false)) {
                if (!legacyMode && !hasFreeSlot(menu)) {
                    return null;
                }

                ItemStack dust = oreWasher.getRandomDust();
                MachineRecipe recipe = new MachineRecipe(5 / getSpeed(), new ItemStack[] { SlimefunItems.SIFTED_ORE }, new ItemStack[] { dust });

                if (!legacyMode || menu.fits(recipe.getOutput()[0], getOutputSlots())) {
                    menu.consumeItem(slot);
                    return recipe;
                }
            } else if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), SlimefunItems.PULVERIZED_ORE, true)) {
                MachineRecipe recipe = new MachineRecipe(4 / getSpeed(), new ItemStack[] { SlimefunItems.PULVERIZED_ORE }, new ItemStack[] { SlimefunItems.PURE_ORE_CLUSTER });

                if (menu.fits(recipe.getOutput()[0], getOutputSlots())) {
                    menu.consumeItem(slot);
                    return recipe;
                }
            }
        }

        return null;
    }

    //@Override
    //public List<ItemStack> getDisplayRecipes() {
    //    return recipes.stream().map(items -> items).collect(Collectors.toList());
    //}

    private final OreWasher OreWasher = SlimefunItems.ORE_WASHER.getItem(OreWasher.class);
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> recipes = new ArrayList<>();

        //recipes.addAll(OreWasher.getDisplayRecipes());
        ItemStack[] dusts =  { SlimefunItems.IRON_DUST, SlimefunItems.GOLD_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.TIN_DUST, SlimefunItems.ZINC_DUST, SlimefunItems.ALUMINUM_DUST, SlimefunItems.MAGNESIUM_DUST, SlimefunItems.LEAD_DUST, SlimefunItems.SILVER_DUST };
        for(ItemStack item : dusts){
            recipes.add(SlimefunItems.SIFTED_ORE);
            recipes.add(item);
        }

        return recipes;
    }

    private boolean hasFreeSlot(BlockMenu menu) {
        for (int slot : getOutputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_DUST_WASHER";
    }

}
