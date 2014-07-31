package com.kickass.MCDuel.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {

	/**
	 * Sends a message in white with the plugin prefix.
	 * 
	 * @param p
	 *            The player to recieve a message.
	 * @param message
	 *            The message to be sent.
	 */

	public static void sendMessage(Player p, String message) {
		p.sendMessage(prefix + ChatColor.WHITE + message);
	}

	public static void sendMessage(CommandSender p, String message) {
		p.sendMessage(prefix + ChatColor.WHITE + message);
	}

	/**
	 * Sends a message in red with the plugin prefix.
	 * 
	 * @param p
	 *            The player to recieve a message.
	 * @param message
	 *            The message to be sent.
	 */

	public static void sendWarning(Player p, String message) {
		p.sendMessage(prefix + ChatColor.RED + message);
	}

	public static void sendWarning(CommandSender p, String message) {
		p.sendMessage(prefix + ChatColor.RED + message);
	}

	public static void broadcastMessage(String message) {
		Bukkit.getServer().broadcastMessage(prefix + ChatColor.WHITE + message);
	}

	public static void broadcastError(String message) {
		Bukkit.getServer().broadcastMessage(prefix + ChatColor.RED + message);
	}

	private static String prefix = ChatColor.GOLD + "BannaDuel " + ChatColor.DARK_GRAY + "| ";

}
