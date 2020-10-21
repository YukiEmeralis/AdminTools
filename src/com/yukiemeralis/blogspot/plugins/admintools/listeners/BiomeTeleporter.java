package com.yukiemeralis.blogspot.plugins.admintools.listeners;

import java.util.ArrayList;

import com.yukiemeralis.blogspot.plugins.admintools.portals.Portal;
import com.yukiemeralis.blogspot.plugins.admintools.portals.PortalNetwork;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BiomeTeleporter implements Listener
{
    @EventHandler
    public void checkPortalPlacement(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        Material held = event.getPlayer().getInventory().getItemInMainHand().getType();

        if (!held.equals(Material.BLAZE_POWDER))
            return;

        if (!event.getClickedBlock().getType().equals(Material.END_ROD))
            return;

        if (!verifyPortal(event.getClickedBlock()))
            return;

        Portal portal = new Portal(event.getClickedBlock().getLocation(), event.getClickedBlock().getBiome());

        // Second activation attempt
        if (PortalNetwork.limbo_portals.containsKey(event.getPlayer()))
        {
            Portal p = PortalNetwork.limbo_portals.get(event.getPlayer());

            PortalNetwork.portals.get(p.getBiome()).deactivate();
            
            PortalNetwork.limbo_portals.remove(event.getPlayer());
            PortalNetwork.registerPortal(p);
            p.activate();

            event.getPlayer().sendMessage("§aOverwrote the portal for biome: " + p.getBiome().name() + "!");
            return;
        }

        // First activation attempt
        if (PortalNetwork.portals.containsKey(portal.getBiome())) // If a portal to this biome already exists
        {
            Player p = event.getPlayer();

            PortalNetwork.limbo_portals.put(p, portal);
            p.sendMessage("§6A portal is already registered for this biome! Left click this portal again to overwrite that portal.");
            p.sendMessage("§cIf you overwrite, you will have to go back to the previous portal and activate it again.");
        } else { // If no portal exists
            PortalNetwork.registerPortal(portal);
            portal.activate();
        }
    }

    private boolean verifyPortal(Block start)
    {
        Block purpur = start.getWorld().getBlockAt(start.getLocation().add(0,-1, 0));

        Block endStone1 = start.getWorld().getBlockAt(start.getLocation().add(0,-2, 0));
        Block endStone2 = start.getWorld().getBlockAt(start.getLocation().add(1,-2, 0));
        Block endStone3 = start.getWorld().getBlockAt(start.getLocation().add(0,-2, 1));
        Block endStone4 = start.getWorld().getBlockAt(start.getLocation().add(-1,-2, 0));
        Block endStone5 = start.getWorld().getBlockAt(start.getLocation().add(0,-2, -1));

        if (
            purpur.getType().equals(Material.PURPUR_BLOCK) &&
            endStone1.getType().equals(Material.END_STONE_BRICKS) &&
            endStone2.getType().equals(Material.END_STONE_BRICKS) &&
            endStone3.getType().equals(Material.END_STONE_BRICKS) &&
            endStone4.getType().equals(Material.END_STONE_BRICKS) &&
            endStone5.getType().equals(Material.END_STONE_BRICKS)
        ) {
            return true;
        }
        
        return false;
    }

    @SuppressWarnings("serial")
    private final static ArrayList<Material> portalBlocks = new ArrayList<>() {{
        portalBlocks.add(Material.END_ROD);
        portalBlocks.add(Material.PURPUR_BLOCK);
        portalBlocks.add(Material.END_STONE_BRICKS);
    }};

    // Portal destruction listener
    @EventHandler
    public void onPortalDestruction(BlockBreakEvent event)
    {
        if (!portalBlocks.contains(event.getBlock().getType()))
            return;

        PortalNetwork.portals.forEach((biome, portal) -> {
            if (portal.getBlocks().contains(event.getBlock()))
            {
                portal.deactivate();
                event.getPlayer().sendMessage("§6Portal to biome: " + portal.getBiome().name() + " has been broken!");
                return;
            }  
        });

        PortalNetwork.portals.entrySet().removeIf(portal -> !portal.getValue().isActive());
    }
}
