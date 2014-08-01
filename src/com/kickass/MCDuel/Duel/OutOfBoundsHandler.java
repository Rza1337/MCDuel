package com.kickass.MCDuel.Duel;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.kickass.MCDuel.Arena.Arena;
import com.kickass.MCDuel.Arena.ArenaManager;

public class OutOfBoundsHandler extends BukkitRunnable {

	@Override
	public void run() {
		ArrayList<Duel> duels = new ArrayList<Duel>(DuelManager.getDuels());

		for (int i = 0; i < duels.size(); i++) {
			Duel d = duels.get(i);
			if (d.hasStarted()) {
				Arena arena = ArenaManager.getArena(d);
				for (Player player : d.getLivingPlayers()) {
					if (!arena.isInBounds(player.getLocation())) {
						player.damage(1.0D);
					}
				}
			}
		}
	}

}
