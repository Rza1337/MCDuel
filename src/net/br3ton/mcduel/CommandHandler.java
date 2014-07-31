package net.br3ton.mcduel;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandHandler implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getLabel().equalsIgnoreCase("duel")) {
			return false;
		}

		if ((sender instanceof Player)) {
			pSender = (Player) sender;
		} else {
			MessageUtils.sendMessage(sender, "You must be a player to use this command!");
			return true;
		}

		if (!pSender.hasPermission("bannaduel.duel")) {
			MessageUtils.sendMessage(pSender, "You do not have permission to use this command!");
			return true;
		}

		if (args.length == 1) {
			if (args[0].equals("accept")) {
				for (final Duel duel : DuelManager.duelsList) {
					if (!duel.isAccepted()) {
						if (duel.getPlayer1().getLocation().distance(duel.getPlayer2().getLocation()) > 30.0D) {
							MessageUtils.sendMessage(pSender, "You must be within a 30 block radius to accept the duel request!");
							return true;
						}
						if (duel.getPlayers().contains(pSender)) {
							// TODO Start duel.
							duel.setAccepted(true);

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
											this.cancel();
										}
									}

									count++;

								}

							};
							runnable.runTaskTimer(MCDuel.instance, 0, 20);

							DuelManager.startDuel(duel);
							MessageUtils.sendMessage(pSender, "Duel accepted!");
							MessageUtils.sendMessage(duel.getPlayer1(), duel.getPlayer2().getName() + " accepted your duel!");
							return true;
						}
					}

				}
			} else if (args[0].equalsIgnoreCase("decline")) {
				for (Duel duels : DuelManager.duelsList) {
					if (duels.getPlayers().contains(pSender)) {
						if (!duels.isStarted()) {
							for (Player player : duels.getPlayers()) {
								MessageUtils.sendMessage(player, "Duel declined by " + pSender.getName() + ".");
							}
							DuelManager.duelsList.remove(duels);
							return true;
						}
						MessageUtils.sendMessage(pSender, "Duel has already started!");
						return true;
					} else {
						MessageUtils.sendMessage(pSender, "You currently have no active duels to decline!");
					}

				}
			} else {
				final Player target = Bukkit.getServer().getPlayer(args[0]);

				if (cooldown.contains(pSender)) {
					MessageUtils.sendMessage(pSender, "You must wait 5 mintues between sending duel requests!");
					return true;
				}

				if (target == pSender) {
					MessageUtils.sendMessage(pSender, "You cannot send a duel request to yourself silly!");
					return true;
				}

				if (target == null) {
					MessageUtils.sendMessage(pSender, "There is no such player as " + args[0] + " online! :(");
					return true;
				} else {

					if (pSender.getLocation().distance(target.getLocation()) > 30.0D) {
						MessageUtils.sendMessage(sender, "You must be within a 30 block radius to send a duel request!");
						return true;
					}

					for (Duel duels : DuelManager.duelsList) {
						if (duels.getPlayers().contains(target)) {
							MessageUtils.sendMessage(sender, "This player has already been invited to duel");
							return true;
						}

						if (duels.getPlayers().contains(pSender)) {
							MessageUtils.sendMessage(pSender, "You have already sent a duel request or have an outstanding duel request.");
							return true;
						}
					}

					final Duel duel = new Duel(sender.getName(), Bukkit.getServer().getPlayer(sender.getName()), target);
					MessageUtils.sendMessage(sender, "You have invited " + target.getName() + " to duel!");
					MessageUtils.sendMessage(target, pSender.getName() + " has invited you to duel. Type /duel accept or /duel decline");
					DuelManager.duelsList.add(duel);

					cooldown.add(pSender);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MCDuel.instance, new Runnable() {
						public void run() {
							cooldown.remove(pSender);
							MessageUtils.sendMessage(pSender, "You can now send duel requests again.");
							return;
						}

					}, 300 * 20);

					duelTimeout.add(duel);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MCDuel.instance, new Runnable() {
						public void run() {
							if (duel.isAccepted() == false) {
								MessageUtils.sendMessage(pSender, "Duel request timed out!");
								MessageUtils.sendMessage(target, "Duel request timed out!");
							}
							return;
						}

					}, 30 * 20);

					return true;
				}
			}
		}
		MessageUtils.sendMessage(sender, "Incorrect Arguements!");
		return true;
	}

	private Player pSender;
	private ArrayList<Player> cooldown = new ArrayList<Player>();
	private ArrayList<Duel> duelTimeout = new ArrayList<Duel>();

}
