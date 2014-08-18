package com.kickass.MCDuel.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.kickass.MCDuel.Duel.Duel;
import com.kickass.MCDuel.Duel.DuelManager;
import com.kickass.MCDuel.Utils.MessageUtils;

public class CommandListener implements Listener {

	@EventHandler
	public void onCommandPre(PlayerCommandPreprocessEvent event) {
		final Player player = event.getPlayer();
		if (player.isOp()) {
			return;
		}
		if (DuelManager.isPlayerDueling(player)) {
			Duel duel = DuelManager.getDuel(player);
			if (duel.hasStarted()) {
				event.setCancelled(true);
				MessageUtils.sendMessage(event.getPlayer(), "You cannot use commands while dueling!");
			} else {
				String command = event.getMessage().toLowerCase();
				if (command.contains("eco") || command.contains("pay")) {
					event.setCancelled(true);
					MessageUtils.sendMessage(event.getPlayer(), "You cannot use economy commands while waiting to duel!");
				}
			}
		}
	}

}
