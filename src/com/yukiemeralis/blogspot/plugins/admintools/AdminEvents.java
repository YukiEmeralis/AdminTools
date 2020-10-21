package com.yukiemeralis.blogspot.plugins.admintools;

import java.util.Arrays;
import java.util.Random;

import com.yukiemeralis.blogspot.plugins.admintools.pvp.PVPLog;
import com.yukiemeralis.blogspot.plugins.admintools.pvp.PVPPlayersInvolved;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class AdminEvents implements Listener
{
    // All the events we want to listen for   
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        // Ensure we always have data for this player
        String UUID = event.getPlayer().getUniqueId().toString();

        if (!AdminData.accounts.containsKey(UUID)) 
            AdminData.accounts.put(UUID, new PlayerAccount(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        System.out.println(event.getEntity().getName() + " died with " + event.getDrops().size() + " items!");

        if (event.getDrops().size() == 0)
            return; // No need to save an empty inventory

        AdminData.accounts.get(event.getEntity().getUniqueId().toString()).addDeath(event.getEntity(), event);
        System.out.println("Registered death as " + (AdminData.accounts.get(event.getEntity().getUniqueId().toString()).getDeaths().size()-1) + "!");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        if (AdminData.restart_requested)
            if (Bukkit.getOnlinePlayers().size() == 1)
            {
                System.out.println("AdminTools is shutting down the server, as a restart was requested...");
                Bukkit.shutdown();
            }
                
    }

    //
    // PVP
    //

    @EventHandler
    public void playerHitPlayer(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;

        Player aggressor = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        PlayerAccount agg_act = AdminData.accounts.get(aggressor.getUniqueId().toString());
        PlayerAccount dam_act = AdminData.accounts.get(damaged.getUniqueId().toString());

        if (!agg_act.getHasPVP() && !dam_act.getHasPVP())
        {
            aggressor.sendMessage("§cYou have PVP disabled!");
            damaged.sendMessage("§c" + aggressor.getName() + " attempted to attack you but they have PVP disabled!");
            event.setCancelled(true);
            return;
        }

        // Log PVP
        PVPPlayersInvolved involved = new PVPPlayersInvolved(aggressor, damaged);
        PVPLog log = null;

        for (PVPPlayersInvolved key : AdminData.active_pvp_logs.keySet())
        {
            if (key.equals(involved))
                log = AdminData.active_pvp_logs.get(key);
        }

        if (log == null)
        {
            log = new PVPLog(involved);
            AdminData.active_pvp_logs.put(involved, log);
            log.start();
        }

        log.addInteraction(event);
    }

    //
    // Omega drive listener
    //
    @EventHandler
    public void omegaDriveUse(PlayerInteractEvent event)
    {
        Player target = event.getPlayer();
        if (target.getInventory().getItemInMainHand() == null) 
            return;
        if (target.getInventory().getItemInOffHand() == null)
            return;

        try {
            if (!target.getInventory()
                .getItemInMainHand()
                .getItemMeta()
                .getDisplayName()
                .equals("§aOmega drive"))
            return;
        } catch (NullPointerException error) {return;}
        
        if (!target.isGliding())
            return;

        target.setVelocity(target.getLocation().getDirection().multiply(2));
        target.playSound(
            target.getLocation(), 
            Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 
            SoundCategory.MASTER, 
            1.0f, 
            1.0f
        );
    }

    Random random = new Random();
    public static ItemStack salmon_weapon = new ItemStack(Material.SALMON);
    static {
        ItemMeta meta = salmon_weapon.getItemMeta();
        meta.setDisplayName("§aSmoked salmon");
        meta.setLore(Arrays.asList(new String[] {"§fPress [MOUSE 2] with the fish to launch a fireball!", "§4Don't eat it!"}));
        salmon_weapon.setItemMeta(meta);

        salmon_weapon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
    }

    //
    // Smoked salmon listeners
    //
    @EventHandler
    public void fishEvent(PlayerFishEvent event)
    {
        if (event.getCaught() == null)
            return;

        // 1/1000 | 500 | 333 | 250 chance
        int bound = 1000;
        ItemStack rod = event.getPlayer().getInventory().getItemInMainHand();

        if (rod.getEnchantments().containsKey(Enchantment.LUCK))
        {
            bound = 1000 / (rod.getEnchantmentLevel(Enchantment.LUCK) + 1);
        }

        if (random.nextInt(bound) != 0)
            return;
            
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("§eA smoked salmon has been born into the world!");
        });
        ((Item) event.getCaught()).setItemStack(salmon_weapon);
    }

    @EventHandler
    public void eatEvent(PlayerItemConsumeEvent event)
    {
        if (!event.getItem().equals(salmon_weapon))
            return;

        event.getPlayer().sendMessage("§4You have incurred the wrath of ancient deities! (Now is a good time to run to a place where you don't mind a large explosion.)");

        new BukkitRunnable(){
            @Override
            public void run() {
                event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 6);
            }
        }.runTaskLater(Main.getInstance(), 20*15);
    }

    @EventHandler
    public void swingSalmonEvent(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(Action.LEFT_CLICK_AIR) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;
        
        Player target = event.getPlayer();
        if (target.getInventory().getItemInMainHand() == null) 
            return;
        if (target.getInventory().getItemInOffHand() == null)
            return;

        if (target.getInventory().getItemInMainHand().equals(salmon_weapon))
        {
            launchFireball(target);
            return;
        }
    }

    private void launchFireball(Player origin)
    {
        if (AdminData.accounts.get(origin.getUniqueId().toString()).getSalmonCooldown())
        {
            origin.sendMessage("§cYou must wait a moment before using the fishes' power again!");
        } else {
            origin.launchProjectile(Fireball.class);
            AdminData.accounts.get(origin.getUniqueId().toString()).triggerSalmonCooldown();
        }
        
    }
}
