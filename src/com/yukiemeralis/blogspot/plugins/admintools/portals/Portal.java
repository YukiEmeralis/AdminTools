package com.yukiemeralis.blogspot.plugins.admintools.portals;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Portal 
{
    Biome biome;
    Location location;

    boolean active = false;

    ArrayList<Block> components = new ArrayList<>();

    public Portal(Location location, Biome biome)
    {
        this.biome = biome;
        this.location = location;
    }

    public void teleport(Player player)
    {
        player.teleport(location);
    }

    public boolean isActive()
    {
        return active;
    }

    public ArrayList<Block> getBlocks()
    {
        return components;
    }

    public Biome getBiome()
    {
        return biome;
    }

    public Location getLocation()
    {
        return location;
    }

    public void activate()
    {
        active = true;
    }

    public void deactivate()
    {
        active = false;
    }
}
