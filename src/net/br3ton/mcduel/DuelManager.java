package net.br3ton.mcduel;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuelManager implements Listener {

	public static void startDuel(final Duel duel) {
		Player dueler = duel.getPlayer1();
		Player accepter = duel.getPlayer2();
		accepter.teleport(dueler);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MCDuel.instance, new Runnable() {
			public void run() {
				if (duelsList.contains(duel)) {
					MessageUtils.sendMessage(duel.getPlayer1(), "Duel timed out!");
					MessageUtils.sendMessage(duel.getPlayer2(), "Duel timed out!");
				}
				duelsList.remove(duel);
				return;
			}

		}, 180 * 20);
		nullDuel(duel);
	}

	public static void nullDuel(Duel duel) {
		duel = null;
	}

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		Entity attacker = event.getDamager();
		Entity defender = event.getEntity();

		if (defender.getType() == EntityType.PLAYER && attacker.getType() == EntityType.PLAYER) {
			Player pDefender = (Player) defender;
			Player pAttacker = (Player) attacker;

			for (Duel duels : duelsList) {
				if (duels.isAccepted()) {
					if (duels.getPlayers().contains(pDefender) && duels.getPlayers().contains(pAttacker)) {
						if (!duels.isStarted()) {
							event.setCancelled(true);
							event.setDamage(0.0D);
						}
						return;
					}
					event.setCancelled(true);
					event.setDamage(0.0D);

					if (duels.getPlayers().contains(pAttacker) && !(duels.getPlayers().contains(pDefender))) {
						event.setCancelled(true);
						event.setDamage(0.0D);
					}
				}

			}

		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player died = event.getEntity();
		Duel duel = null;
		for (Duel duels : duelsList) {
			if (duels.isAccepted()) {
				if (duels.getPlayers().contains(died)) {
					duel = duels;
					if (duels.getPlayer1() == died) {
						MessageUtils.broadcastMessage(duels.getPlayer2().getName() + " won the duel against " + died.getName());
						ParticleEffect.FIREWORKS_SPARK.display(duels.getPlayer2().getLocation().add(0.0, 2, 0.0), 0.1F, 0.1F, 0.1F, 0.1F, 100);
						ParticleEffect.FIREWORKS_SPARK.display(duels.getPlayer2().getLocation().add(0.0, 2, 0.0), 0, 0, 0, 0, 50);
						duels.getPlayer2().setHealth(duels.getPlayer2().getMaxHealth());
					} else {
						MessageUtils.broadcastMessage(duels.getPlayer1().getName() + " won the duel against " + died.getName());
						ParticleEffect.FIREWORKS_SPARK.display(duels.getPlayer1().getLocation().add(0.0, 2, 0.0), 0.1F, 0.1F, 0.1F, 0.1F, 100);
						ParticleEffect.FIREWORKS_SPARK.display(duels.getPlayer1().getLocation().add(0.0, 2, 0.0), 0, 0, 0, 0, 50);
						duels.getPlayer1().setHealth(duels.getPlayer1().getMaxHealth());
					}
					duels = null;
				}
			}
		}
		if (duel != null) {
			duelsList.remove(duel);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Duel duel = null;
		for (Duel duels : duelsList) {
			if (duels.getPlayers().contains(event.getPlayer())) {
				if (duels.getPlayer1().getLocation().distance(duels.getPlayer2().getLocation()) > 40.0D) {
					duel = duels;
					MessageUtils.sendMessage(duels.getPlayer1(), "Duel cancelled, you are too far away from the other player!");
					MessageUtils.sendMessage(duels.getPlayer2(), "Duel cancelled, you are too far away from the other player!");
					duels = null;
				}
			}
		}
		if (duel != null) {
			duelsList.remove(duel);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Duel duels = null;
		for (Duel duel : duelsList) {
			if (duel.getPlayers().contains(player)) {
				duels = duel;
				for (Player p : duel.getPlayers()) {
					MessageUtils.sendMessage(p, "Opponent logged out, duel cancelled!");
					duel = null;
				}
			}
		}
		if (duels != null) {
			duelsList.remove(duels);
		}
	}

	public static ArrayList<Duel> duelsList = new ArrayList<Duel>();

}
