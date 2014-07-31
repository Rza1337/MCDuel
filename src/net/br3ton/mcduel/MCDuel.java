package net.br3ton.mcduel;

import org.bukkit.plugin.java.JavaPlugin;

public class MCDuel extends JavaPlugin {

	public void onEnable() {
		instance = this;
		getCommand("duel").setExecutor(new CommandHandler());
		ListenerManager.initialize(this);
	}

	public void onDisable() {

	}

	public static MCDuel instance;

}
