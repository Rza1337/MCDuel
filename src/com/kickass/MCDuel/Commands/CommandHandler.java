package com.kickass.MCDuel.Commands;

import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.kickass.MCDuel.MCDuel;
import com.kickass.MCDuel.Duel.Duel;
import com.kickass.MCDuel.Duel.DuelHandler;
import com.kickass.MCDuel.Duel.DuelManager;
import com.kickass.MCDuel.Utils.MessageUtils;
import com.kickass.MCDuel.Utils.VaultUtils;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getLabel().equalsIgnoreCase("duel")) {
			return false;
		}

		Player playerSender = null;

		// Ensures a sender is a player, and not the console or other
		if (sender instanceof Player) {
			playerSender = (Player) sender;
		} else {
			MessageUtils.sendMessage(sender, "You must be a player to use this command!");
			return true;
		}

		// Ensures a sender has the relevant permission to duel
		// if (!sender.hasPermission("bannaduel.duel")) {
		// MessageUtils.sendMessage(sender,
		// "You do not have permission to use this command!");
		// return true;
		// }

		// Check for accept command
		if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {
			if (DuelManager.isPlayerDueling(playerSender)) {
				Duel duel = DuelManager.getDuel(playerSender);
				// Use distance squared as its easier on the processor
				if (duel.getRequester().getLocation().distanceSquared(playerSender.getLocation()) > (ACCEPT_RANGE * ACCEPT_RANGE)) {
					MessageUtils.sendMessage(playerSender, "You must be within " + ACCEPT_RANGE + " blocks to accept.");
				} else {
					duel.setAccepted(playerSender);
					for (Player player : duel.getPlayers()) {
						MessageUtils.sendMessage(player, playerSender.getName() + " has accepted the duel.");
					}

					// Start duel check
					if (duel.shouldStart()) {
						DuelHandler.startDuel(duel);
						timeoutHolder.remove(duel);
					}
				}
			}
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("decline")) {
			// Check for decline command
			if (DuelManager.isPlayerDueling(playerSender)) {
				Duel duel = DuelManager.getDuel(playerSender);
				if (duel.hasStarted()) {
					MessageUtils.sendMessage(playerSender, "This duel is in progress, you cannot leave.");
				} else {
					for (Player player : duel.getPlayers()) {
						MessageUtils.sendMessage(player, playerSender.getName() + " has chosen to decline the duel, therefore the duel is cancelled.");
						// Set to ended so the duel can be cleaned up
						duel.setEnded(true);
						DuelManager.removeDuel(duel);
						timeoutHolder.remove(duel);
					}
				}
			} else {
				MessageUtils.sendMessage(playerSender, "You have no duels to accept.");
			}
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
			// Allows creators of duels to cancel a duel before it begins
			if (DuelManager.isPlayerDueling(playerSender)) {
				Duel duel = DuelManager.getDuel(playerSender);
				if (duel.getRequester().getUniqueId().equals(playerSender.getUniqueId())) {
					if (duel.hasStarted()) {
						MessageUtils.sendMessage(playerSender, "You cannot cancel a duel in progress.");
					} else {
						for (Player p : duel.getPlayers()) {
							MessageUtils.sendMessage(p, "The duel you were invited to has been cancelled.");
						}
						duel.setEnded(true);
						DuelManager.removeDuel(duel);
						timeoutHolder.remove(duel);
					}
				}
			}
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
			PluginDescriptionFile pdf = MCDuel.getInstance().getDescription();
			MessageUtils.sendMessage(sender, "Version: " + pdf.getVersion());
			return true;
		} else if (args.length >= 1) {

			if (playerCooldown.contains(playerSender)) {
				MessageUtils.sendMessage(playerSender, "You cannot send another duel request yet.");
				return true;
			}

			boolean isBet = false;
			int stakeValue = 0;

			// Checks if there is a bet or not
			if (args.length >= 2) {
				String stake = args[0];
				@SuppressWarnings("deprecation")
				Player p = Bukkit.getPlayer(stake);
				if (p == null || !p.isOnline()) {
					try {
						stakeValue = Integer.parseInt(stake);
						if (stakeValue > 0) {
							isBet = true;
						} else {
							MessageUtils.sendMessage(sender, "You must place a bet greater than 0");
							return true;
						}
					} catch (NumberFormatException ex) {
					}
				}
			}

			// Requesters current World
			World w = playerSender.getWorld();

			// Checks that invited players are online
			ArrayList<Player> participants = new ArrayList<Player>();

			for (int i = 0; i < args.length; i++) {

				// Skip first arg is is bet
				if (isBet) {
					if (i == 0) {
						continue;
					}
				}

				// Stops players inviting themselves
				if (args[i].equalsIgnoreCase(playerSender.getName())) {
					MessageUtils.sendMessage(playerSender, "You cannot invite yourself.");
					return true;
				}

				@SuppressWarnings("deprecation")
				// Leave this here until bukkit fix their shit, cause they suck.
				Player player = Bukkit.getPlayer(args[i]);
				if (player == null) {
					MessageUtils.sendMessage(playerSender, args[i] + " is not online. The duel requests have not gone through.");
					return true;
				} else if (!player.getWorld().getName().equalsIgnoreCase(w.getName())) {
					MessageUtils.sendMessage(playerSender, args[i] + " is not in the correct world. The duel request has not gone through.");
					return true;
				} else {
					participants.add(player);
				}
			}

			// Checks everyone can afford the bet
			if (isBet) {
				Economy eco = VaultUtils.getEconomy();
				if (eco == null) {
					MessageUtils.broadcastError("Vault has not found a valid economy. Please report to an admin.");
					return true;
				}
				if (!(eco.getBalance(playerSender) >= stakeValue)) {
					MessageUtils.sendMessage(playerSender, "You cannot afford the duel. The duel request has not gone through.");
					return true;
				}
				for (Player p : participants) {
					if (!(eco.getBalance(p) >= stakeValue)) {
						MessageUtils.sendMessage(playerSender, p.getName() + " cannot afford to join the duel. The duel request has not gone through.");
						return true;
					}
				}
			}

			// Checks if a recipient to a duel is mid duel
			for (Player p : participants) {
				if (DuelManager.isPlayerDueling(p)) {
					MessageUtils.sendMessage(playerSender, p.getName() + " is already in a duel. The duel request has not gone through.");
					return true;
				}
			}

			Player[] participantArr = new Player[participants.size()];
			participantArr = participants.toArray(participantArr);
			final Duel duel = new Duel(stakeValue, playerSender, participantArr);
			DuelManager.addDuel(duel);

			// Sends messages to accept or decline to everyone
			for (Player p : duel.getPlayers()) {
				if (p.getUniqueId().equals(duel.getRequester().getUniqueId())) {
					String againstString = "";
					for (Player against : duel.getPlayers()) {
						if (against.getUniqueId().equals(duel.getRequester().getUniqueId())) {
							continue;
						}
						againstString += ", " + against.getName();
					}
					MessageUtils.sendMessage(p, "You sent invites to" + againstString);
				} else {
					MessageUtils.sendMessage(p, duel.getRequester().getName() + " has invited you to duel. Type /duel accept or /duel decline");
					if (duel.getStake() > 0) {
						MessageUtils.sendMessage(p, ChatColor.DARK_RED + "[WARNING] " + ChatColor.WHITE + "this duel has a stake entry of " + ChatColor.GREEN + duel.getStake());
					}
				}
			}

			// Starts timeout timer
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MCDuel.getInstance(), new Runnable() {
				public void run() {
					if (timeoutHolder.contains(duel)) {
						for (Player p : duel.getPlayers()) {
							MessageUtils.sendMessage(p, "Duel request timed out.");
						}
						DuelManager.removeDuel(duel);
						timeoutHolder.remove(duel);
					}
					return;
				}

			}, 30 * 20);

			final Player pSender = playerSender;

			// Starts Player Cooldown
			playerCooldown.add(playerSender);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MCDuel.getInstance(), new Runnable() {
				public void run() {
					playerCooldown.remove(pSender);
					MessageUtils.sendMessage(pSender, "You can now send duel requests again.");
					return;
				}

			}, 300 * 20);

			return true;
		}

		// Send if arguments are wrong.
		MessageUtils.sendMessage(sender, "[]'s indicate essential arguments.");
		MessageUtils.sendMessage(sender, "<>'s indicate optional arguments.");
		MessageUtils.sendMessage(sender, "...'s indicate an option to have a limitless amount.");
		MessageUtils.sendMessage(sender, "Command format is as follows:");
		MessageUtils.sendMessage(sender, "/duel <Coin Bet> [PlayerName] <Alternative PlayerName>...");
		MessageUtils.sendMessage(sender, "/duel [PlayerName] <Alternative PlayerName>...");
		return true;
	}

	private static final double ACCEPT_RANGE = 30.0D;
	private static final ArrayList<Player> playerCooldown = new ArrayList<Player>();
	private static final ArrayList<Duel> timeoutHolder = new ArrayList<Duel>();

}
