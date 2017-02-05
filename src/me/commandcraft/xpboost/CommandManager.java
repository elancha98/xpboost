package me.commandcraft.xpboost;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.commandcraft.xpboost.configuration.XpBoost;

public class CommandManager {

	public static void process(CommandSender sender, String[] args) {
		switch (args.length) {
		case 0:
			if (sender instanceof Player) {
				new BoostGui((Player) sender);
			} else {
				sender.sendMessage(ChatColor.RED + "only players can do that");
			}
			break;
		case 1:
			if (args[0].equalsIgnoreCase("time")) {
				List<XpBoost> boosts = Main.config.getActiveBoost(sender.getName());
				for (XpBoost boost : boosts) {
					sender.sendMessage(ChatColor.GREEN + "Modifier: x" + boost.getMultiplier() + ", time left: " + boost.getTimeLeft());
				}
			} else
				usage(sender);
			break;
		case 2:
			if (args[0].equals("clear") && sender.hasPermission("xpboost.admin")) {
				Player player = Bukkit.getPlayer(args[1]);
				if (player != null) {
					Main.config.clearBoosts(player.getName());
				} else sender.sendMessage(ChatColor.RED + "That's not a player");
			} else usage(sender);
			break;
		case 4:
			if (args[0].equals("give") && sender.hasPermission("xpboost.admin")) {
				Player player = Bukkit.getPlayer(args[1]);
				double d;
				int time;
				try {
					d = Double.parseDouble(args[2]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "enter a valid multiplier");
					return;
				}
				try {
					time = Integer.parseInt(args[3]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "enter a valid time");
					return;
				}
				if (player != null) {
					boolean result = Main.config.addBoost(player.getName(), d, time);
					if (result == false) sender.sendMessage(ChatColor.RED + "player has too many boosts");
				} else sender.sendMessage(ChatColor.RED + "That's not a player");
			} else usage(sender);
			break;
		default:
			usage(sender);
			break;
		}
	}

	public static void usage(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "usage:");
		sender.sendMessage(ChatColor.GREEN + " - /xpboost " + ChatColor.YELLOW + "to open a GUI with your boosts");
		sender.sendMessage(
				ChatColor.GREEN + " - /xpboost time " + ChatColor.YELLOW + "to get time left to end your boosts");
		if (sender.hasPermission("xpboost.admin")) {
			sender.sendMessage(ChatColor.GREEN + " - /xpboost give <player> <multiplier> <time> " + ChatColor.YELLOW
					+ "to give a player a boost");
			sender.sendMessage(
					ChatColor.GREEN + " - /xpboost clear <player> " + ChatColor.YELLOW + "to clear all player boosts");
		}
	}
}
