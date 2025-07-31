package br.com.gvs.mobs.listeners;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.SpawnMobs;
import br.com.gvs.mobs.config.BossConfig;
import br.com.gvs.mobs.event.BossDamageByPlayer;
import br.com.gvs.mobs.event.BossDeathEvent;
import br.com.gvs.mobs.event.MobDamageByPlayer;
import br.com.gvs.mobs.event.MobDeathEvent;
import br.com.gvs.mobs.event.PlayerDamageByBossEvent;
import br.com.gvs.mobs.event.PlayerDamageByMobEvent;
import br.com.gvs.mobs.event.PlayerDeathByBossEvent;
import br.com.gvs.mobs.event.PlayerDeathByMobEvent;
import br.com.gvs.mobs.util.Boss;
import br.com.gvs.mobs.util.BossBarUtil;
import br.com.gvs.mobs.util.BossType;
import br.com.gvs.mobs.util.DropsUtil;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import br.com.smooph.bossbar.BossBar;
import net.minecraft.server.v1_7_R2.EntityLiving;


public class MonsterListener implements Listener
{


	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		if(SpawnMobs.players.containsKey(e.getPlayer().getName()))
		{
			SpawnMobs.players.remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			Mob mob = Mobs.getMob(e.getDamager());
			Boss boss = Mobs.getBoss(e.getDamager());

			if(boss != null)
			{
				PlayerDamageByBossEvent bossDmg = new PlayerDamageByBossEvent(player, e.getDamager(), boss, e.getDamage());
				Bukkit.getPluginManager().callEvent(bossDmg);
				e.setDamage(bossDmg.getDamage());
				if(bossDmg.isCancelled())
				{
					e.setCancelled(true);
				}
			}
			else
				if(mob != null)
				{
					PlayerDamageByMobEvent mobDmg = new PlayerDamageByMobEvent(player, e.getDamager(), mob, e.getDamage());
					Bukkit.getPluginManager().callEvent(mobDmg);
					e.setDamage(mobDmg.getDamage());
					if(mobDmg.isCancelled())
					{
						e.setCancelled(true);
					}
				}
		}
		else
			if(e.getDamager() instanceof Player)
			{
				Player player = (Player) e.getDamager();
				Mob mob = Mobs.getMob(e.getEntity());
				Boss boss = Mobs.getBoss(e.getEntity());

				if(boss != null)
				{
					BossDamageByPlayer bossDmg = new BossDamageByPlayer(player, e.getEntity(), boss, e.getDamage());
					Bukkit.getPluginManager().callEvent(bossDmg);
					e.setDamage(bossDmg.getDamage());
					if(bossDmg.isCancelled())
					{
						e.setCancelled(true);
					}
				}
				else
					if(mob != null)
					{
						MobDamageByPlayer mobDmg = new MobDamageByPlayer(player, e.getDamager(), mob, e.getDamage());
						Bukkit.getPluginManager().callEvent(mobDmg);
						e.setDamage(mobDmg.getDamage());
						if(mobDmg.isCancelled())
						{
							e.setCancelled(true);
						}
					}
			}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			if(e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
			{
				EntityDamageByEntityEvent lastDmg = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();

				Boss bossMsg = null;

				if(lastDmg.getDamager() instanceof Arrow)
				{
					bossMsg = Mobs.getBoss(((Arrow) lastDmg.getDamager()).getShooter());
				}
				else
				{
					bossMsg = Mobs.getBoss(lastDmg.getDamager());
				}

				if(bossMsg != null)
				{
					PlayerDeathByBossEvent bossDeath = new PlayerDeathByBossEvent(((Player) e.getEntity()), lastDmg.getDamager(), bossMsg);
					Bukkit.getPluginManager().callEvent(bossDeath);
					String message = bossMsg.getDialogs().getKillDialog();

					if(message != null)
					{
						((Player) e.getEntity()).sendMessage("§6[" + bossMsg.getBossType().getName() + "] §c" + message);
					}

					return;
				}
				Mob mob = Mobs.getMob(lastDmg.getDamager());
				if(mob != null)
				{
					PlayerDeathByMobEvent mobDeath = new PlayerDeathByMobEvent(((Player) e.getEntity()), lastDmg.getDamager(), mob);
					Bukkit.getPluginManager().callEvent(mobDeath);
					return;
				}
			}
			return;
		}

		Mob mob = Mobs.getMob(e.getEntity());
		Boss boss = Mobs.getBoss(e.getEntity());

		if(mob != null || boss != null)
		{
			e.getDrops().clear();

			if(((EntityLiving) ((CraftLivingEntity) e.getEntity()).getHandle()).isBaby())
			{
				return;
			}

			double reward = 0;
			ArrayList<ItemStack> drops = null;

			if(mob != null)
			{
				reward = mob.getReward();
				if(e.getEntity().getKiller() != null){
					drops = DropsUtil.getCalculatedDrops(e.getEntity().getKiller(), mob);
				}else{
					drops = DropsUtil.getCalculatedDrops(mob);
				}
			}
			else
			{
				reward = boss.getReward();
				if(e.getEntity().getKiller() != null){
					drops = DropsUtil.getCalculatedDrops(e.getEntity().getKiller(), boss);
				}else{
					drops = DropsUtil.getCalculatedDrops(boss);
				}
				BossConfig.attRespawnTime(boss.getBossType());
				if(BossBarUtil.bossBar.containsKey(e.getEntity()))
				{
					for(String p1 : BossBarUtil.bossBar.get(e.getEntity()))
					{
						if(Bukkit.getPlayer(p1) != null)
						{
							Player p = Bukkit.getPlayer(p1);
							if(BossBar.hasBar(p))
							{
								BossBar.removeBar(p);
							}
						}
					}
					BossBarUtil.bossBar.remove(e.getEntity());
				}

				String message = boss.getDialogs().getDeathDialog();

				if(message != null)
				{
					Bukkit.broadcastMessage(" ");
					Bukkit.broadcastMessage("§6[" + boss.getBossType().getName() + "] §c" + message);
					if(e.getEntity().getKiller() != null)
					{
						Bukkit.broadcastMessage("§6[" + boss.getBossType().getName() + "] §cFoi morto por: " + e.getEntity().getKiller().getName());
					}
					Bukkit.broadcastMessage(" ");
				}
			}

			if(drops == null)
			{
				return;
			}

			for(ItemStack itemStack : drops)
			{
				if(e.getEntity().getFireTicks() > 0)
				{
					switch(itemStack.getType())
					{
					case RAW_BEEF:
						itemStack.setType(Material.COOKED_BEEF);
						break;
					case RAW_CHICKEN:
						itemStack.setType(Material.COOKED_CHICKEN);
						break;
					case RAW_FISH:
						itemStack.setType(Material.COOKED_FISH);
						break;
					case POTATO:
						itemStack.setType(Material.BAKED_POTATO);
						break;
					case PORK:
						itemStack.setType(Material.GRILLED_PORK);
						break;
					default:
						break;
					}
				}
				e.getDrops().add(itemStack);
			}

			if(e.getEntity().getKiller() != null)
			{
				if(boss != null)
				{
					BossDeathEvent bossDeath = new BossDeathEvent(e.getEntity().getKiller(), e.getEntity(), boss);
					Bukkit.getPluginManager().callEvent(bossDeath);
				}
				else
					if(mob != null)
					{
						MobDeathEvent mobDeath = new MobDeathEvent(e.getEntity().getKiller(), e.getEntity(), mob);
						Bukkit.getPluginManager().callEvent(mobDeath);
					}

				Player player = e.getEntity().getKiller();
				Mobs.getEconomy().depositPlayer(player.getName(), reward);

			}
		}
	}

	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent e)
	{
		e.blockList().clear();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e)
	{
		if(e.getEntity().getShooter() == null || !(e.getEntity().getShooter() instanceof Creature))
		{
			return;
		}
		if(Mobs.getMob(e.getEntity().getShooter()) != null)
		{
			Mob mob = Mobs.getMob(e.getEntity().getShooter());
			if(mob.getMobType() == MobType.SKELETON)
			{
				if(mob.getLevel() >= 5)
				{
					boolean life = (((Damageable) e.getEntity().getShooter()).getHealth() <= (((Damageable) e.getEntity().getShooter()).getMaxHealth() / 100) * 30);
					if(life)
					{
						Location loc = e.getEntity().getLocation();
						loc.getWorld().createExplosion(loc, 1.5F);
					}
				}
			}
		}
		else
			if(Mobs.getBoss(e.getEntity().getShooter()) != null)
			{
				if(Mobs.getBoss(e.getEntity().getShooter()).getBossType() == BossType.LUCIFRON || Mobs.getBoss(e.getEntity().getShooter()).getBossType() == BossType.SQUIT)
				{
					boolean life = (((Damageable) e.getEntity().getShooter()).getHealth() <= (((Damageable) e.getEntity().getShooter()).getMaxHealth() / 100) * 30);
					if(life)
					{
						Location loc = e.getEntity().getLocation();
						loc.getWorld().createExplosion(loc, 1.5F);
					}
				}
			}
	}

	@EventHandler
	public void onEntityCreatePortalEvent(EntityCreatePortalEvent e)
	{
		e.setCancelled(true);
	}


	@EventHandler
	public void onBurn(EntityCombustEvent e)
	{
		if(e.getDuration() == 8 && day(e.getEntity().getWorld()) && (Mobs.getBoss(e.getEntity()) != null || Mobs.getMob(e.getEntity()) != null))
		{
			e.setCancelled(true);
		}
	}

	public static boolean day(org.bukkit.World world)
	{
		long time = world.getTime();

		return time < 12300 || time > 23850;
	}
}
