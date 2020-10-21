package com.yukiemeralis.blogspot.plugins.admintools.pvp;

import java.util.HashMap;

import com.yukiemeralis.blogspot.plugins.admintools.AdminData;
import com.yukiemeralis.blogspot.plugins.admintools.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PVPLog {
    HashMap<Integer, PVPInteraction> log = new HashMap<Integer, PVPInteraction>();
    PVPPlayersInvolved involved;

    boolean active = true;

    BukkitTask pvp_timer;

    public PVPLog(PVPPlayersInvolved involved)
    {
        this.involved = involved;

        pvp_timer = new BukkitRunnable() {
            @Override
            public void run() 
            {
                System.out.println("PVP ended. Reason: time out");
                System.out.println("----[ PVP End! ]----");
                
                stop();
            }
        }.runTaskLater(Main.getInstance(), 60*20);
    }

    /**
     * Adds an interaction to the list.
     * @param event
     */
    public void addInteraction(EntityDamageByEntityEvent event) {
        for (int i = 0; true; i++) 
        {
            if (!log.containsKey(i)) {
                PVPInteraction action = generateInteraction(event, i);
                System.out.println(
                    "[Action " + (i + 1) + "] " + action.getAggressor().getName() + " (" + action.aggressorHP + " HP) struck " +
                    action.getDefender().getName() + "! (" + action.getDefenderHP() + " HP, " + action.getDamageDealt() + " damage)"
                );
                log.put(i, action);
                return;
            }
        }
    }

    /**
     * Generates an interaction based on an event
     * @param event
     * @param id
     * @return
     */
    private PVPInteraction generateInteraction(EntityDamageByEntityEvent event, int id) {
        Player aggressor = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();

        pvp_timer.cancel();

        pvp_timer = new BukkitRunnable() {
            @Override
            public void run() 
            {
                System.out.println("PVP ended. Reason: time out");
                System.out.println("----[ PVP End! ]----");
                
                stop();
            }
        }.runTaskLater(Main.getInstance(), 60*20);
        
        return new PVPInteraction(aggressor, defender, event.getDamage(), id);
    }

    public HashMap<Integer, PVPInteraction> getLog()
    {
        return log;
    }

    public PVPPlayersInvolved getPlayers()
    {
        return involved;
    }

    public void start()
    {
        System.out.println("----[ PVP Begin! ]----");
        AdminData.active_pvp_logs.put(involved, this);
    }

    public void stop()
    {
        active = false;
        AdminData.registerFinalPVPLog(involved);
    }
}
