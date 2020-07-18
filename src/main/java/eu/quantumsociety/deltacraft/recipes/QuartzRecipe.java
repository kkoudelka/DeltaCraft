package eu.quantumsociety.deltacraft.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class QuartzRecipe {

    public static void registerRecipe(JavaPlugin plugin) {
        ItemStack item = new ItemStack(Material.QUARTZ, 4);

        NamespacedKey key = new NamespacedKey(plugin, "quartz");

        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("Q");
        recipe.setIngredient('Q', Material.QUARTZ_BLOCK);

        Bukkit.addRecipe(recipe);
    }
}
