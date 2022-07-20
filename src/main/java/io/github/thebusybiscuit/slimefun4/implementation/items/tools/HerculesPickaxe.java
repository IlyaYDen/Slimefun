package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import javax.annotation.ParametersAreNonnullByDefault;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HerculesPickaxe extends SimpleSlimefunItem<ToolUseHandler> {

    @ParametersAreNonnullByDefault
    public HerculesPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Block b = e.getBlock();
            if (SlimefunTag.ORES.isTagged(e.getBlock().getType())) {
                if (e.getBlock().getType() == Material.IRON_ORE) {
                    drops.add(new CustomItem(SlimefunItems.IRON_DUST, 2));
                } else if (e.getBlock().getType() == Material.GOLD_ORE) {
                    drops.add(new CustomItem(SlimefunItems.GOLD_DUST, 2));
                } else if (e.getBlock().getType() == Material.COPPER_ORE |e.getBlock().getType() == Material.DEEPSLATE_COPPER_ORE) {
                    drops.add(new CustomItem(SlimefunItems.COPPER_DUST, 2));
                } else {
                    for (ItemStack drop : e.getBlock().getDrops(tool)) {
                        drops.add(new CustomItem(drop, drop.getAmount() * 2));
                    }
                }
            }else if(!BlockStorage.hasBlockInfo(b) && b.getType().equals(Material.COPPER_ORE)) {
                drops.add(new CustomItem(SlimefunItems.COPPER_DUST,2));
                //e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),new CustomItem(SlimefunItems.COPPER_DUST,2));
            }
            else if(!BlockStorage.hasBlockInfo(b) &&(
                    b.getType().equals(Material.QUARTZ_SLAB)|       // ALUMINIUM_ORE
                            b.getType().equals(Material.STONE_BRICK_SLAB)|  // SILVER_ORE
                            b.getType().equals(Material.STONE_SLAB)|        // NICKEL_ORE
                            b.getType().equals(Material.SANDSTONE_SLAB)|    // TIN_ORE
                            b.getType().equals(Material.NETHER_BRICK_SLAB))) {

                Slab block = (Slab) b.getBlockData();
                if (block.getType().equals(Slab.Type.DOUBLE))
                    switch (b.getType()){
                        case QUARTZ_SLAB:
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),new CustomItem(SlimefunItems.ALUMINUM_DUST,2));
                            break;
                        case STONE_BRICK_SLAB:
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),new CustomItem(SlimefunItems.SILVER_DUST,2));

                            break;
                        case SANDSTONE_SLAB:
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),new CustomItem(SlimefunItems.TIN_DUST,2));

                            break;
                    }
            }

        };
    }

}
