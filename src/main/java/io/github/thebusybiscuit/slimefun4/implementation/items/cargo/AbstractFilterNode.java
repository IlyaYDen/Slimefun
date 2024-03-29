package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

/**
 * This abstract super class represents all filtered Cargo nodes.
 * 
 * @author TheBusyBiscuit
 * 
 * @see CargoInputNode
 * @see AdvancedCargoOutputNode
 *
 */
abstract class AbstractFilterNode extends AbstractCargoNode {

    protected static final int[] SLOTS = { 19, 20, 21, 28, 29, 30, 37, 38, 39 };
    private static final String FILTER_TYPE = "filter-type";
    private static final String FILTER_LORE = "filter-lore";

    @ParametersAreNonnullByDefault
    protected AbstractFilterNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        addItemHandler(onBreak());
    }

    @Override
    public boolean hasItemFilter() {
        return true;
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                BlockMenu inv = BlockStorage.getInventory(b);

                if (inv != null) {
                    inv.dropItems(b.getLocation(), SLOTS);
                }
            }
        };
    }

    @Nonnull
    protected abstract int[] getBorder();

    @Override
    protected void onPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        BlockStorage.addBlockInfo(b, "index", "0");
        BlockStorage.addBlockInfo(b, FILTER_TYPE, "whitelist");
        BlockStorage.addBlockInfo(b, FILTER_LORE, String.valueOf(true));
        BlockStorage.addBlockInfo(b, "filter-durability", String.valueOf(false));
    }

    @Override
    protected void createBorder(BlockMenuPreset preset) {
        for (int i : getBorder()) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(2, new CustomItem(Material.PAPER, "&3Предметы", "", "&bПоместите все предметы, которые вы хотите", "&bв черный/белый список"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected void updateBlockMenu(BlockMenu menu, Block b) {
        Location loc = b.getLocation();
        String filterType = BlockStorage.getLocationInfo(loc, FILTER_TYPE);

        if (!BlockStorage.hasBlockInfo(b) || filterType == null || filterType.equals("whitelist")) {
            menu.replaceExistingItem(15, new CustomItem(Material.WHITE_WOOL, "&7Тип: &rБелый список", "", "&e> Нажмите, чтобы изменить его на Черный список"));
            menu.addMenuClickHandler(15, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, FILTER_TYPE, "blacklist");
                updateBlockMenu(menu, b);
                return false;
            });
        } else {
            menu.replaceExistingItem(15, new CustomItem(Material.BLACK_WOOL, "&7Тип: &8Черный список", "", "&e> Нажмите, чтобы изменить его на Белый список"));
            menu.addMenuClickHandler(15, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, FILTER_TYPE, "whitelist");
                updateBlockMenu(menu, b);
                return false;
            });
        }

        String lore = BlockStorage.getLocationInfo(b.getLocation(), FILTER_LORE);

        if (!BlockStorage.hasBlockInfo(b) || lore == null || lore.equals(String.valueOf(true))) {
            menu.replaceExistingItem(25, new CustomItem(Material.MAP, "&7Учитывать Описание: &2\u2714", "", "&e> Нажмите, чтобы переключить, должно ли описание совпадать"));
            menu.addMenuClickHandler(25, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, FILTER_LORE, String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        } else {
            menu.replaceExistingItem(25, new CustomItem(Material.MAP, "&7Учитывать Описание: &4\u2718", "", "&e> Нажмите, чтобы переключить, должно ли описание совпадать"));
            menu.addMenuClickHandler(25, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, FILTER_LORE, String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        }

        addChannelSelector(b, menu, 41, 42, 43);
        markDirty(loc);
    }

    @Override
    protected void markDirty(@Nonnull Location loc) {
        CargoNet network = CargoNet.getNetworkFromLocation(loc);

        if (network != null) {
            network.markCargoNodeConfigurationDirty(loc);
        }
    }

}
