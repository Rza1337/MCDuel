package com.kickass.MCDuel.Arena;

import org.bukkit.Location;
import org.bukkit.World;

import com.kickass.MCDuel.Utils.ParticleEffect;

public class Arena {

	public Arena(World world, int xPos, int zPos, int xLength, int zLength) {
		this.world = world;
		this.xPos = xPos;
		this.zPos = zPos;
		this.xLength = xLength;
		this.zLength = zLength;

	}

	public void render() {
		for (int x = xPos; x < xPos + xLength; x++) {
			for (int y = 0; y < world.getMaxHeight(); y++) {
				for (int z = zPos; z < zPos + zLength; z++) {
					ParticleEffect.PORTAL.display(new Location(world, x, y, z), 0.0F, 0.0F, 0.0F, 0.0F, 1);
				}
			}
		}
	}

	public boolean isInBounds(Location loc) {
		if (loc.getBlockX() >= xPos && loc.getBlockX() < xPos + xLength && loc.getBlockZ() > zPos && loc.getBlockZ() < zPos + zLength) {
			return true;
		}
		return false;
	}

	private World world;
	private int xPos;
	private int zPos;
	private int xLength;
	private int zLength;

}
