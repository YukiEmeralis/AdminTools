package com.yukiemeralis.blogspot.plugins.admintools;

import com.yukiemeralis.blogspot.plugins.admintools.items.FireworkRecipe;
import com.yukiemeralis.blogspot.plugins.admintools.items.TestificateTeleporterItem;
import com.yukiemeralis.blogspot.plugins.admintools.listeners.*;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin 
{
    private static Main instance;

    @Override
    public void onEnable()
    {
        instance = this;

        // Commands
        this.getCommand("at").setExecutor(new AdminCommand());
        this.getServer().getPluginManager().registerEvents(new AdminEvents(), this);
        this.getServer().getPluginManager().registerEvents(new Treecapitator(), this);
        this.getServer().getPluginManager().registerEvents(new SilkSpawner(), this);
        this.getServer().getPluginManager().registerEvents(new VillagerTeleport(), this);

        // Recipes
        new FireworkRecipe().register();   
        new TestificateTeleporterItem().register();     
    }

    @Override
    public void onDisable()
    {
        // Deselect all villagers
        AdminData.accounts.forEach((user, account) -> {
            if (account.getVillager() != null)
                account.getVillager().setInvulnerable(false);
        });
    }

    public static Main getInstance()
    {
        return instance;
    }
}
