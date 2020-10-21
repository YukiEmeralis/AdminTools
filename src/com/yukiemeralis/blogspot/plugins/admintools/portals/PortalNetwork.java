package com.yukiemeralis.blogspot.plugins.admintools.portals;

import java.util.HashMap;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class PortalNetwork 
{
    public static HashMap<Biome, Portal> portals = new HashMap<>();
    public static HashMap<Player, Portal> limbo_portals = new HashMap<>();

    public static void registerPortal(Portal portal)
    {
        portals.put(portal.getBiome(), portal);
    }

    public static void deregisterPortal(Portal portal)
    {
        portals.remove(portal.getBiome());
    }
}
