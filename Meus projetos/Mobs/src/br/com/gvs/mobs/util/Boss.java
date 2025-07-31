package br.com.gvs.mobs.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;


public class Boss
{
	
	
	private double attackDamage;
	private double moveSpeed;
	private double health;
	private double followRange;
	private double knockbackResistence;
	private double reward;
	private AttackType attackType;
	private Map<ItemStack, Double> drops;
	private int maxDrops;
	private HashMap<Integer, ItemStack> equipments;
	private ArrayList<Location> spawns = new ArrayList<Location>();
	private BossType bossType;
	private DialogsUtil dialogs;
	
	public Boss(BossType bossType, double attack, double moveSpeed, double health, double followRange, double knockbackResistence, double reward, AttackType attackType, Map<ItemStack, Double> drops, int maxDrops, HashMap<Integer, ItemStack> equipments, ArrayList<Location> spawns, DialogsUtil dialogs)
	{
		this.setAttackDamage(attack);
		this.setMoveSpeed(moveSpeed);
		this.setHealth(health);
		this.setFollowRange(followRange);
		this.setKnockbackResistence(knockbackResistence);
		this.setReward(reward);
		this.setAttackType(attackType);
		this.setDrops(drops);
		this.setMaxDrops(maxDrops);
		this.setEquipments(equipments);
		this.setBossType(bossType);
		this.setSpawns(spawns);
		this.setDialogs(dialogs);
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
	
	public BossType getBossType()
	{
		return bossType;
	}
	
	public void setBossType(BossType bossType)
	{
		this.bossType = bossType;
	}
	
	public ArrayList<Location> getSpawns()
	{
		return spawns;
	}
	
	public void setSpawns(ArrayList<Location> spawns)
	{
		this.spawns = spawns;
	}

	public DialogsUtil getDialogs() {
		return dialogs;
	}

	public void setDialogs(DialogsUtil dialogs) {
		this.dialogs = dialogs;
	}
	
}
