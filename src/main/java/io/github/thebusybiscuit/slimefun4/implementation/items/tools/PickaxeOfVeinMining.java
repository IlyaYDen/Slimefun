package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link PickaxeOfVeinMining} is a powerful tool which allows you to mine an entire vein of ores
 * at once. It even works with the fortune {@link Enchantment}.
 * 
 * @author TheBusyBiscuit
 * @author Linox
 *
 */
public class PickaxeOfVeinMining extends SimpleSlimefunItem<ToolUseHandler> {

    private final ItemSetting<Integer> maxBlocks = new IntRangeSetting(this, "max-blocks", 1, 16, Integer.MAX_VALUE);

    @ParametersAreNonnullByDefault
    public PickaxeOfVeinMining(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        addItemSetting(maxBlocks);
    }

    @Override
    public ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Block bl = e.getBlock();
            if (SlimefunTag.PICKAXE_OF_VEIN_MINING_BLOCKS.isTagged(e.getBlock().getType())) {
                List<Block> blocks = Vein.find(e.getBlock(), maxBlocks.getValue(), b -> SlimefunTag.PICKAXE_OF_VEIN_MINING_BLOCKS.isTagged(b.getType()));
                breakBlocks(e.getPlayer(), blocks, fortune, tool);
            }
            else if(!BlockStorage.hasBlockInfo(bl)) {
                if (bl.getType().equals(Material.QUARTZ_SLAB)){
                    // ALUMINIUM_ORE
                    List<Block> blocks = Vein.find(e.getBlock(), maxBlocks.getValue(), b -> b.getType().equals(Material.QUARTZ_SLAB));
                    breakBlocks(e.getPlayer(), blocks, fortune, tool);
                }
                else if (bl.getType().equals(Material.STONE_BRICK_SLAB)){
                    List<Block> blocks = Vein.find(e.getBlock(), maxBlocks.getValue(), b -> b.getType().equals(Material.STONE_BRICK_SLAB));
                    breakBlocks(e.getPlayer(), blocks, fortune, tool);
                    // SILVER_ORE
                }
                else if (bl.getType().equals(Material.BRICK_SLAB)){
                    List<Block> blocks = Vein.find(e.getBlock(), maxBlocks.getValue(), b -> b.getType().equals(Material.BRICK_SLAB));
                    breakBlocks(e.getPlayer(), blocks, fortune, tool);
                    //
                }
                else if (bl.getType().equals(Material.STONE_SLAB)){
                    List<Block> blocks = Vein.find(e.getBlock(), maxBlocks.getValue(), b -> b.getType().equals(Material.STONE_SLAB));
                    breakBlocks(e.getPlayer(), blocks, fortune, tool);
                    // NICKEL_ORE
                }
                else if (bl.getType().equals(Material.SANDSTONE_SLAB)){
                    List<Block> blocks = Vein.find(e.getBlock(), maxBlocks.getValue(), b -> b.getType().equals(Material.SANDSTONE_SLAB));
                    breakBlocks(e.getPlayer(), blocks, fortune, tool);
                    // TIN_ORE
                }
                else if (bl.getType().equals(Material.NETHER_BRICK_SLAB)) {
                    List<Block> blocks = Vein.find(e.getBlock(), maxBlocks.getValue(), b -> b.getType().equals(Material.NETHER_BRICK_SLAB));
                    breakBlocks(e.getPlayer(), blocks, fortune, tool);

                }
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void breakBlocks(Player p, List<Block> blocks, int fortune, ItemStack tool) {
        for (Block b : blocks) {
            if (SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
                b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

                if (tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
                    ItemStack i = new ItemStack(b.getType());
                    if(!BlockStorage.hasBlockInfo(b) &&(
                            b.getType().equals(Material.QUARTZ_SLAB)|       // ALUMINIUM_ORE
                                    b.getType().equals(Material.STONE_BRICK_SLAB)|  // SILVER_ORE
                                    b.getType().equals(Material.STONE_SLAB)|        // NICKEL_ORE
                                    b.getType().equals(Material.SANDSTONE_SLAB)|    // TIN_ORE
                                    b.getType().equals(Material.NETHER_BRICK_SLAB))) {
                        Slab block = (Slab) b.getBlockData();
                        if (block.getType().equals(Slab.Type.DOUBLE))
                            switch (b.getType()){
                                case QUARTZ_SLAB:
                                    i = new CustomItem(SlimefunItems.ALUMINIUM_ORE,1);
                                    break;
                                case STONE_BRICK_SLAB:
                                    i = new CustomItem(SlimefunItems.SILVER_ORE,1);
                                    break;
                                case STONE_SLAB:
                                    i = new CustomItem(SlimefunItems.NICKEL_ORE,1);
                                    break;
                                case SANDSTONE_SLAB:
                                    i = new CustomItem(SlimefunItems.TIN_ORE,1);
                                    break;

                            }

                    }
                    b.getWorld().dropItemNaturally(b.getLocation(), i);
                } else {

                    ItemStack i = new ItemStack(b.getType());
                    if(!BlockStorage.hasBlockInfo(b) &&(
                            b.getType().equals(Material.QUARTZ_SLAB)|       // ALUMINIUM_ORE
                                    b.getType().equals(Material.STONE_BRICK_SLAB)|  // SILVER_ORE
                                    b.getType().equals(Material.STONE_SLAB)|        // NICKEL_ORE
                                    b.getType().equals(Material.SANDSTONE_SLAB)|    // TIN_ORE
                                    b.getType().equals(Material.BRICK_SLAB)|    // LEAD_ORE
                                    b.getType().equals(Material.NETHER_BRICK_SLAB))) {
                        Slab block = (Slab) b.getBlockData();
                        if (block.getType().equals(Slab.Type.DOUBLE))
                            switch (b.getType()){
                                case QUARTZ_SLAB:
                                    i = new CustomItem(SlimefunItems.ALUMINIUM_ORE,1);
                                    break;
                                case STONE_BRICK_SLAB:
                                    i = new CustomItem(SlimefunItems.SILVER_ORE,1);
                                    break;
                                case STONE_SLAB:
                                    i = new CustomItem(SlimefunItems.NICKEL_ORE,1);
                                    break;
                                case SANDSTONE_SLAB:
                                    i = new CustomItem(SlimefunItems.TIN_ORE,1);
                                    break;
                                case BRICK_SLAB:
                                    i = new CustomItem(SlimefunItems.LEAD_ORE,1);
                                    break;
                            }

                        b.getWorld().dropItemNaturally(b.getLocation(), i);
                    } else
                    for (ItemStack drop : b.getDrops(tool)) {
                        b.getWorld().dropItemNaturally(b.getLocation(), drop.getType().isBlock() ? drop : new CustomItem(drop, fortune));
                    }
                }

                b.setType(Material.AIR);
            }
        }
    }

}
