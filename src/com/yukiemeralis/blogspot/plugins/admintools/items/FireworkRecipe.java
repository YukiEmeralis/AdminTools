package com.yukiemeralis.blogspot.plugins.admintools.items;

import java.util.Arrays;

import com.yukiemeralis.blogspot.plugins.admintools.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class FireworkRecipe 
{
    private final ItemStack final_item = new ItemStack(Material.HEART_OF_THE_SEA);
    {
        ItemMeta meta = final_item.getItemMeta();
        meta.setDisplayName("§aOmega drive");
        meta.setLore(Arrays.asList(new String[] {"§fPress [MOUSE 2] while flying with an", "§felytra to propel yourself forward."}));

        final_item.setItemMeta(meta);
    }

    public void register()
    {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "omega_drive");
        ShapedRecipe recipe = new ShapedRecipe(key, final_item);

        recipe.shape("FSF", "FDF", "FSF");
        
        recipe.setIngredient('F', Material.FIREWORK_ROCKET);
        recipe.setIngredient('S', Material.SHULKER_SHELL);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);

        Bukkit.addRecipe(recipe);
    }    
}
