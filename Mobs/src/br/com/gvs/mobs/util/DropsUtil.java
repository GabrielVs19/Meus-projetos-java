package br.com.gvs.mobs.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.v1_7_R2.EnchantmentManager;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityLiving;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class DropsUtil
{


	public static ArrayList<ItemStack> getCalculatedDrops(Mob mob)
	{
		return getCalculatedDrops(mob.getDrops(), mob.getMaxDrops());
	}

	public static ArrayList<ItemStack> getCalculatedDrops(Boss boss)
	{
		return getCalculatedDrops(boss.getDrops(), boss.getMaxDrops());
	}

	public static ArrayList<ItemStack> getCalculatedDrops(Player killer, Mob mob)
	{
		return getCalculatedDrops(killer, mob.getDrops(), mob.getMaxDrops());
	}

	public static ArrayList<ItemStack> getCalculatedDrops(Player killer, Boss boss)
	{
		return getCalculatedDrops(killer, boss.getDrops(), boss.getMaxDrops());
	}

	public static ArrayList<ItemStack> getCalculatedDrops(Map<ItemStack, Double> drops, int maxDrops)
	{

		ArrayList<ItemStack> d = new ArrayList<ItemStack>();
		if(drops != null){
			List<ItemStack> items = new LinkedList<ItemStack>(Arrays.asList(drops.keySet().toArray(new ItemStack[drops.size()])));
			if(items != null)
			{
				for(int i = 0; i < Math.min(maxDrops, items.size()); i++)
				{
					ItemStack itemStack = items.get((int) (Math.random() * items.size()));

					if(itemStack == null)
					{
						continue;
					}

					if(drops.get(itemStack) == null){
						continue;
					}

					double chance = drops.get(itemStack);

					if(chance >= Math.random() * 100)
					{
						d.add(itemStack);
						items.remove(itemStack);
					}
				}
			}
		}

		return d;
	}

	public static ArrayList<ItemStack> getCalculatedDrops(Player killer, Map<ItemStack, Double> drops, int maxDrops)
	{
		Entity entity = ((CraftPlayer)killer).getHandle();
		int loot = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving)entity);
		ArrayList<ItemStack> d = new ArrayList<ItemStack>();
		if(drops != null){
			List<ItemStack> items = new LinkedList<ItemStack>(Arrays.asList(drops.keySet().toArray(new ItemStack[drops.size()])));
			if(items != null)
			{
				for(int i = 0; i < Math.min(maxDrops, items.size()); i++)
				{
					ItemStack itemStack = items.get((int) (Math.random() * items.size()));

					if(itemStack == null)
					{
						continue;
					}

					if(drops.get(itemStack) == null){
						continue;
					}

					double chance = drops.get(itemStack);

					if(chance >= Math.random() * 100)
					{
						ItemStack newItem = itemStack.clone();
						if(loot > 0){
							int maxStackSize = newItem.getMaxStackSize();
							int amount = newItem.getAmount();
							amount += new Random().nextInt(loot + 1);
							if(amount <= maxStackSize){
								newItem.setAmount(amount);
							}
						}
						d.add(newItem);
						items.remove(itemStack);
					}
				}
			}
		}

		return d;
	}

}
