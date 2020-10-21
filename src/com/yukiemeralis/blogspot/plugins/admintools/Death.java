package com.yukiemeralis.blogspot.plugins.admintools;

import java.util.Base64;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class Death 
{
    private Player player;
    private ItemStack[] inventory;  

    private int level;
    private float experience;
    private Location deathLocation;
    private String deathMessage;

    private int id;

    // Settings
    private boolean redeemedByPlayer = false;
    private boolean redeemByPlayerValid = false;

    // Log information
    private int ping;
    
    public Death(Player player, PlayerDeathEvent event, int id)
    {
        this.player = player;
        this.level = player.getLevel();
        this.experience = event.getDroppedExp();
        this.inventory = new ItemStack[event.getDrops().size()];
        this.deathMessage = event.getDeathMessage();

        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (Exception error) {
            ping = -1;
        }

        for (int i = 0; i < event.getDrops().size(); i++)
        {
            this.inventory[i] = event.getDrops().get(i);
        }

        this.deathLocation = player.getLocation();

        this.id = id;
        event.getEntity().sendMessage("§aRIP. Your inventory, exp, and location are saved. Your death ticket number is " + id + ".");
        System.out.println("Player's ping is " + ping);

        if (ping > 400)
        {
            offerDeathTicket();
        }
    }

    public void restore()
    {
        player.sendMessage("§6Restoring inventory and experience... (ID " + id + ")");
        player.setLevel(level);
        //player.setExp(experience);

        for (ItemStack i : inventory)
        {
            player.getWorld().dropItem(player.getLocation(), i);
        }
    }

    public void restoreLocation()
    {
        player.sendMessage("§6Teleporting to your death location... (ID " + id + ")");
        player.teleport(deathLocation);
    }

    public void restoreByPlayer()
    {
        if (redeemedByPlayer)
        {
            player.sendMessage("§cThis death ticket has already been redeemed! Ask Hailey if you think this is a mistake.");
            return;
        }
        redeemedByPlayer = true;
        restore();
    }

    public void restoreLocationByPlayer()
    {
        if (redeemedByPlayer)
        {
            player.sendMessage("§cThis death ticket has already been redeemed! Ask Hailey if you think this is a mistake.");
            return;
        }
        redeemedByPlayer = true;
        restoreLocation();
    }

    /**
     * If the server's TPS is low, or the player's ping is high, offer them a chance to redeem their
     * death ticket on their own. Can only be used once per ticket.
     */
    private void offerDeathTicket()
    {
        player.sendMessage("§bHigh ping detected. You may use [/at redeem reg " + id + "] to restore your inventory, or [/at redeem loc " + id + "] to restore your location.");
        this.redeemByPlayerValid = true;
    }

    // Getters
    public Location getLocation()
    {
        return deathLocation;
    }

    public ItemStack[] getDrops()
    {
        return inventory;
    }

    public int getLevels()
    {
        return level;
    }

    public boolean canBeRedeemedByPlayer()
    {
        return redeemByPlayerValid;
    }

    public int getID()
    {
        return id;
    }

    public int getPing()
    {
        return ping;
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getDeathMessage()
    {
        return deathMessage;
    }

    private String getInventoryAsString()
    {
        StringBuilder builder = new StringBuilder();
        ItemStack item;

        for (int i = 0; i < inventory.length; i++)
        {
            item = inventory[i];
            builder.append(item.getType() + ":" + item.getAmount());

            if (i != inventory.length -1)
                builder.append(",");
        }

        String inv = builder.toString();

        return Base64.getEncoder().encodeToString(inv.getBytes());
    }
}
