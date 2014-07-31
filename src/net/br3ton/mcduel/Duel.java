package net.br3ton.mcduel;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Duel {

	public Duel(String duelName, Player player1, Player player2) {
		this.duelName = duelName;
		this.player1 = player1;
		this.player2 = player2;
		duelPlayers.add(player1);
		duelPlayers.add(player2);
	}

	public ArrayList<Player> getPlayers() {
		return duelPlayers;
	}

	public String getDuelName() {
		return duelName;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isStarted() {
		return started;
	}

	private Player player1;
	private Player player2;
	private String duelName;
	private ArrayList<Player> duelPlayers = new ArrayList<Player>();
	private boolean accepted = false;
	private boolean started = false;

}
