package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link IndustrialMiner} is a {@link MultiBlockMachine} that can mine any
 * ores it finds in a given range underneath where it was placed.
 * 
 * <i>And for those of you who are wondering... yes this is the replacement for the
 * long-time deprecated Digital Miner.</i>
 * 
 * @author TheBusyBiscuit
 * 
 * @see AdvancedIndustrialMiner
 * @see ActiveMiner
 *
 */
public class IndustrialMiner extends MultiBlockMachine {

    protected final Map<Location, ActiveMiner> activeMiners = new HashMap<>();
    protected final List<MachineFuel> fuelTypes = new ArrayList<>();

    private final int range;
    private final boolean silkTouch;
    private final ItemSetting<Boolean> canMineAncientDebris = new ItemSetting<>(this, "can-mine-ancient-debris", false);

    @ParametersAreNonnullByDefault
    public IndustrialMiner(Category category, SlimefunItemStack item, Material baseMaterial, boolean silkTouch, int range) {
        super(category, item, new ItemStack[] { null, null, null, new CustomItem(Material.PISTON, "Поршень (Смотрит вверх)"), new ItemStack(Material.CHEST), new CustomItem(Material.PISTON, "Поршень (Смотрит вверх)"), new ItemStack(baseMaterial), new ItemStack(Material.BLAST_FURNACE), new ItemStack(baseMaterial) }, BlockFace.UP);

        this.range = range;
        this.silkTouch = silkTouch;

        registerDefaultFuelTypes();
        addItemSetting(canMineAncientDebris);
    }

    /**
     * This returns whether this {@link IndustrialMiner} will output ores as they are.
     * Similar to the Silk Touch {@link Enchantment}.
     * 
     * @return Whether to treat ores with Silk Touch
     */
    public boolean hasSilkTouch() {
        return silkTouch;
    }

    /**
     * This method returns the range of the {@link IndustrialMiner}.
     * The total area will be determined by the range multiplied by 2 plus the actual center
     * of the machine.
     * 
     * So a range of 3 will make the {@link IndustrialMiner} affect an area of 7x7 blocks.
     * 3 on all axis, plus the center of the machine itself.
     * 
     * @return The range of this {@link IndustrialMiner}
     */
    public int getRange() {
        return range;
    }

    /**
     * This registers the various types of fuel that can be used in the
     * {@link IndustrialMiner}.
     */
    protected void registerDefaultFuelTypes() {
        // Coal & Charcoal
        fuelTypes.add(new MachineFuel(4, new ItemStack(Material.COAL)));
        fuelTypes.add(new MachineFuel(4, new ItemStack(Material.CHARCOAL)));

        fuelTypes.add(new MachineFuel(40, new ItemStack(Material.COAL_BLOCK)));
        fuelTypes.add(new MachineFuel(10, new ItemStack(Material.DRIED_KELP_BLOCK)));
        fuelTypes.add(new MachineFuel(4, new ItemStack(Material.BLAZE_ROD)));

        // Logs
        for (Material mat : Tag.LOGS.getValues()) {
            fuelTypes.add(new MachineFuel(1, new ItemStack(mat)));
        }
    }

    /**
     * This method returns the outcome that mining certain ores yields.
     * 
     * @param ore
     *            The {@link Material} of the ore that was mined
     * 
     * @return The outcome when mining this ore
     */
    //edited
    public ItemStack getOutcome(Block ore) {
        if (hasSilkTouch() && !ore.getType().toString().endsWith("_SLAB")) {
            return new ItemStack(ore.getType());
        }
        Random random = ThreadLocalRandom.current();
        switch (ore.getType()) {
            case BRICK_SLAB:
                return new CustomItem(SlimefunItems.LEAD_ORE,1);
            case QUARTZ_SLAB:
                return new CustomItem(SlimefunItems.ALUMINIUM_ORE,1);
            case STONE_BRICK_SLAB:
                return new CustomItem(SlimefunItems.SILVER_ORE,1);
            case STONE_SLAB:
                return new CustomItem(SlimefunItems.NICKEL_ORE,1);
            case SANDSTONE_SLAB:
                return new CustomItem(SlimefunItems.TIN_ORE,1);
            case NETHER_BRICK_SLAB:
                return new CustomItem(SlimefunItems.URANIUM,1);
            case COAL_ORE:
                return new ItemStack(Material.COAL);
            case DIAMOND_ORE:
                return new ItemStack(Material.DIAMOND);
            case EMERALD_ORE:
                return new ItemStack(Material.EMERALD);
            case NETHER_QUARTZ_ORE:
                return new ItemStack(Material.QUARTZ);
            case REDSTONE_ORE:
                return new ItemStack(Material.REDSTONE, 4 + random.nextInt(2));
            case LAPIS_ORE:
                return new ItemStack(Material.LAPIS_LAZULI, 4 + random.nextInt(4));
            default:
                // This includes Iron and Gold ore (and Ancient Debris)
                return new ItemStack(ore.getType());
        }
    }

    /**
     * This registers a new fuel type for this {@link IndustrialMiner}.
     * 
     * @param ores
     *            The amount of ores this allows you to mine
     * @param item
     *            The item that shall be consumed
     */
    public void addFuelType(int ores, ItemStack item) {
        Validate.isTrue(ores > 1 && ores % 2 == 0, "Количество руд должно быть не менее 2 и кратно 2.");
        fuelTypes.add(new MachineFuel(ores / 2, item));
    }

    @Override
    public String getLabelLocalPath() {
        return "guide.tooltips.recipes.generator";
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> list = new ArrayList<>();

        for (MachineFuel fuel : fuelTypes) {
            ItemStack item = fuel.getInput().clone();
            ItemMeta im = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColors.color("&8\u21E8 &7Длится максимум. " + fuel.getTicks() + " Руды"));
            //lore.add(ChatColors.color("&8\u21E8 &7Lasts for max. " + fuel.getTicks() + " Ores"));
            im.setLore(lore);
            item.setItemMeta(im);
            list.add(item);
        }

        return list;
    }

    @Override
    public void onInteract(Player p, Block b) {
        if (activeMiners.containsKey(b.getLocation())) {
            SlimefunPlugin.getLocalization().sendMessage(p, "machines.INDUSTRIAL_MINER.already-running");
            return;
        }

        Block chest = b.getRelative(BlockFace.UP);
        Block[] pistons = findPistons(chest);

        int mod = getRange();
        Block start = b.getRelative(-mod, -1, -mod);
        Block end = b.getRelative(mod, -1, mod);

        ActiveMiner instance = new ActiveMiner(this, p.getUniqueId(), chest, pistons, start, end);
        instance.start(b);
    }

    private Block[] findPistons(Block chest) {
        Block northern = chest.getRelative(BlockFace.NORTH);

        if (northern.getType() == Material.PISTON) {
            return new Block[] { northern, chest.getRelative(BlockFace.SOUTH) };
        } else {
            return new Block[] { chest.getRelative(BlockFace.WEST), chest.getRelative(BlockFace.EAST) };
        }
    }

    /**
     * This returns whether this {@link IndustrialMiner} can mine the given {@link Material}.
     * 
     * @param type
     *            The {@link Material} to check
     *
     * @return Whether this {@link IndustrialMiner} is capable of mining this {@link Material}
     */
    //edited
    public boolean canMine(Block type) {
       // Bukkit.broadcastMessage("ttt");
        if (SlimefunTag.INDUSTRIAL_MINER_ORES.isTagged(type.getType())) {
            return true;
        } else if(!BlockStorage.hasBlockInfo(type) &&(
                type.getType().equals(Material.BRICK_SLAB)|        // LEAD_ORE
                        type.getType().equals(Material.QUARTZ_SLAB)|       // ALUMINIUM_ORE
                        type.getType().equals(Material.STONE_BRICK_SLAB)|  // SILVER_ORE
                        type.getType().equals(Material.STONE_SLAB)|        // NICKEL_ORE
                        type.getType().equals(Material.SANDSTONE_SLAB)|    // TIN_ORE
                        type.getType().equals(Material.NETHER_BRICK_SLAB))){

            //Bukkit.broadcastMessage("ttt2");
            Slab block = (Slab) type.getBlockData();
            if (block.getType().equals(Slab.Type.DOUBLE))
                return true;
        }
        else if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            return type.getType() == Material.ANCIENT_DEBRIS && canMineAncientDebris.getValue();

        }


        return false;
    }

}
