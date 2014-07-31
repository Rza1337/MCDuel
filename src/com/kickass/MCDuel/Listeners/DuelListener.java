package com.kickass.MCDuel.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.kickass.MCDuel.Arena.Arena;
import com.kickass.MCDuel.Arena.ArenaManager;
import com.kickass.MCDuel.Duel.Duel;
import com.kickass.MCDuel.Duel.DuelHandler;
import com.kickass.MCDuel.Duel.DuelManager;
import com.kickass.MCDuel.Utils.MessageUtils;
import com.kickass.MCDuel.Utils.ParticleEffect;

public class DuelListener implements Listener {

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		Entity attacker = event.getDamager();
		Entity defender = event.getEntity();

		if (defender.getType() == EntityType.PLAYER && attacker.getType() == EntityType.PLAYER) {
			Player pDefender = (Player) defender;
			Player pAttacker = (Player) attacker;

			// Only need to check the defenders duelling status for stopping
			// damage whilst duelling
			if (DuelManager.isPlayerDueling(pDefender)) {
				Duel duel = DuelManager.getDuel(pDefender);
				if (duel.getLivingPlayers().contains(pAttacker)) {
					if (duel.hasStarted()) {
						return;
					}
				}
				event.setCancelled(true);
				event.setDamage(0.0D);
			}

			// Checks to ensure a dueller is only damaging a combatant
			if (DuelManager.isPlayerDueling(pAttacker)) {
				Duel duel = DuelManager.getDuel(pAttacker);
				if (duel.getLivingPlayers().contains(pDefender)) {
					if (duel.hasStarted()) {
						return;
					}
				}
				event.setCancelled(true);
				event.setDamage(0.0D);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (DuelManager.isPlayerDueling(player)) {
			Duel duel = DuelManager.getDuel(player);
			if (!duel.hasStarted()) {
				for(Player p : duel.getPlayers()) {
					MessageUtils.sendMessage(p, player.getName() + " has quit, therefore the duel has been cancelled.");
				}
				DuelHandler.endDuel(duel);
				return;
			}
			duel.killPlayer(player);

			// Shame players that leave mid duel
			MessageUtils.broadcastMessage("Opponent " + player.getName() + " logged out during a duel!");

			// Checks for victory conditions
			if (duel.getPlayersAlive() == 1) {
				DuelHandler.endDuel(duel);
			}
		}

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player died = event.getEntity();

		if (DuelManager.isPlayerDueling(died)) {

			// Messages participants on death
			Duel duel = DuelManager.getDuel(died);
			for (Player p : duel.getPlayers()) {
				MessageUtils.sendMessage(p, "Opponent " + died.getName() + " has been eliminated.");
			}
			duel.killPlayer(died);

			// Firework effect on killing player
			if (died.getKiller() != null) {
				// Covers the player in 150 firework particles above their head
				ParticleEffect.FIREWORKS_SPARK.display(died.getKiller().getLocation().add(0.0, 2, 0.0), 0.1F, 0.1F, 0.1F, 0.1F, 100);
				ParticleEffect.FIREWORKS_SPARK.display(died.getKiller().getLocation().add(0.0, 2, 0.0), 0, 0, 0, 0, 50);
			}

			// Checks for victory conditions
			if (duel.getPlayersAlive() == 1) {
				DuelHandler.endDuel(duel);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player mover = event.getPlayer();

		// Summons lightning on players that leave the arena zone
		if (DuelManager.isPlayerDueling(mover)) {
			Duel duel = DuelManager.getDuel(mover);
			if (duel.hasStarted()) {
				Arena arena = ArenaManager.getArena(duel);
				if (!arena.isInBounds(mover.getLocation())) {
					mover.damage(1.0D); // Investigate if appropriate amounts of
										// damage
				}
			}
		}
	}

}
