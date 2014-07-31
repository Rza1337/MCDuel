package com.kickass.MCDuel.Arena;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.kickass.MCDuel.Duel.Duel;

public class ArenaManager extends BukkitRunnable {

	@Override
	public void run() {
		for(Duel d : arenas.keySet()) {
			if(d.hasStarted()) {
				Arena a = arenas.get(d);
				for(Player p : d.getLivingPlayers()) {
					a.render(p);
				}
			}
		}
	}
	
	public static void addArena(Duel duel, Arena arena) {
		arenas.put(duel, arena);
	}
	
	public static void removeArena(Duel duel) {
		arenas.remove(duel);
	}
	
	public static Arena getArena(Duel duel) {
		return arenas.get(duel);
	}
	
	private static final HashMap<Duel, Arena> arenas = new HashMap<Duel, Arena>();
	
}
