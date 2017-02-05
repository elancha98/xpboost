package me.commandcraft.xpboost;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.commandcraft.xpboost.configuration.XpBoost;

public class Utils {

	public static ItemStack bottle = new ItemStack(Material.EXP_BOTTLE);

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static Class<?> getOBCClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Class<?> getCraftItem() {
		return getOBCClass("inventory.CraftItemStack");
	}
	
	public static Class<?> getItem() {
		return getNMSClass("ItemStack");
	}
	
	public static Class<?> getNBT() {
		return getNMSClass("NBTTagCompound");
	}
	
	public static ItemStack getBottle(XpBoost boost) {
		ItemStack item = null;
		try {
			Object itemStack = getCraftItem().getMethod("asNMSCopy", ItemStack.class).invoke(null, bottle);
			Object nbt = getNBT().getConstructor().newInstance();
			nbt.getClass().getMethod("setInt", String.class, int.class).invoke(nbt, "xp_id", boost.getId());
			getItem().getMethod("setTag", getNBT()).invoke(itemStack, nbt);
			item = (ItemStack) getCraftItem().getMethod("asBukkitCopy", getItem()).invoke(null, itemStack);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Duration: " + boost.getTimeLeft() + "s");
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.GREEN + "XP x" + boost.getMultiplier());
		item.setItemMeta(meta);
		return item;
	}
	
	public static int getId(ItemStack item) {
		try {
			Object itemStack = getCraftItem().getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
			Object nbt = getItem().getMethod("getTag").invoke(itemStack);
			return (int) getNBT().getMethod("getInt", String.class).invoke(nbt, "xp_id");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
