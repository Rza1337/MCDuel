package net.br3ton.mcduel;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager {

	public static void initialize(MCDuel r) {
		for (Listener l : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(l, r);
		}
	}

	private static ArrayList<Listener> listeners = new ArrayList<Listener>();

	static {
		listeners.add(new DuelManager());
		listeners.add(new CommandListener());
	}

}
