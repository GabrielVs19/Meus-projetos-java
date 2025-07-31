package br.com.gvs.mobs.util;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;


public class Mob
{
	
	
	private double attackDamage;
	private double moveSpeed;
	private double health;
	private double followRange;
	private double knockbackResistence;
	private int level;
	private double reward;
	private AttackType attackType;
	private Map<ItemStack, Double> drops;
	private int maxDrops;
	private HashMap<Integer, ItemStack> equipments;
	private Map<String, SpawnRadius> spawnRadius = new HashMap<String, SpawnRadius>();
	private MobType mobType;
	
	public Mob(MobType mobType, int level, double attack, double moveSpeed, double health, double followRange, double knockbackResistence, double reward, AttackType attackType, Map<ItemStack, Double> drops, int maxDrops, HashMap<Integer, ItemStack> equipments, Map<String, SpawnRadius> spawnRadius)
	{
		this.setAttackDamage(attack);
		this.setMoveSpeed(moveSpeed);
		this.setHealth(health);
		this.setFollowRange(followRange);
		this.setKnockbackResistence(knockbackResistence);
		this.setLevel(level);
		this.setReward(reward);
		this.setAttackType(attackType);
		this.setDrops(drops);
		this.setMaxDrops(maxDrops);
		this.setEquipments(equipments);
		this.setSpawnRadius(spawnRadius);
		this.setMobType(mobType);
	}
	
	public double getAttackDamage()
	{
		return this.attackDamage;
	}
	
	public void setAttackDamage(double attackDamage)
	{
		this.attackDamage = attackDamage;
	}
	
	public double getMoveSpeed()
	{
		return moveSpeed;
	}
	
	public void setMoveSpeed(double moveSpeed)
	{
		this.moveSpeed = moveSpeed;
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public void setHealth(double health)
	{
		this.health = health;
	}
	
	public double getFollowRange()
	{
		return followRange;
	}
	
	public void setFollowRange(double followRange)
	{
		this.followRange = followRange;
	}
	
	public double getKnockbackResistence()
	{
		return knockbackResistence;
	}
	
	public void setKnockbackResistence(double knockbackResistence)
	{
		this.knockbackResistence = knockbackResistence;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public double getReward()
	{
		return reward;
	}
	
	public void setReward(double reward)
	{
		this.reward = reward;
	}
	
	public AttackType getAttackType()
	{
		return attackType;
	}
	
	public void setAttackType(AttackType attackType)
	{
		this.attackType = attackType;
	}
	
	public Map<ItemStack, Double> getDrops()
	{
		return drops;
	}
	
	public void setDrops(Map<ItemStack, Double> drops)
	{
		this.drops = drops;
	}
	
	public int getMaxDrops()
	{
		return maxDrops;
	}
	
	public void setMaxDrops(int maxDrops)
	{
		this.maxDrops = maxDrops;
	}
	
	public HashMap<Integer, ItemStack> getEquipments()
	{
		return equipments;
	}
	
	public void setEquipments(HashMap<Integer, ItemStack> equipments)
	{
		this.equipments = equipments;
	}
	
	public Map<String, SpawnRadius> getSpawnRadius()
	{
		return spawnRadius;
	}
	
	public SpawnRadius getSpawnRadius(String worldName)
	{
		if(spawnRadius.containsKey(worldName))
		{
			return spawnRadius.get(worldName);
		}
		return null;
	}
	
	public void setSpawnRadius(Map<String, SpawnRadius> spawnRadius)
	{
		this.spawnRadius = spawnRadius == null ? new HashMap<String, SpawnRadius>() : spawnRadius;
	}
	
	public MobType getMobType()
	{
		return mobType;
	}
	
	public void setMobType(MobType mobType)
	{
		this.mobType = mobType;
	}
	
}
