package com.kickass.MCDuel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.kickass.MCDuel.Arena.ArenaManager;
import com.kickass.MCDuel.Commands.CommandHandler;
import com.kickass.MCDuel.Duel.OutOfBoundsHandler;
import com.kickass.MCDuel.Listeners.ListenerManager;
import com.kickass.MCDuel.Utils.VaultUtils;

public class MCDuel extends JavaPlugin {

	@Override
	public void onEnable() {
		instance = this;

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ArenaManager(), 0, 6);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new OutOfBoundsHandler(), 0, 10);
		ListenerManager.initialize(this);
		VaultUtils.initialize(this);
		getCommand("duel").setExecutor(new CommandHandler());
	}

	public static MCDuel getInstance() {
		return instance;
	}

	private static MCDuel instance;

}
