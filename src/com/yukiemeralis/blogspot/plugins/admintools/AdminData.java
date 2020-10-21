package com.yukiemeralis.blogspot.plugins.admintools;

import java.util.HashMap;

import com.yukiemeralis.blogspot.plugins.admintools.pvp.PVPLog;
import com.yukiemeralis.blogspot.plugins.admintools.pvp.PVPPlayersInvolved;

public class AdminData 
{
    // Boilerplate code
    public static HashMap<String, PlayerAccount> accounts = new HashMap<>();
    public static boolean restart_requested = false;

    public static HashMap<PVPPlayersInvolved, PVPLog> active_pvp_logs = new HashMap<>();
    public static HashMap<Integer, PVPLog> inactive_pvp_logs = new HashMap<>();

    public static void registerFinalPVPLog(PVPPlayersInvolved involved)
    {
        for (int i = 0; true; i++)
        {
            if (!inactive_pvp_logs.containsKey(i))
            {
                inactive_pvp_logs.put(i, active_pvp_logs.get(involved));
                active_pvp_logs.remove(involved);
                System.out.println("Saved PVP log as " + i);
                break;
            }
        }
    }
}
