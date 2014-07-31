package net.br3ton.mcduel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

	@EventHandler
	public void onCommandPre(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		if (command.equalsIgnoreCase("tpa") || command.equalsIgnoreCase("tp") || command.equalsIgnoreCase("warp") || command.equalsIgnoreCase("spawn") || command.equalsIgnoreCase("f home")) {
			for (Duel duel : DuelManager.duelsList) {
				if (duel.getPlayers().contains(event.getPlayer())) {
					event.setCancelled(true);
					MessageUtils.sendMessage(event.getPlayer(), "You cannot teleport while dueling!");
				}
			}
		}
	}
}
