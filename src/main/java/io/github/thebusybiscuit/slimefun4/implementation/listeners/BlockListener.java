package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import net.minecraft.core.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Slab;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

/**
 * The {@link BlockListener} is responsible for listening to the {@link BlockPlaceEvent}
 * and {@link BlockBreakEvent}.
 *
 * @author TheBusyBiscuit
 * @author Linox
 * @author Patbox
 *
 * @see BlockPlaceHandler
 * @see BlockBreakHandler
 * @see ToolUseHandler
 *
 */
public class BlockListener implements Listener {

    public BlockListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlaceExisting(BlockPlaceEvent e) {
        Block block = e.getBlock();

        // Fixes #2636 - This will solve the "ghost blocks" issue
        if (e.getBlockReplacedState().getType().isAir()) {
            SlimefunItem sfItem = BlockStorage.check(block);

            if (sfItem != null && !SlimefunPlugin.getTickerTask().isDeletedSoon(block.getLocation())) {
                for (ItemStack item : sfItem.getDrops()) {
                    if (item != null && !item.getType().isAir()) {
                        block.getWorld().dropItemNaturally(block.getLocation(), item);
                    }
                }

                BlockStorage.clearBlockInfo(block);
                List<Entity> entityes = e.getBlock().getLocation().getWorld().getEntities();
                for (Entity ent : entityes) {

                    //e.getPlayer().sendMessage(String.valueOf(ent));

                    if (ent.getLocation().getX() - 0.5 == e.getBlock().getLocation().getX() &&
                            ent.getLocation().getY() == e.getBlock().getLocation().getY() &&
                            ent.getLocation().getZ() - 0.5 == e.getBlock().getLocation().getZ())
                        ent.remove();
                }
            }
        } else if (BlockStorage.hasBlockInfo(e.getBlock())) {
            // If there is no air (e.g. grass) then don't let the block be placed
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null && !(sfItem instanceof NotPlaceable)) {
            if (!sfItem.canUse(e.getPlayer(), true)) {
                e.setCancelled(true);
            } else {
                if (SlimefunPlugin.getBlockDataService().isTileEntity(e.getBlock().getType())) {
                    SlimefunPlugin.getBlockDataService().setBlockData(e.getBlock(), sfItem.getId());
                }

                BlockStorage.addBlockInfo(e.getBlock(), "id", sfItem.getId(), true);
                sfItem.callItemHandler(BlockPlaceHandler.class, handler -> handler.onPlayerPlace(e));

                if (
                        SlimefunUtils.isItemSimilar(sfItem.getItem(), SlimefunItems.ALUMINIUM_ORE, false) |
                                //SlimefunUtils.isItemSimilar(sfItem.getItem(),SlimefunItems.COPPER_ORE,false)|
                                SlimefunUtils.isItemSimilar(sfItem.getItem(), SlimefunItems.LEAD_ORE, false) |
                                SlimefunUtils.isItemSimilar(sfItem.getItem(), SlimefunItems.NICKEL_ORE, false) |
                                SlimefunUtils.isItemSimilar(sfItem.getItem(), SlimefunItems.TIN_ORE, false) |
                                SlimefunUtils.isItemSimilar(sfItem.getItem(), SlimefunItems.URANIUM_ORE, false) |
                                SlimefunUtils.isItemSimilar(sfItem.getItem(), SlimefunItems.SILVER_ORE, false)
                    //sfItem.getItem().getType().toString().endsWith("_HEAD")

                ) {
                    return;
                }


                if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
                        item.getItemMeta().getCustomModelData() != 0
                        && (!SlimefunUtils.isItemSimilar(item, SlimefunItems.RAINBOW_GLASS, false)
                        && !SlimefunUtils.isItemSimilar(item, SlimefunItems.RAINBOW_CLAY, false)
                        && !SlimefunUtils.isItemSimilar(item, SlimefunItems.RAINBOW_CONCRETE, false)
                        && !SlimefunUtils.isItemSimilar(item, SlimefunItems.RAINBOW_GLASS_PANE, false)
                        && !SlimefunUtils.isItemSimilar(item, SlimefunItems.INFUSED_HOPPER, false)
                )


                ) {


                    World world = e.getBlock().getWorld();


                    Location loc = e.getBlock().getLocation();
                    loc.setX(loc.getBlockX() + 0.5);
                    loc.setZ(loc.getBlockZ() + 0.5);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);

                    ItemMeta meta = item.getItemMeta();
                    meta.setUnbreakable(true);

                    as.setHelmet(item);
                    as.setSmall(true);
                    as.setVisible(false);
                    as.setGravity(false);
                    as.setMarker(true);

                    if (e.getBlock().getType() == Material.DAYLIGHT_DETECTOR
                            | SlimefunUtils.isItemSimilar(item, SlimefunItems.ANCIENT_ALTAR, false)
                            | SlimefunUtils.isItemSimilar(item, SlimefunItems.COMPOSTER, false)
                            | SlimefunUtils.isItemSimilar(item, SlimefunItems.HOLOGRAM_PROJECTOR, false)


                    ) return;
                    if (e.getBlock().getType() == Material.DISPENSER
                            || e.getBlock().getType() == Material.CRAFTING_TABLE
                    ) {
                        e.getBlock().setType(Material.GLASS);
                        return;
                    }


                    //if (b == false) return;
                    //if(loc.getBlock().getBlockData() instanceof Directional) return;
                    if (loc.getBlock().getType().equals(Material.FURNACE)) {
                        Directional dir = (Directional) loc.getBlock().getBlockData();

                        if (dir.getFacing().equals(BlockFace.SOUTH)) {
                            as.setHeadPose(new EulerAngle(0, Math.toRadians(0), 0));
                        }
                        if (dir.getFacing().equals(BlockFace.WEST)) {
                            as.setHeadPose(new EulerAngle(0, Math.toRadians(90), 0));

                        }
                        if (dir.getFacing().equals(BlockFace.NORTH)) {
                            as.setHeadPose(new EulerAngle(0, Math.toRadians(-180), 0));
                        }
                        if (dir.getFacing().equals(BlockFace.EAST)) {
                            as.setHeadPose(new EulerAngle(0, Math.toRadians(-90), 0));
                        }
                    }
                    CraftWorld cw = (CraftWorld) e.getBlock().getWorld();
                    cw.getHandle().getTileEntity(new BlockPosition(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ()));
                    e.getBlock().setType(Material.GLASS);
//*/
                }


            }
        }
    }

    @EventHandler
    public void onBlockInterract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (
                    BlockStorage.hasBlockInfo(e.getClickedBlock().getLocation()) &&
                            SlimefunItem.getByID(BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "id")).getBreakTime() != 0) {
                //if (BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "breakingTime") != null) {
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 160000, -1, true, false, false));
            } else {
                e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
            }
        }
    }


    @EventHandler
    public void onBuild(BlockPlaceEvent e) {
        Block b = e.getBlockPlaced();
        if (b.getType().toString().contains("SLAB")) {
            Slab block = (Slab) e.getBlock().getBlockData();
            //e.getPlayer().sendMessage(String.valueOf(e.getBlockPlaced().getBlockData().getAsString().contains("type=double")));
            if (block.getType().equals(Slab.Type.DOUBLE)) {
                if (e.getBlockPlaced().getType().equals(Material.COBBLESTONE_SLAB))
                    e.getBlockPlaced().setType(Material.COBBLESTONE);            // Медь
                if (e.getBlockPlaced().getType().equals(Material.BRICK_SLAB))
                    e.getBlockPlaced().setType(Material.BRICKS);                        // Свинец
                if (e.getBlockPlaced().getType().equals(Material.SANDSTONE_SLAB))
                    e.getBlockPlaced().setType(Material.SANDSTONE);                // Олово
                if (e.getBlockPlaced().getType().equals(Material.NETHER_BRICK_SLAB))
                    e.getBlockPlaced().setType(Material.NETHER_BRICKS);            // Уран
                //if(e.getBlockPlaced().getType().equals(Material.OAK_SLAb)) e.getBlockPlaced().setType(Material.OAK_PLANKS);						// зачарованый Верстак
                if (e.getBlockPlaced().getType().equals(Material.OAK_SLAB))
                    e.getBlockPlaced().setType(Material.OAK_PLANKS);                        //
                if (e.getBlockPlaced().getType().equals(Material.QUARTZ_SLAB))
                    e.getBlockPlaced().setType(Material.QUARTZ_BLOCK);                        //алюминь
                if (e.getBlockPlaced().getType().equals(Material.SPRUCE_SLAB))
                    e.getBlockPlaced().setType(Material.SPRUCE_PLANKS);                //
                if (e.getBlockPlaced().getType().equals(Material.STONE_BRICK_SLAB))
                    e.getBlockPlaced().setType(Material.STONE_BRICKS);            // серебро
                if (e.getBlockPlaced().getType().equals(Material.STONE_SLAB))
                    e.getBlockPlaced().setType(Material.STONE);            //никель
                if (e.getBlockPlaced().getType().equals(Material.ACACIA_SLAB))
                    e.getBlockPlaced().setType(Material.ACACIA_PLANKS);                //
                if (e.getBlockPlaced().getType().equals(Material.BIRCH_SLAB))
                    e.getBlockPlaced().setType(Material.BIRCH_PLANKS);                    //
                if (e.getBlockPlaced().getType().equals(Material.DARK_OAK_SLAB))
                    e.getBlockPlaced().setType(Material.DARK_OAK_PLANKS);            //
                if (e.getBlockPlaced().getType().equals(Material.JUNGLE_SLAB))
                    e.getBlockPlaced().setType(Material.JUNGLE_PLANKS);                //
                if (e.getBlockPlaced().getType().equals(Material.DARK_PRISMARINE_SLAB))
                    e.getBlockPlaced().setType(Material.DARK_PRISMARINE);    //
                if (e.getBlockPlaced().getType().equals(Material.PRISMARINE_BRICK_SLAB))
                    e.getBlockPlaced().setType(Material.PRISMARINE_BRICKS);    //
                if (e.getBlockPlaced().getType().equals(Material.PRISMARINE_SLAB))
                    e.getBlockPlaced().setType(Material.PRISMARINE);                //
                if (e.getBlockPlaced().getType().equals(Material.PURPUR_SLAB))
                    e.getBlockPlaced().setType(Material.PURPUR_BLOCK);                //
                if (e.getBlockPlaced().getType().equals(Material.RED_SANDSTONE_SLAB))
                    e.getBlockPlaced().setType(Material.RED_SANDSTONE);
            }
        } else if (SlimefunUtils.isItemSimilar(e.getItemInHand(), SlimefunItems.TIN_ORE, false)) {
            Block bl = e.getBlockPlaced();
            bl.setType(Material.SANDSTONE_SLAB);
            Slab block1 = (Slab) bl.getBlockData();
            block1.setType(Slab.Type.DOUBLE);
            bl.setBlockData(block1);
        } else if (SlimefunUtils.isItemSimilar(e.getItemInHand(), SlimefunItems.LEAD_ORE, false)) {
            Block bl = e.getBlockPlaced();
            bl.setType(Material.BRICK_SLAB);
            Slab block1 = (Slab) bl.getBlockData();
            block1.setType(Slab.Type.DOUBLE);
            bl.setBlockData(block1);
        } else if (SlimefunUtils.isItemSimilar(e.getItemInHand(), SlimefunItems.ALUMINIUM_ORE, false)) {
            Block bl = e.getBlockPlaced();
            bl.setType(Material.QUARTZ_SLAB);
            Slab block1 = (Slab) bl.getBlockData();
            block1.setType(Slab.Type.DOUBLE);
            bl.setBlockData(block1);
        } else if (SlimefunUtils.isItemSimilar(e.getItemInHand(), SlimefunItems.NICKEL_ORE, false)) {
            Block bl = e.getBlockPlaced();
            bl.setType(Material.STONE_SLAB);
            Slab block1 = (Slab) bl.getBlockData();
            block1.setType(Slab.Type.DOUBLE);
            bl.setBlockData(block1);
        } else if (SlimefunUtils.isItemSimilar(e.getItemInHand(), SlimefunItems.SILVER_ORE, false)) {
            Block bl = e.getBlockPlaced();
            bl.setType(Material.STONE_BRICK_SLAB);
            Slab block1 = (Slab) bl.getBlockData();
            block1.setType(Slab.Type.DOUBLE);
            bl.setBlockData(block1);
        } else if (SlimefunUtils.isItemSimilar(e.getItemInHand(), SlimefunItems.URANIUM_ORE, false)) {
            Block bl = e.getBlockPlaced();
            bl.setType(Material.NETHER_BRICK_SLAB);
            Slab block1 = (Slab) bl.getBlockData();
            block1.setType(Slab.Type.DOUBLE);
            bl.setBlockData(block1);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        // Simply ignore any events that were faked by other plugins
        if (SlimefunPlugin.getIntegrations().isEventFaked(e)) {
            return;
        }

        // Also ignore custom blocks which were placed by other plugins
        if (SlimefunPlugin.getIntegrations().isCustomBlock(e.getBlock())) {
            return;
        }

        // Ignore blocks which we have marked as deleted (Fixes #2771)
        if (SlimefunPlugin.getTickerTask().isDeletedSoon(e.getBlock().getLocation())) {
            return;
        }

        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        checkForSensitiveBlockAbove(e, item);

        int fortune = getBonusDropsWithFortune(item, e.getBlock());
        List<ItemStack> drops = new ArrayList<>();

        if (!e.isCancelled() && !item.getType().isAir()) {
            callToolHandler(e, item, fortune, drops);
        }

        if (!e.isCancelled()) {
            callBlockHandler(e, item, drops);
        }

        dropItems(e, drops);
    }

    @ParametersAreNonnullByDefault
    private void callToolHandler(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
        SlimefunItem tool = SlimefunItem.getByItem(item);

        if (tool != null) {
            if (tool.canUse(e.getPlayer(), true)) {
                tool.callItemHandler(ToolUseHandler.class, handler -> handler.onToolUse(e, item, fortune, drops));
            } else {
                e.setCancelled(true);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void callBlockHandler(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
        SlimefunItem sfItem = BlockStorage.check(e.getBlock());

        if (sfItem == null && SlimefunPlugin.getBlockDataService().isTileEntity(e.getBlock().getType())) {
            Optional<String> blockData = SlimefunPlugin.getBlockDataService().getBlockData(e.getBlock());

            if (blockData.isPresent()) {
                sfItem = SlimefunItem.getByID(blockData.get());
            }
        }

        if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
            sfItem.callItemHandler(BlockBreakHandler.class, handler -> handler.onPlayerBreak(e, item, drops));

            if (e.isCancelled()) {
                return;
            }

            drops.addAll(sfItem.getDrops());
            BlockStorage.clearBlockInfo(e.getBlock());
            List<Entity> entityes = e.getBlock().getLocation().getWorld().getEntities();
            for (Entity ent : entityes) {

                //e.getPlayer().sendMessage(String.valueOf(ent));

                if (ent.getLocation().getX() - 0.5 == e.getBlock().getLocation().getX() &&
                        ent.getLocation().getY() == e.getBlock().getLocation().getY() &&
                        ent.getLocation().getZ() - 0.5 == e.getBlock().getLocation().getZ())
                    ent.remove();
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void dropItems(BlockBreakEvent e, List<ItemStack> drops) {
        if (!drops.isEmpty() && !e.isCancelled()) {
            // Notify plugins like CoreProtect
            SlimefunPlugin.getProtectionManager().logAction(e.getPlayer(), e.getBlock(), ProtectableAction.BREAK_BLOCK);

            // Fixes #2560
            if (e.isDropItems()) {
                // Disable normal block drops
                e.setDropItems(false);

                for (ItemStack drop : drops) {
                    // Prevent null or air from being dropped
                    if (drop != null && drop.getType() != Material.AIR) {
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
                    }
                }
            }
        }
    }

    /**
     * This method checks for a sensitive {@link Block}.
     * Sensitive {@link Block Blocks} are pressure plates or saplings, which should be broken
     * when the block beneath is broken as well.
     *
     * @param p The {@link Player} who broke this {@link Block}
     * @param b The {@link Block} that was broken
     */
    @ParametersAreNonnullByDefault
    private void checkForSensitiveBlockAbove(BlockBreakEvent e, ItemStack item) {
        Block blockAbove = e.getBlock().getRelative(BlockFace.UP);

        if (SlimefunTag.SENSITIVE_MATERIALS.isTagged(blockAbove.getType())) {
            SlimefunItem sfItem = BlockStorage.check(blockAbove);

            if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
                /*
                 * We create a dummy here to pass onto the BlockBreakHandler.
                 * This will set the correct block context.
                 */
                BlockBreakEvent dummyEvent = new BlockBreakEvent(blockAbove, e.getPlayer());
                List<ItemStack> drops = new ArrayList<>();
                drops.addAll(sfItem.getDrops(e.getPlayer()));

                sfItem.callItemHandler(BlockBreakHandler.class, handler -> handler.onPlayerBreak(dummyEvent, item, drops));
                blockAbove.setType(Material.AIR);

                if (!dummyEvent.isCancelled() && dummyEvent.isDropItems()) {
                    for (ItemStack drop : drops) {
                        if (drop != null && !drop.getType().isAir()) {
                            blockAbove.getWorld().dropItemNaturally(blockAbove.getLocation(), drop);
                        }
                    }
                }

                // Fixes #2944 - Don't forget to clear the Block Data
                BlockStorage.clearBlockInfo(blockAbove);
                List<Entity> entityes = e.getBlock().getLocation().getWorld().getEntities();
                for (Entity ent : entityes) {

                    //e.getPlayer().sendMessage(String.valueOf(ent));

                    if (ent.getLocation().getX() - 0.5 == e.getBlock().getLocation().getX() &&
                            ent.getLocation().getY() == e.getBlock().getLocation().getY() &&
                            ent.getLocation().getZ() - 0.5 == e.getBlock().getLocation().getZ())
                        ent.remove();
                }
            }
        }
    }

    private int getBonusDropsWithFortune(@Nullable ItemStack item, @Nonnull Block b) {
        int amount = 1;

        if (item != null && !item.getType().isAir() && item.hasItemMeta()) {
            /*
             * Small performance optimization:
             * ItemStack#getEnchantmentLevel() calls ItemStack#getItemMeta(), so if
             * we are handling more than one Enchantment, we should access the ItemMeta
             * directly and re use it.
             */
            ItemMeta meta = item.getItemMeta();
            int fortuneLevel = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);

            if (fortuneLevel > 0 && !meta.hasEnchant(Enchantment.SILK_TOUCH)) {
                Random random = ThreadLocalRandom.current();

                amount = Math.max(1, random.nextInt(fortuneLevel + 2) - 1);
                amount = (b.getType() == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (amount + 1);
            }
        }

        return amount;
    }

    @EventHandler
    public void onOreMine(BlockBreakEvent e) {
        if (!SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), e.getBlock(), ProtectableAction.BREAK_BLOCK))
            return;

        if (BlockStorage.hasBlockInfo(e.getBlock())) return;

        Block b = e.getBlock();
        if (
                SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.SMELTERS_PICKAXE, false) |
                        SlimefunUtils.canPlayerUseItem(e.getPlayer(),e.getPlayer().getInventory().getItemInMainHand(),false)
        ) return;
        if (b.getType().equals(Material.COBBLESTONE_SLAB) |  // COPPER_ORE
                b.getType().equals(Material.BRICK_SLAB) |        // LEAD_ORE
                b.getType().equals(Material.QUARTZ_SLAB) |       // ALUMINIUM_ORE
                b.getType().equals(Material.STONE_BRICK_SLAB) |  // SILVER_ORE
                b.getType().equals(Material.STONE_SLAB) |        // NICKEL_ORE
                b.getType().equals(Material.SANDSTONE_SLAB) |    // TIN_ORE
                b.getType().equals(Material.NETHER_BRICK_SLAB)) {//uranium

            Slab block = (Slab) e.getBlock().getBlockData();
            if (block.getType().equals(Slab.Type.DOUBLE)) {

                if (b.getType().equals(Material.STONE_SLAB)) {
                    e.setDropItems(false);
                    e.setExpToDrop(2);
                    if (SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.SMELTERS_PICKAXE, false))
                        return;
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.NICKEL_ORE);
                } else if (b.getType().equals(Material.STONE_BRICK_SLAB)) {
                    e.setDropItems(false);
                    e.setExpToDrop(2);
                    if (SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.HERCULES_PICKAXE, false)
                            | SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.SMELTERS_PICKAXE, false))
                        return;
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.SILVER_ORE);
                } else if (b.getType().equals(Material.QUARTZ_SLAB)) {
                    e.setDropItems(false);
                    e.setExpToDrop(1);
                    if (SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.HERCULES_PICKAXE, false)
                            | SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.SMELTERS_PICKAXE, false)
                    ) return;
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.ALUMINIUM_ORE);
                } else if (b.getType().equals(Material.BRICK_SLAB)) {
                    e.setDropItems(false);
                    e.setExpToDrop(2);
                    if (SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.SMELTERS_PICKAXE, false))
                        return;
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.LEAD_ORE);
                } else if (b.getType().equals(Material.SANDSTONE_SLAB)) {
                    e.setDropItems(false);
                    e.setExpToDrop(4);
                    if (SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.HERCULES_PICKAXE, false)
                            | SlimefunUtils.isItemSimilar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.SMELTERS_PICKAXE, false))
                        return;
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.TIN_ORE);
                } else if (b.getType().equals(Material.NETHER_BRICK_SLAB)) {
                    e.setDropItems(false);
                    e.setExpToDrop(7);
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.URANIUM);
                }

            }
        }
    }
/*

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOreExp(BlockExplodeEvent e) {

        Bukkit.broadcastMessage("ttt");
        if (BlockStorage.hasBlockInfo(e.getBlock())) {
            e.getBlock().setType(Material.AIR);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),SlimefunItem.getByItem(BlockStorage.retrieve(e.getBlock())).getItem());
            BlockStorage.clearBlockInfo(e.getBlock());
            return;
        }

        Block b = e.getBlock();
        if (b.getType().equals(Material.COBBLESTONE_SLAB) |  // COPPER_ORE
                b.getType().equals(Material.BRICK_SLAB) |        // LEAD_ORE
                b.getType().equals(Material.QUARTZ_SLAB) |       // ALUMINIUM_ORE
                b.getType().equals(Material.STONE_BRICK_SLAB) |  // SILVER_ORE
                b.getType().equals(Material.STONE_SLAB) |        // NICKEL_ORE
                b.getType().equals(Material.SANDSTONE_SLAB) |    // TIN_ORE
                b.getType().equals(Material.NETHER_BRICK_SLAB)) {//uranium

            Bukkit.broadcastMessage("ttt");
            Slab block = (Slab) e.getBlock().getBlockData();
            if (block.getType().equals(Slab.Type.DOUBLE)) {

                if (b.getType().equals(Material.STONE_SLAB)) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.NICKEL_ORE);
                } else if (b.getType().equals(Material.STONE_BRICK_SLAB)) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.SILVER_ORE);
                } else if (b.getType().equals(Material.QUARTZ_SLAB)) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.ALUMINIUM_ORE);
                } else if (b.getType().equals(Material.BRICK_SLAB)) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.LEAD_ORE);
                } else if (b.getType().equals(Material.SANDSTONE_SLAB)) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.TIN_ORE);
                } else if (b.getType().equals(Material.NETHER_BRICK_SLAB)) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), SlimefunItems.URANIUM);
                }

            }
        }
    }*/

}
