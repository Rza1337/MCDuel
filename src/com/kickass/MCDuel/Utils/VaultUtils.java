package com.kickass.MCDuel.Utils;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultUtils {

	public static void initialize(JavaPlugin plugin) {
		setupChat(plugin);
		setupEconomy(plugin);
		setupPermissions(plugin);
	}
	
	public static Chat getChat() {
		return chat;
	}
	
	public static Economy getEconomy() {
		return economy;
	}
	
	public static Permission getPermission() {
		return permission;
	}

	private static boolean setupPermissions(JavaPlugin plugin) {
		RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private static boolean setupChat(JavaPlugin plugin) {
		RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}
		return (chat != null);
	}

	private static boolean setupEconomy(JavaPlugin plugin) {
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economy != null);
	}

	private static Permission permission = null;
	private static Economy economy = null;
	private static Chat chat = null;
}
