package com.yukiemeralis.blogspot.plugins.admintools.listeners;

import com.yukiemeralis.blogspot.plugins.admintools.AdminData;
import com.yukiemeralis.blogspot.plugins.admintools.PlayerAccount;
import com.yukiemeralis.blogspot.plugins.admintools.items.TestificateTeleporterItem;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VillagerTeleport implements Listener
{
    // Villager -> Data
    @EventHandler
    public void villagerInteract(PlayerInteractAtEntityEvent event)
    {
        if (!event.getRightClicked().getType().equals(EntityType.VILLAGER))
            return;
        ItemStack main_held = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack side_held = event.getPlayer().getInventory().getItemInOffHand();

        if (main_held == null)
            return;
        if (side_held == null)
            return;

        if (main_held.equals(TestificateTeleporterItem.item) || side_held.equals(TestificateTeleporterItem.item))
        {
            event.setCancelled(true);

            PlayerAccount act = AdminData.accounts.get(event.getPlayer().getUniqueId().toString());

            if (act.getVillager() != null)
            {
                event.getPlayer().sendMessage("§cYou already have a villager marked!");
                return;
            }

            act.setVillager(event.getRightClicked());

            Entity e = event.getRightClicked();
            e.setInvulnerable(true);

            event.getPlayer().sendMessage("§aMarked villager for transportation!");
        }
    }

    // Data -> Villager
    @EventHandler
    public void blockInteract(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;

        ItemStack main_held = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack side_held = event.getPlayer().getInventory().getItemInOffHand();

        if (main_held == null)
            return;
        if (side_held == null)
            return;

        if (main_held.equals(TestificateTeleporterItem.item) || side_held.equals(TestificateTeleporterItem.item))
        {
            event.setCancelled(true);

            PlayerAccount act = AdminData.accounts.get(event.getPlayer().getUniqueId().toString());

            if (act.getVillager() == null)
            {
                event.getPlayer().sendMessage("§cYou don't have a villager marked!");
                return;
            }

            Location target = event.getClickedBlock().getLocation();
            act.getVillager().teleport(target.add(0, 1, 0));

            event.getPlayer().sendMessage("§aTeleported marked villager!");
            act.setVillager(null);
        }
    }
}
