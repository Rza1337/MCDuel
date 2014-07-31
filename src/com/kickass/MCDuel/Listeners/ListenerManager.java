package com.kickass.MCDuel.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.kickass.MCDuel.MCDuel;

public class ListenerManager {

	public static void initialize(MCDuel r) {
		for (Listener l : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(l, r);
		}
	}

	private static ArrayList<Listener> listeners = new ArrayList<Listener>();

	static {
		listeners.add(new CommandListener());
		listeners.add(new DuelListener());
	}

}
