package com.yukiemeralis.blogspot.plugins.admintools.listeners;

import java.util.ArrayList;

import com.yukiemeralis.blogspot.plugins.admintools.AdminData;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@SuppressWarnings({"serial", "unused"})
public class Treecapitator implements Listener
{
    /**
     * We handle this in a similar way to how we use the A* algorithm.
     * We have a list of "open" blocks, which are blocks that haven't been parsed yet,
     * we have a list of "closed" blocks, which are blocks that HAVE been parsed,
     * and we have a list of "marked" blocks which are blocks to be destroyed.
     * 
     * We use the block broken as the seed, and parse it for nearby log blocks, marking them and adding them to open.
     * We then proceed to parse THOSE blocks, adding the block to closed and new ones to open. We do NOT mark blocks twice.
     */

    private ArrayList<Block> open = new ArrayList<>();
    private ArrayList<Block> closed = new ArrayList<>();
    private ArrayList<Block> marked = new ArrayList<>();

    private final ArrayList<Material> log_types = new ArrayList<>() {{
        add(Material.OAK_LOG);
        add(Material.BIRCH_LOG);
        add(Material.SPRUCE_LOG);
        add(Material.DARK_OAK_LOG);
        add(Material.ACACIA_LOG);
        add(Material.JUNGLE_LOG);
        add(Material.CRIMSON_STEM);
        add(Material.WARPED_STEM);
    }};

    private final ArrayList<Material> axes = new ArrayList<>() {{
        add(Material.WOODEN_AXE);
        add(Material.STONE_AXE);
        add(Material.IRON_AXE);
        add(Material.GOLDEN_AXE);
        add(Material.DIAMOND_AXE);
        add(Material.NETHERITE_AXE);
    }};

    @EventHandler
    public void logBreak(BlockBreakEvent event)
    {
        // Only listen to logs breaking
        if (!log_types.contains(event.getBlock().getType()))
            return;

        // Listen to axes only
        if (!axes.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
            return;

        // Check if the player has treecapitator enabled
        if (!AdminData.accounts.get(event.getPlayer().getUniqueId().toString()).getHasTreecap())
            return;
        
        open.add(event.getBlock());
        while (!open.isEmpty())
        {
            parseBlocks(open.get(0));

            closed.add(open.get(0));
            open.remove(0);
        }

        // Destroy all marked blocks
        marked.forEach(block -> {
            block.breakNaturally();
        });

        // Cleanup
        open.clear();
        closed.clear();
        marked.clear();
    }    

    private void parseBlocks(Block block)
    {
        Location loc = block.getLocation();
        double x = loc.getX(), y = loc.getY(), z = loc.getZ();

        //      N
        //    0 1 2
        // W  3 4 5  E
        //    6 7 8
        //      S

        // Layer 1
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y)-1, (int) Math.round(z)+1)); // 0
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y)-1, (int) Math.round(z)+1)); // 1
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y)-1, (int) Math.round(z)+1)); // 2
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y)-1, (int) Math.round(z))); // 3
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y)-1, (int) Math.round(z))); // 4 
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y)-1, (int) Math.round(z))); // 5
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y)-1, (int) Math.round(z)-1)); // 6
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y)-1, (int) Math.round(z)-1)); // 7 
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y)-1, (int) Math.round(z)-1)); // 8

        // Layer 2
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y), (int) Math.round(z)+1)); // 0
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y), (int) Math.round(z)+1)); // 1
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y), (int) Math.round(z)+1)); // 2
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y), (int) Math.round(z))); // 3
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y), (int) Math.round(z))); // 5
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y), (int) Math.round(z)-1)); // 6
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y), (int) Math.round(z)-1)); // 7 
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y), (int) Math.round(z)-1)); // 8

        // Layer 3
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y)+1, (int) Math.round(z)+1)); // 0
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y)+1, (int) Math.round(z)+1)); // 1
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y)+1, (int) Math.round(z)+1)); // 2
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y)+1, (int) Math.round(z))); // 3
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y)+1, (int) Math.round(z))); // 4 
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y)+1, (int) Math.round(z))); // 5
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)-1, (int) Math.round(y)+1, (int) Math.round(z)-1)); // 6
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x), (int) Math.round(y)+1, (int) Math.round(z)-1)); // 7 
        parseBlock(block.getWorld().getBlockAt((int) Math.round(x)+1, (int) Math.round(y)+1, (int) Math.round(z)-1)); // 8
    }

    private void parseBlock(Block block)
    {
        // If it's not a log...
        if (!log_types.contains(block.getType()))
            return;

        // If it  hasn't already been parsed...
        if (closed.contains(block))
            return;

        // Or if it's already been seen but not parsed...
        if (open.contains(block))
            return;

        // Finally if it's not marked...
        if (!marked.contains(block))
        {
            marked.add(block);
            open.add(block);
        }
    }
}
