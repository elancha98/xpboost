package me.commandcraft.xpboost.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.commandcraft.xpboost.Utils;

public class Configuration {

	List<XpBoost> config;

	public Configuration(File folder) {
		try {
			String file = Utils.readFile(folder.getPath() + "/players.txt", Charset.defaultCharset());
			String[] split = file.split("\\*");
			if (split.length == 0) {
				config = new ArrayList<XpBoost>();
				XpBoost.setMaxId(0);
			} else {
				Type type = new TypeToken<List<XpBoost>>(){}.getType();
				config = new Gson().fromJson(split[0], type);
				XpBoost.setMaxId(Integer.parseInt(split[1]));
			}
		} catch (Exception e) {
			config = new ArrayList<XpBoost>();
			XpBoost.setMaxId(0);
		}
	} 	

	public void save(File folder) throws IOException {
		Gson gson = new Gson();
		folder.mkdirs();
		String json = gson.toJson(config);
		File file = new File(folder, "players.txt");
		if (!file.exists()) file.createNewFile();
		FileWriter writer;
		writer = new FileWriter(file);
		writer.write(json + "*" + XpBoost.getMaxId());
		writer.close();

	}

	public List<XpBoost> getBoost(String player, boolean active) {
		List<XpBoost> ret = new ArrayList<XpBoost>();
		for (XpBoost boost : config) {
			if (boost.isActive() == active && boost.getPlayer().equals(player))
				ret.add(boost);
		}
		return ret;
	}

	public List<XpBoost> getActiveBoost(String player) {
		return getBoost(player, true);
	}

	public List<XpBoost> getNonActiveBoost(String player) {
		return getBoost(player, false);
	}
	
	public void activateBoost(int id) {
		for (XpBoost boost : config) {
			if (boost.equals(id)) {
				boost.setActive();
				return;
			}
		}
	}
	
	public void clearBoosts(String player) {
		for (int i = config.size()-1; i >= 0; i--) {
			if (config.get(i).getPlayer().equals(player)) config.remove(i);
		}
	}
	
	public boolean addBoost(String player, double multiplier, int time) {
		List<XpBoost> boosts = getNonActiveBoost(player);
		if (boosts.size() == 9) return false;
		config.add(new XpBoost(player, multiplier, time));
		return true;
	}

	public void run() {
		for (XpBoost boost : config) {
			if (boost.run()) config.remove(boost);
		}
	}
}
