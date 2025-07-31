package br.com.gvs.mobs.bosses;


import org.bukkit.Location;

import br.com.gvs.mobs.util.Boss;


public abstract interface CustomBoss
{
	
	public abstract Boss getBoss();
	public abstract void setWalkToLocation(Location loc);
	public abstract void resetAI();
	public abstract void setAI();
}
