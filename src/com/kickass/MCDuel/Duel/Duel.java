package com.kickass.MCDuel.Duel;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Duel {

	public Duel(Player duelReq, Player... players) {
		duelRequester = duelReq;
		if (players == null || players.length == 0) {
			throw new IllegalArgumentException("You must have at least one other player to duel.");
		}

		// Duel Requester
		acceptedPlayers.add(duelReq);
		duelPlayers.add(duelReq);
		playersAlive.add(duelReq);

		// Participants
		for (Player p : players) {
			duelPlayers.add(p);
			playersAlive.add(p);
		}
	}

	public Player getRequester() {
		return duelRequester;
	}

	public ArrayList<Player> getPlayers() {
		return duelPlayers;
	}

	public void setAccepted(Player player) {
		if (!acceptedPlayers.contains(player)) {
			acceptedPlayers.add(player);
		}
	}

	public boolean isAccepted() {
		return acceptedPlayers.size() == duelPlayers.size();
	}

	public boolean hasAccepted(Player player) {
		return acceptedPlayers.contains(player);
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean shouldStart() {
		return duelPlayers.size() == acceptedPlayers.size();
	}

	public boolean isAlive(Player player) {
		return playersAlive.contains(player);
	}

	public int getPlayersAlive() {
		return playersAlive.size();
	}

	public void killPlayer(Player player) {
		playersAlive.remove(player);
	}

	public ArrayList<Player> getLivingPlayers() {
		return playersAlive;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public boolean hasEnded() {
		return ended;
	}

	private Player duelRequester;
	private boolean started = false;
	private boolean ended = false;
	private final ArrayList<Player> acceptedPlayers = new ArrayList<Player>();
	private final ArrayList<Player> playersAlive = new ArrayList<Player>();
	private final ArrayList<Player> duelPlayers = new ArrayList<Player>();

}
