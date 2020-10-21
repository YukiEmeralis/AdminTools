package com.yukiemeralis.blogspot.plugins.admintools;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("serial")
public class PlayerAccount 
{
    private String UUID;
    private HashMap<Integer, Death> deaths = new HashMap<>();
    private boolean hasTreecapitator = true;
    private boolean haspvp = true;
    private boolean salmonCooldown = false; 

    private Entity saved_villager;

    // For Gson
    public PlayerAccount() {}

    public PlayerAccount(Player player)
    {
        this.UUID = player.getUniqueId().toString();
    }

    public void addDeath(Player player, PlayerDeathEvent event)
    {
        int index = 0;
        while (true)
        {
            if (!deaths.containsKey(index))
            {
                deaths.put(index, new Death(player, event, index));
                break;
            }
            index++;
        }
    }

    private static final ItemStack error_item = new ItemStack(Material.STICK);

    static {
        ItemMeta meta = error_item.getItemMeta();
        meta.setDisplayName("§r§cFailed...");
        meta.setLore(new ArrayList<String>() {{
            add("§r§7Send Yuki a screenshot please!");
        }});
        error_item.setItemMeta(meta);
    }

    public HashMap<Integer, Death> getDeaths()
    {
        return deaths;
    }

    public boolean getHasTreecap()
    {
        return hasTreecapitator;
    }

    public void setTreecapitator(boolean arg)
    {
        hasTreecapitator = arg;
    }

    public void toggleTreecap()
    {
        hasTreecapitator = !hasTreecapitator;
    }

    public void setUUID(String UUID)
    {
        this.UUID = UUID;
    }

    public String getUUID()
    {
        return this.UUID;
    }

    public void togglePVP()
    {
        this.haspvp = !this.haspvp;
    }

    public boolean getHasPVP()
    {
        return haspvp;
    }

    public boolean getSalmonCooldown()
    {
        return salmonCooldown;
    }

    public void triggerSalmonCooldown()
    {
        salmonCooldown = true;
        new BukkitRunnable(){
            @Override
            public void run() {
                salmonCooldown = false;
            }
        }.runTaskLater(Main.getInstance(), 10*20);
    }

    public void setVillager(Entity e)
    {
        saved_villager = e;
    }

    public Entity getVillager()
    {
        return saved_villager;
    }
}
