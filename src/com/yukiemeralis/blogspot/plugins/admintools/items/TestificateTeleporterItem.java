package com.yukiemeralis.blogspot.plugins.admintools.items;

import java.util.Arrays;

import com.yukiemeralis.blogspot.plugins.admintools.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class TestificateTeleporterItem 
{
    public static ItemStack item = new ItemStack(Material.EMERALD);

    static {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aTestificate transmitter");
        meta.setLore(Arrays.asList(new String[] {
            "§fRight-click on a villager to mark them for teleportation.", 
            "§fLeft-click on a block to bring an uploaded villager",
            "§fto the block's location."
        }));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    }

    public void register()
    {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "villager_teleporter");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("CPB", "EEE", "ReW");

        recipe.setIngredient('e', Material.EMERALD);
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        recipe.setIngredient('C', Material.GOLDEN_CARROT);
        recipe.setIngredient('B', Material.BOOK);
        recipe.setIngredient('R', Material.ROTTEN_FLESH);
        recipe.setIngredient('W', Material.WHEAT);
        recipe.setIngredient('P', Material.PUMPKIN);

        Bukkit.addRecipe(recipe);
    }
}
