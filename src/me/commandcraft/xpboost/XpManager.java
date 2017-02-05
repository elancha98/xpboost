package me.commandcraft.xpboost;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.commandcraft.xpboost.configuration.XpBoost;

public class XpManager extends BukkitRunnable implements Listener {
	
	@EventHandler
	public void onXpGetted(PlayerExpChangeEvent event) {
		double amount = event.getAmount();
		if (amount > 0) {
			List<XpBoost> boosts = Main.config.getActiveBoost(event.getPlayer().getName());
			for (XpBoost boost : boosts) {
				amount *= boost.getMultiplier();
			}
			event.setAmount((int) amount);
		}
	}

	@Override
	public void run() {
		Main.config.run();
	}

}
