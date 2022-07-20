package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HeatedPressureChamber extends AContainer implements RecipeDisplayItem {

    private List<ItemStack> recipes;

    public HeatedPressureChamber(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        //super(category, item, recipeType, recipe); edited
        super(category, item, recipeType, recipe,44, 99, new int[]{ 19,20 }, new int[]{25, 24}, 270, "pickaxe");

        new BlockMenuPreset(getId(), getItemName()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return getOutputSlots();
                }

                List<Integer> slots = new ArrayList<>();

                for (int slot : getInputSlots()) {
                    if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), item, true)) {
                        slots.add(slot);
                    }
                }

                if (slots.isEmpty()) {
                    return getInputSlots();
                } else {
                    Collections.sort(slots, compareSlots(menu));
                    int[] array = new int[slots.size()];

                    for (int i = 0; i < slots.size(); i++) {
                        array[i] = slots.get(i);
                    }

                    return array;
                }
            }
        };
    }

    private Comparator<Integer> compareSlots(DirtyChestMenu menu) {
        return Comparator.comparingInt(slot -> menu.getItemInSlot(slot).getAmount());
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(45, new ItemStack[] { SlimefunItems.OIL_BUCKET }, new ItemStack[] { new CustomItem(SlimefunItems.PLASTIC_SHEET, 8) });
        registerRecipe(30, new ItemStack[] { SlimefunItems.GOLD_24K, SlimefunItems.URANIUM }, new ItemStack[] { SlimefunItems.BLISTERING_INGOT });
        registerRecipe(30, new ItemStack[] { SlimefunItems.BLISTERING_INGOT, SlimefunItems.CARBONADO }, new ItemStack[] { SlimefunItems.BLISTERING_INGOT_2 });
        registerRecipe(60, new ItemStack[] { SlimefunItems.BLISTERING_INGOT_2, new ItemStack(Material.NETHER_STAR) }, new ItemStack[] { SlimefunItems.BLISTERING_INGOT_3 });
        registerRecipe(90, new ItemStack[] { SlimefunItems.PLUTONIUM, SlimefunItems.URANIUM }, new ItemStack[] { SlimefunItems.BOOSTED_URANIUM });
        registerRecipe(60, new ItemStack[] { SlimefunItems.NETHER_ICE, SlimefunItems.PLUTONIUM }, new ItemStack[] { new CustomItem(SlimefunItems.ENRICHED_NETHER_ICE, 4) });
        registerRecipe(45, new ItemStack[] { SlimefunItems.ENRICHED_NETHER_ICE }, new ItemStack[] { new CustomItem(SlimefunItems.NETHER_ICE_COOLANT_CELL, 8) });
        registerRecipe(8, new ItemStack[] { SlimefunItems.MAGNESIUM_DUST, SlimefunItems.SALT }, new ItemStack[] { SlimefunItems.MAGNESIUM_SALT });
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Override
    public String getMachineIdentifier() {
        return "HEATED_PRESSURE_CHAMBER";
    }



    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> recipes = new ArrayList<>();
        recipes.add(SlimefunItems.OIL_BUCKET);
        recipes.add(new CustomItem(SlimefunItems.PLASTIC_SHEET, 8));

        recipes.add(new CustomItem(Material.ITEM_FRAME,"Мульти-Рецепт", "&fЗолотой Слиток &7(24-Карата)","&4Уран"));
        recipes.add(SlimefunItems.BLISTERING_INGOT);

        recipes.add(new CustomItem(Material.ITEM_FRAME,"Мульти-Рецепт", "&6Раскалённый Cлиток &7(33%)","&b&lКарбонадо"));
        recipes.add(SlimefunItems.BLISTERING_INGOT_2);

        recipes.add(new CustomItem(Material.ITEM_FRAME,"Мульти-Рецепт", SlimefunItems.BLISTERING_INGOT_2.getDisplayName(),"&eЗвезда Незера"));
        recipes.add(SlimefunItems.BLISTERING_INGOT_3);

        recipes.add(new CustomItem(Material.ITEM_FRAME,"Мульти-Рецепт", SlimefunItems.PLUTONIUM.getDisplayName(),SlimefunItems.URANIUM.getDisplayName()));
        recipes.add(SlimefunItems.BOOSTED_URANIUM);

        recipes.add(new CustomItem(Material.ITEM_FRAME,"Мульти-Рецепт", SlimefunItems.NETHER_ICE.getDisplayName(),SlimefunItems.PLUTONIUM.getDisplayName()));
        recipes.add(SlimefunItems.ENRICHED_NETHER_ICE);

        recipes.add(SlimefunItems.ENRICHED_NETHER_ICE);
        recipes.add(new CustomItem(SlimefunItems.NETHER_ICE_COOLANT_CELL, 8) );

        recipes.add(new CustomItem(Material.ITEM_FRAME,"Мульти-Рецепт", SlimefunItems.MAGNESIUM_DUST.getDisplayName(),SlimefunItems.SALT.getDisplayName()));
        recipes.add(SlimefunItems.MAGNESIUM_SALT);

        return recipes;
    }
}
