package com.kickass.MCDuel.Arena;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.kickass.MCDuel.Utils.ParticleEffect;

public class Arena {

	public Arena(World world, int xPos, int zPos, int xLength, int zLength, int yLower, int yUpper) {
		this.world = world;
		this.xPos = xPos;
		this.yLower = yLower;
		this.zPos = zPos;
		this.xLength = xLength;
		this.yUpper = yUpper;
		this.zLength = zLength;

	}

	public void render(Player p) {
		for (int x = xPos; x < xPos + (xLength * 2); x++) {
			for (int y = yLower; y < yUpper; y += 5) {
				ParticleEffect.LARGE_EXPLODE.display(new Location(world, x, y, zPos), 0.5F, 0.5F, 0.0F, 0.0F, 1, p);
				ParticleEffect.LARGE_EXPLODE.display(new Location(world, x, y, zPos + (zLength * 2)), 0.5F, 0.5F, 0.0F, 0.0F, 1,p);
			}
		}
		for (int z = zPos; z < zPos + (zLength * 2); z++) {
			for (int y = yLower; y < yUpper; y += 5) {
				ParticleEffect.LARGE_EXPLODE.display(new Location(world, xPos, y, z), 0.0F, 0.5F, 0.5F, 0.0F, 1, p);
				ParticleEffect.LARGE_EXPLODE.display(new Location(world, xPos + (xLength * 2), y, z), 0.0F, 0.5F, 0.5F, 0.0F, 1, p);
			}
		}
	}

	public boolean isInBounds(Location loc) {
		if (loc.getBlockX() >= xPos && loc.getBlockX() < (xPos + (xLength * 2)) && loc.getBlockZ() > zPos && loc.getBlockZ() < (zPos + (zLength * 2))) {
			return true;
		}
		return false;
	}

	private World world;
	private int xPos;
	private int yLower;
	private int zPos;
	private int xLength;
	private int yUpper;
	private int zLength;

}
