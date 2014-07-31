package com.kickass.MCDuel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.kickass.MCDuel.Arena.ArenaManager;
import com.kickass.MCDuel.Commands.CommandHandler;
import com.kickass.MCDuel.Listeners.ListenerManager;

public class MCDuel extends JavaPlugin {

	@Override
	public void onEnable() {
		instance = this;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ArenaManager(), 0, 0);
		ListenerManager.initialize(this);
		getCommand("duel").setExecutor(new CommandHandler());
	}
	
	public static MCDuel getInstance() {
		return instance;
	}
	
	private static MCDuel instance;

}
