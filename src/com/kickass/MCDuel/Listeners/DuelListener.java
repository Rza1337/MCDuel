package com.kickass.MCDuel.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
		
		if(attacker.getType() == EntityType.PLAYER && defender.getType() == EntityType.PLAYER) {
			Player attackerPlayer = (Player) attacker;
			Player defenderPlayer = (Player) defender;

			// Only lets attackers attack players in their duel
			if(DuelManager.isPlayerDueling(attackerPlayer)) {
				Duel duel = DuelManager.getDuel(attackerPlayer);
				if(duel.getPlayers().contains(defenderPlayer) && duel.hasStarted() && duel.isAccepted()) {
					return;
				}
				event.setCancelled(true);
				event.setDamage(0.0D);
			}
		} else if(defender.getType() == EntityType.PLAYER) {
			Player defenderPlayer = (Player) defender;
			
			// Only lets defenders get attacked by players in their duel
			if(attacker.getType() == EntityType.PLAYER) {
				Player attackerPlayer = (Player) attacker;
				if(DuelManager.isPlayerDueling(defenderPlayer)) {
					Duel duel = DuelManager.getDuel(defenderPlayer);
					if(duel.getPlayers().contains(attackerPlayer) && duel.hasStarted() && duel.isAccepted()) {
						return;
					}
				}
			}
			
			event.setCancelled(true);
			event.setDamage(0.0D);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (DuelManager.isPlayerDueling(player)) {
			Duel duel = DuelManager.getDuel(player);
			if (!duel.hasStarted()) {
				for (Player p : duel.getPlayers()) {
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

}
