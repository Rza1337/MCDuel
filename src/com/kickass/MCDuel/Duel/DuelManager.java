package com.kickass.MCDuel.Duel;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class DuelManager {
	
	public static void addDuel(Duel duel) {
		for(Player player : duel.getPlayers()) {
			duels.put(player.getUniqueId(), duel);
		}
	}
	
	public static void removeDuel(Player player) {
		removeDuel(player.getUniqueId());
	}
	
	public static void removeDuel(UUID uuid) {
		Duel duel = duels.get(uuid);
		if(uuid != null) {
			removeDuel(duel);
		}
	}
	
	public static void removeDuel(Duel duel) {
		for(Player player : duel.getPlayers()) {
			duels.remove(player.getUniqueId());
		}
	}
	
	public static boolean isPlayerDueling(Player p) {
		return isPlayerDueling(p.getUniqueId());
	}
	
	public static boolean isPlayerDueling(UUID uuid) {
		return duels.containsKey(uuid);
	}
	
	public static Duel getDuel(Player player) {
		return getDuel(player.getUniqueId());
	}
	
	public static Duel getDuel(UUID uuid) {
		return duels.get(uuid);
	}
	
	private static final HashMap<UUID, Duel> duels = new HashMap<UUID, Duel>();

}
