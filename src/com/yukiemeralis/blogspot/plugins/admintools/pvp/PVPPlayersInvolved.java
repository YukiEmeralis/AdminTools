package com.yukiemeralis.blogspot.plugins.admintools.pvp;

import org.bukkit.entity.Player;

public class PVPPlayersInvolved 
{
    private Player player1;
    private Player player2;
    
    public PVPPlayersInvolved(Player player1, Player player2)
    {
        this.player1 = player1;
        this.player2 = player2;


    }

    public Player getPlayer1()
    {
        return this.player1;
    }

    public Player getPlayer2()
    {
        return this.player2;
    }

    public void destroy()
    {
        this.player1 = null;
        this.player2 = null;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        if (!(o instanceof PVPPlayersInvolved))
            return false;

        PVPPlayersInvolved ppi = (PVPPlayersInvolved) o;

        if (this.player1.getName().equals(ppi.getPlayer1().getName()) && this.player2.getName().equals(ppi.getPlayer2().getName()))
        {
            return true;
        }
        return false;
    }
}
