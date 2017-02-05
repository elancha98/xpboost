package me.commandcraft.xpboost;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.commandcraft.xpboost.configuration.XpBoost;

public class BoostGui implements Listener {

	private Inventory inventory;
	
	public BoostGui(Player player) {
		inventory = Bukkit.createInventory(player, 9);
		List<XpBoost> boosts = Main.config.getNonActiveBoost(player.getName());
		int i = 0;
		for (XpBoost boost : boosts) {
			ItemStack item = Utils.getBottle(boost);
			inventory.setItem(i++, item);
		}
		player.openInventory(inventory);
		Main.registerEvents(this);
	}
	
	@EventHandler
	public void onPlayerClick(InventoryClickEvent event) {
		if (event.getClickedInventory().equals(inventory)) {
			int id = Utils.getId(event.getCurrentItem());
			Main.config.activateBoost(id);
			event.getWhoClicked().closeInventory();
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory)) {
			InventoryClickEvent.getHandlerList().unregister(this);
			InventoryCloseEvent.getHandlerList().unregister(this);
			inventory = null;
		}
	}
}
