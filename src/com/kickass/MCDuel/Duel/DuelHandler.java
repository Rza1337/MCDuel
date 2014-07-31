package com.kickass.MCDuel.Duel;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.kickass.MCDuel.MCDuel;
import com.kickass.MCDuel.Arena.Arena;
import com.kickass.MCDuel.Arena.ArenaManager;
import com.kickass.MCDuel.Utils.MessageUtils;

public class DuelHandler {

	public static void startDuel(final Duel duel) {
		Player requester = duel.getRequester();

		// Generate arena code
		Location centre = requester.getLocation();
		Arena arena = new Arena(centre.getWorld(), centre.getBlockX() - ARENA_WIDTH, centre.getBlockZ() - ARENA_WIDTH, ARENA_WIDTH, ARENA_WIDTH);
		ArenaManager.addArena(duel, arena);

		if (duel.shouldStart()) {
			// Teleport
			for(Player player : duel.getPlayers()) {
				player.teleport(requester);
			}
			
			// Create grace period before fighting can occur
			BukkitRunnable runnable = new BukkitRunnable() {
				double count = 0;
				double secs = 3;

				@Override 	
				public void run() {
					if (count - secs < 0) {
						for (Player p : duel.getPlayers()) {
							MessageUtils.sendMessage(p, (int) (secs - count) + "");
						}
					} else {
						duel.setStarted(true);
						for (Player p : duel.getPlayers()) {
							MessageUtils.sendMessage(p, "DUEL!");
							duel.setStarted(true);
							cancel();
						}
					}
					count++;
				}
			};
			runnable.runTaskTimer(MCDuel.getInstance(), 0, 20);
			
		}
	}

	public static void endDuel(Duel duel) {
		if (duel.getLivingPlayers().size() == 1) {
			Player winner = duel.getLivingPlayers().get(0);

			// Winner actions
			winner.setHealth(winner.getMaxHealth());
			// TODO Give winnings (in future versions, players can bet/buy in to
			// duels)

			// Broadcast winner to server
			String againstString = "";
			for (Player p : duel.getPlayers()) {
				if (p.getUniqueId().equals(winner.getUniqueId())) {
					continue;
				}
				againstString += ", " + p.getName();
			}
			MessageUtils.broadcastMessage(winner.getName() + " has won the duel against " + againstString + " !");
		}
		duel.setEnded(true);
		DuelManager.removeDuel(duel);
		ArenaManager.removeArena(duel);
	}

	public static final int ARENA_WIDTH = 30;

}
