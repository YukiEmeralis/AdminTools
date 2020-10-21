package com.yukiemeralis.blogspot.plugins.admintools.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings({"serial", "unused"})
public class SilkSpawner implements Listener
{
    // Mob registry
    private static final HashMap<String, EntityType> name_registry = new HashMap<>() 
    {{
        put("Blaze spawner", EntityType.BLAZE);
        put("Cave spider spawaner", EntityType.CAVE_SPIDER);
        put("Zombie spawner", EntityType.ZOMBIE);
        put("Spider spawner", EntityType.SPIDER);
        put("Skeleton spawner", EntityType.SKELETON);
        put("Silverfish spawner", EntityType.SILVERFISH);
        put("Magma cube spawner", EntityType.MAGMA_CUBE);
    }};

    // Inverse mob registry
    private static final HashMap<EntityType, String> inverse_name_registry = new HashMap<>()
    {{
        name_registry.forEach((name, type) -> {
            put(type, name);
        });
    }};

    @EventHandler
    public void silkSpawner(BlockBreakEvent event)
    {
        // Only listen to mob spawners
        if (!event.getBlock().getType().equals(Material.SPAWNER))
            return; 
        // Make sure the player has silk touch
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
        if (!itemInHand.getEnchantments().containsKey(Enchantment.SILK_TOUCH))
            return;

        CreatureSpawner state = (CreatureSpawner) event.getBlock().getState();
        EntityType type = state.getSpawnedType();

        String name = "§r" + inverse_name_registry.get(type);

        ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
        ItemMeta meta = spawner.getItemMeta();
        meta.setDisplayName(name);
        spawner.setItemMeta(meta);

        event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), spawner);
    }

    @EventHandler
    public void placeSpawner(BlockPlaceEvent event)
    {
        if (!event.getBlockPlaced().getType().equals(Material.SPAWNER))
            return;
        
        String name = event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
        if (!name_registry.containsKey(name))
            return; // We don't want things to break if someone places down a regular unnamed spawner

        EntityType type = name_registry.get(name);
        CreatureSpawner state = (CreatureSpawner) event.getBlockPlaced().getState();
        state.setSpawnedType(type);
        state.update();
    }
}
