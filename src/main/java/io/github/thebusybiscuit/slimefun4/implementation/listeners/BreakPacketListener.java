package io.github.thebusybiscuit.slimefun4.implementation.listeners;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class BreakPacketListener {


    Location loc;
    //int t;

    public HashMap<UUID,Location> animation = new HashMap<>();


    public HashMap<UUID,Integer> animationL = new HashMap<>();


    HashMap<Integer,Integer> test;

//‘- skill{s=urskill;delay=60} @self ~onDeath’
    public BreakPacketListener(SlimefunPlugin slimefunPlugin) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        Bukkit.getScheduler().runTaskTimer(slimefunPlugin, new Runnable() {
            @Override
            public void run() {
                for(Player p :Bukkit.getServer().getOnlinePlayers()){
                    if(
                            animation.containsKey(p.getUniqueId()) && BlockStorage.hasBlockInfo(animation.get(p.getUniqueId())) &&
                                    BlockStorage.check(animation.get(p.getUniqueId())).getBreakTime() > 0
                        //&& BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"breakingTime") != null
                    ) {
                        //int timeBreak = Integer.parseInt(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"breakingTime"));
                        int timeBreak = SlimefunItem.getByID(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"id")).getBreakTime();
                        int x = 1;
                        //Bukkit.broadcastMessage(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"breakingItem"));
                        if(SlimefunItem.getByID(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()), "id")).getBreakItem().equals("shovel")){
                            if(p.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_SHOVEL)) x = 2;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.STONE_SHOVEL)) x = 4;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.IRON_SHOVEL)) x = 6;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SHOVEL)) x = 8;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SHOVEL)) x = 9;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_SHOVEL)) x = 12;
                        }
                        else if(SlimefunItem.getByID(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"id")).getBreakItem().equals("pickaxe")) {

                            //Bukkit.broadcastMessage(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"breakingItem"));

                            if(p.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_PICKAXE)) x = 2;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.STONE_PICKAXE)) x = 4;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.IRON_PICKAXE)) x = 6;
                            else if(p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) x = 8;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_PICKAXE)) x = 9;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_PICKAXE)) x = 12;
                        }
                        else if(SlimefunItem.getByID(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"id")).getBreakItem().equals("axe")) {
                            if(p.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_AXE)) x = 2;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.STONE_AXE)) x = 4;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.IRON_AXE)) x = 6;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_AXE)) x = 8;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_AXE)) x = 9;
                            else if(p.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_AXE)) x = 12;
                        }
                        if(p.getInventory().getItemInMainHand().containsEnchantment(Enchantment.DIG_SPEED))
                            x = x + (1+(p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DIG_SPEED)*p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DIG_SPEED)));
/*
                        else if(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"breakingItem") == "pickaxe") {
                            if(p.getInventory().getItemInMainHand().equals(Material.WOODEN_)) x = 5;
                            else if(p.getInventory().getItemInMainHand().equals(Material.STONE_)) x = 10;
                            else if(p.getInventory().getItemInMainHand().equals(Material.IRON_)) x = 20;
                            else if(p.getInventory().getItemInMainHand().equals(Material.DIAMOND_)) x = 30;
                            else if(p.getInventory().getItemInMainHand().equals(Material.NETHERITE_)) x = 40;
                        }*/


                        if(animation.get(p.getUniqueId()).getBlock().getType().equals(Material.AIR)){
                            animation.remove(p.getUniqueId());
                            animationL.remove(p.getUniqueId());
                            return;
                        }
                        PacketContainer p1 = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
                        p1.getModifier().writeDefaults();
                        //p1.getBlockPositionModifier().write(0,new BlockPosition(animation.get(p.getUniqueId()).getBlockX(),animation.get(p.getUniqueId()).getBlockY(),animation.get(p.getUniqueId()).getBlockZ()));
                        p1.getBlockPositionModifier().write(0,new BlockPosition(animation.get(p.getUniqueId()).getBlockX(),animation.get(p.getUniqueId()).getBlockY(),animation.get(p.getUniqueId()).getBlockZ()));
                        p1.getIntegers().write(1, getCMD(animationL.get(p.getUniqueId()),timeBreak,10));
                        //  Bukkit.broadcastMessage("ff    " +animationL.get(p.getUniqueId()) + "   " + getCMD(animationL.get(p.getUniqueId()),timeBreak,10));
                        //getIntegers().write(1, animationL.get(p.getUniqueId()));

                        try {
                            for(Player pall :Bukkit.getServer().getOnlinePlayers()) {
                                manager.sendServerPacket(pall, p1);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        animationL.put(p.getUniqueId(),animationL.get(p.getUniqueId())+x);

                        if(animationL.get(p.getUniqueId()) >= timeBreak) {
                            if(p.getInventory().getItemInMainHand().getDurability()>1){
                                p.getInventory().getItemInMainHand().setDurability((short) (p.getInventory().getItemInMainHand().getDurability()-1));

                            } else{
                                System.out.print(p.getInventory().getItemInMainHand().getDurability());
                                p.getInventory().getItemInMainHand().setType(Material.AIR);
                                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK,1,1);
                            }
                            BlockStorage.clearBlockInfo(animation.get(p.getUniqueId()));
                            animation.get(p.getUniqueId()).getBlock().setType(Material.AIR);
                            for(Entity e:p.getWorld().getEntities()){
                                if(e.getLocation().getBlock().getLocation().equals(animation.get(p.getUniqueId()).getBlock().getLocation())) e.remove();
                            }

                            p.getWorld().dropItemNaturally(animation.get(p.getUniqueId()), SlimefunItem.getByID(BlockStorage.getLocationInfo(animation.get(p.getUniqueId()),"id")).getItem());
                        }
                    }
                }

            }
        },1,1);
        manager.addPacketListener(new PacketAdapter(slimefunPlugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {



                PacketContainer packet = event.getPacket();
                Player p = event.getPlayer();
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                BlockPosition pb = packet.getBlockPositionModifier().getValues().get(0);
                if(digType.equals(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK)){
                    animation.put(p.getUniqueId(),new Location(p.getWorld(),pb.getX(),pb.getY(),pb.getZ()));
                    animationL.put(p.getUniqueId(),0);
                }

                //event.getPlayer().sendMessage("DigType: "+digType.name());

                PacketContainer p1 = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
                p1.getModifier().writeDefaults();
                p1.getBlockPositionModifier().write(0,new BlockPosition(pb.getX(),pb.getY(),pb.getZ()));

                if(digType.name().equals("ABORT_DESTROY_BLOCK")){

                    p1.getBlockPositionModifier().write(0,new BlockPosition(pb.getX(),-1,pb.getZ()));
                    p1.getIntegers().write(1, 0);

                    animation.remove(p.getUniqueId());
                    animationL.remove(p.getUniqueId());
                    loc = null;
                    //t = -1;

                    try {
                        for(Player pall :Bukkit.getServer().getOnlinePlayers()) {
                            manager.sendServerPacket(pall, p1);
                        }
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    private static int getCMD(int timeLeft, int time,int size) {
        Double timeleftt = (double) timeLeft;
        Double tiime = (double) time;
        return (int) (((size / tiime) * timeleftt));
        //return (time - timeLeft);
    }

}
