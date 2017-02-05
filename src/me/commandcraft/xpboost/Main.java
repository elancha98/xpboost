package me.commandcraft.xpboost;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.commandcraft.xpboost.configuration.Configuration;

public class Main extends JavaPlugin {
	
	public static Configuration config;
	public static JavaPlugin instance;
	public static XpManager manager;
	
	public void onEnable() {
		manager = new XpManager();
		instance = this;
		config = new Configuration(getDataFolder());
		Bukkit.getPluginManager().registerEvents(manager, this);
		manager.runTaskTimerAsynchronously(instance, 0L, 20L);
	}
	
	public void onDisable() {
		try {
			config.save(getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("xpboost")) {
			CommandManager.process(sender, args);
		}
		return true;
	}
	
	public static void registerEvents(BoostGui gui) {
		Bukkit.getPluginManager().registerEvents(gui, instance);
	}
}
