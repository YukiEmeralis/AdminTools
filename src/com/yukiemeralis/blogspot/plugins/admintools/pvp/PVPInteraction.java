package com.yukiemeralis.blogspot.plugins.admintools.pvp;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PVPInteraction 
{
    ItemStack aggressor_item;
    ItemStack defender_item;    

    Player aggressor;
    Player defender;

    double damageDealt;

    int aggressorHP;
    int defenderHP;

    int interactionID;

    public PVPInteraction(Player aggressor, Player defender, double damageDealt, int interactionID)
    {
        this.aggressor = aggressor;
        this.defender = defender;
        this.interactionID = interactionID;

        this.damageDealt = damageDealt;
        this.aggressorHP = (int) Math.round(aggressor.getHealth());
        this.defenderHP = (int) Math.round(defender.getHealth());

        this.aggressor_item = aggressor.getInventory().getItemInMainHand();
        this.defender_item = defender.getInventory().getItemInMainHand();
    }

    public ItemStack getAggressorItem()
    {
        return aggressor_item;
    }

    public ItemStack getDefenderItem()
    {
        return defender_item;
    }

    public int getInteractionID()
    {
        return interactionID;
    }

    public Player getAggressor()
    {
        return aggressor;
    }

    public Player getDefender()
    {
        return defender;
    }

    public int getAggressorHP()
    {
        return aggressorHP;
    }

    public int getDefenderHP()
    {
        return defenderHP;
    }

    public double getDamageDealt()
    {
        return damageDealt;
    }
}
