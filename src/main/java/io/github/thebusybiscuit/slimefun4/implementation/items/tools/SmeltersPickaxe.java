package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link SmeltersPickaxe} automatically smelts any ore you mine.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SmeltersPickaxe extends SimpleSlimefunItem<ToolUseHandler> implements DamageableItem {

    @ParametersAreNonnullByDefault
    public SmeltersPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Block b = e.getBlock();

            if (SlimefunTag.SMELTERS_PICKAXE_BLOCKS.isTagged(b.getType()) && !BlockStorage.hasBlockInfo(b)) {
                Collection<ItemStack> blockDrops = b.getDrops(tool);

                for (ItemStack drop : blockDrops) {
                    if (drop != null && !drop.getType().isAir()) {
                        smelt(b, drop, fortune);
                        drops.add(drop);
                    }
                }

                damageItem(e.getPlayer(), tool);
            }
            else if(!BlockStorage.hasBlockInfo(b) &&(
                            b.getType().equals(Material.QUARTZ_SLAB)|       // ALUMINIUM_ORE
                            b.getType().equals(Material.STONE_BRICK_SLAB)|  // SILVER_ORE
                            b.getType().equals(Material.STONE_SLAB)|        // NICKEL_ORE
                            b.getType().equals(Material.SANDSTONE_SLAB)|    // TIN_ORE
                                    b.getType().equals(Material.BRICK_SLAB)|    // LEAD_ORE
                            b.getType().equals(Material.NETHER_BRICK_SLAB))) {

                Slab block = (Slab) b.getBlockData();
                if (block.getType().equals(Slab.Type.DOUBLE)) {

                    switch (b.getType()) {
                        case QUARTZ_SLAB:
                            //drops.add(SlimefunItems.ALUMINUM_INGOT);
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),SlimefunItems.ALUMINUM_INGOT);
                            damageItem(e.getPlayer(), tool);
                            break;
                        case STONE_BRICK_SLAB:
                            //drops.add(SlimefunItems.SILVER_INGOT);
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),SlimefunItems.SILVER_INGOT);

                            damageItem(e.getPlayer(), tool);
                            break;
                        case STONE_SLAB:
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),SlimefunItems.LEAD_ORE);
                            //drops.add(SlimefunItems.NICKEL_INGOT);
                            damageItem(e.getPlayer(), tool);
                            break;
                        case BRICK_SLAB:
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),SlimefunItems.NICKEL_INGOT);
                            //drops.add(SlimefunItems.NICKEL_INGOT);
                            damageItem(e.getPlayer(), tool);
                            break;
                        case SANDSTONE_SLAB:
                            e.getPlayer().getWorld().dropItemNaturally(b.getLocation(),SlimefunItems.TIN_INGOT);
                            //drops.add(SlimefunItems.TIN_INGOT);
                            damageItem(e.getPlayer(), tool);
                            break;
                    }
                }
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void smelt(Block b, ItemStack drop, int fortune) {
        Optional<ItemStack> furnaceOutput = SlimefunPlugin.getMinecraftRecipeService().getFurnaceOutput(drop);

        if (furnaceOutput.isPresent()) {
            b.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            drop.setType(furnaceOutput.get().getType());
        }

        // Fixes #3116
        drop.setAmount(fortune);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

}
