package com.yukiemeralis.blogspot.plugins.admintools;

import com.yukiemeralis.blogspot.plugins.admintools.pvp.PVPLog;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AdminCommand implements CommandExecutor 
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        if (args.length == 0)
                return false;

        String subcmd = args[0];

        // Command variables
        Player target;
        int target_index;
        String uuid;
        PlayerAccount account;
        Death death;
        PVPLog log;

        if (subcmd == null)
            return false;

        // Admin commands
        switch(subcmd)
        {
            case "restore":
                // /at restore <player> <death index>
                if (!verifyYuki(sender))
                {
                    sender.sendMessage("§cOnly Yuki_emeralis can use this command!");
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);

                target_index = Integer.parseInt(args[2]);
                AdminData.accounts.get(target.getUniqueId().toString()).getDeaths().get(target_index).restore();

                break;
            case "restoreloc":
                // /at restoreloc <player> <death index>
                if (!verifyYuki(sender))
                {
                    sender.sendMessage("§cOnly Yuki_emeralis can use this command!");
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);

                target_index = Integer.parseInt(args[2]);
                AdminData.accounts.get(target.getUniqueId().toString()).getDeaths().get(target_index).restoreLocation();

                break;
            case "trees":
                // /at trees
                uuid = Bukkit.getPlayer(sender.getName()).getUniqueId().toString();

                AdminData.accounts.get(uuid).setTreecapitator(!AdminData.accounts.get(uuid).getHasTreecap());

                if (AdminData.accounts.get(uuid).getHasTreecap())
                {
                    sender.sendMessage("§aEnabled treecapitator!");
                } else {
                    sender.sendMessage("§aDisabled treecapitator!");
                }
                break;
            case "redeem":
                //
                // This one is meant to be used by players. So we need to idiot-proof it.
                //

                uuid = Bukkit.getPlayer(sender.getName()).getUniqueId().toString();
                account = AdminData.accounts.get(uuid);
                int ticketNo;

                // Check for arguments,
                try {
                    ticketNo = Integer.parseInt(args[2]);
                } catch (ArrayIndexOutOfBoundsException error) {
                    sender.sendMessage("§cNot enough arguments. Usage: /at redeem <reg | loc> <ticket ID>");
                    return true;
                }
                
                Death d = account.getDeaths().get(ticketNo);

                // Make sure the death is valid,
                if (d == null)
                {
                    sender.sendMessage("§cInvalid ticket! Most recent ticket: " + (account.getDeaths().size()-1));
                    return true;
                }

                // Check if the sub command is valid,
                String subarg = args[1];
                if (!subarg.equals("loc") && !subarg.equals("reg"))
                {
                    sender.sendMessage("§cInvalid subcommand. Valid options are 'reg' and 'loc'.");
                    return true;
                }

                // Make sure the ticket can be redeemed by a player,
                if (!d.canBeRedeemedByPlayer())
                {
                    sender.sendMessage("§cThis ticket cannot be redeemed by a player!");
                    return true;
                }

                // And restore their pre-death information.
                if (subarg.equals("loc"))
                {
                    d.restoreLocationByPlayer();
                    return true;
                } else {
                    d.restoreByPlayer();
                    return true;
                }
            case "info":
                if (!verifyYuki(sender))
                {
                    sender.sendMessage("§cOnly Yuki_emeralis can use this command!");
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);
                target_index = Integer.parseInt(args[2]);
                death = AdminData.accounts.get(target.getUniqueId().toString()).getDeaths().get(target_index);

                sender.sendMessage("§6-----[§a Death " + target_index + " §6]-----");
                sender.sendMessage("§6Player: §e" + death.getPlayer().getName());
                sender.sendMessage("§6Death ID: §e" + death.getID());
                sender.sendMessage("§6Item count: §e" + death.getDrops().length);
                sender.sendMessage("§6Levels: §e" + death.getLevels());
                sender.sendMessage("§6Location: §e" + death.getLocation().getX() + ", " + death.getLocation().getY() + ", " + death.getLocation().getZ());
                sender.sendMessage("§6World: §e" + death.getLocation().getWorld().getName());
                sender.sendMessage("§6Message: §e" + death.getDeathMessage());
                sender.sendMessage("§6Ping: §e" + death.getPing());
                sender.sendMessage("§6Redeemable?: §e" + death.canBeRedeemedByPlayer());
                
                break;
            case "pvp":
                uuid = Bukkit.getPlayer(sender.getName()).getUniqueId().toString();
                account = AdminData.accounts.get(uuid);
                
                account.togglePVP();

                if (account.getHasPVP())
                {
                    sender.sendMessage("§cYou have enabled PVP!");
                } else {
                    sender.sendMessage("§aYou have disabled PVP!");
                }

                break;
            case "update":
                // Start telling players that the plugin needs to update.
                if (!verifyYuki(sender))
                {
                    sender.sendMessage("§cOnly Yuki_emeralis can use this command!");
                    return true;
                }

                new BukkitRunnable()
                {
					@Override
                    public void run() 
                    {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.sendMessage("§6[ SERVER ] Hailey would like to restart the server to update the plugin.");
                        });
					}   
                }.runTaskTimer(Main.getInstance(), 0L, (long) 20*300);
                AdminData.restart_requested = true;
                break;
            case "givesalmon":
                if (!verifyYuki(sender))
                {
                    sender.sendMessage("§cOnly Yuki_emeralis can use this command!");
                    return true;
                }

                target = Bukkit.getPlayer(sender.getName());
                target.getInventory().addItem(AdminEvents.salmon_weapon);

                break;
            case "skipnight":
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    if (p.isSleeping())
                    {
                        p.getWorld().setTime(0);
                        break;
                    }
                }
                break;
            case "pvpinfo":
                if (!verifyYuki(sender))
                {
                    sender.sendMessage("§cOnly Yuki_emeralis can use this command!");
                    return true;
                }
                target_index = Integer.parseInt(args[1]);

                log = AdminData.inactive_pvp_logs.get(target_index);

                if (log == null)
                {
                    sender.sendMessage("§c" + target_index + " is not a valid PVP log!");
                    return true;
                }

                sender.sendMessage("§6-----[§a PVP log " + target_index + " §6]-----");
                sender.sendMessage("§6Between: §e" + log.getPlayers().getPlayer1().getName() + " vs. " + log.getPlayers().getPlayer2().getName());
                for (int i = 0; i < log.getLog().size(); i++)
                {
                    sender.sendMessage(
                        "§6[Turn " + (i + 1) + "]§e: " + log.getLog().get(i).getAggressor().getName() + " (" + log.getLog().get(i).getAggressorHP() + " HP) -> " +
                        log.getLog().get(i).getDefender().getName() + " (" + log.getLog().get(i).getDefenderHP()  + " HP, " + log.getLog().get(i).getDamageDealt() +
                        " damage)"
                    );
                    sender.sendMessage("§6[Turn " + (i + 1) + "]§e: Holding: " + log.getLog().get(i).getAggressorItem().getType().toString().toLowerCase());
                }
                sender.sendMessage("§6-----[§a End PVP §6]-----");

                break;
            default:
                sender.sendMessage("§c" + subcmd + " is not a recognized subcommand!");
                break;
        }

        return true;
    }
    
    private boolean verifyYuki(CommandSender sender)
    {
        if (Bukkit.getPlayer(sender.getName()).getUniqueId().toString().equals("5370de43-f1cc-44ed-9c69-c76f1af984c6"))
            return true;
        return false;
    }
}
