package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;

public class PrisaoDeTeiaSkill implements Listener {

	private Skills skill = Skills.PRISAO_DE_TEIA;
	private SkillType skillType = skill.getSkillType();
	//Hitbox do projetil
	private double hitbox = 0.5;
	//Ticks que a teia vai ficar no mapa
	private int ticks = 70;
	public SmashMobs plugin;
	public PrisaoDeTeiaSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(CoreD.isRunning()){
			if(SkillEventUtil.rightClickShovel(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
				final Player player = e.getPlayer();
				if(!CoreDPlayer.isSpectator(player)){
					if(!CooldownManager.isCooldown(player.getName(), skillType)){
						CooldownManager.cooldown(player.getName(), skill);
						SkillEventUtil.sendUseSkillMessage(player, skill);
						Location loc = player.getEyeLocation();
						Item web = player.getWorld().dropItem(loc, new ItemStack(Material.WEB));
						web.setVelocity(player.getEyeLocation().getDirection().multiply(1.8));
						player.getLocation().getWorld().playEffect(loc, Effect.STEP_SOUND, Material.WEB);
						verificar(player, web);
					}else{
						SkillEventUtil.soundCooldownSkill(player);
					}
				}
			}
		}
	}

	private ArrayList<Player> a = new ArrayList<>();
	public void verificar(final Entity player, final Item entity){
		new BukkitRunnable(){
			int qnt = 0;
			@Override
			public void run() {
				if(entity != null){
					qnt++;
					if(qnt <= ticks){
						entity.getLocation().getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, Material.WEB);
						List<Entity> en = entity.getNearbyEntities(hitbox, hitbox, hitbox);
						final HashMap<Location, Material> blocks = new HashMap<>();
						for(final Entity e : en){
							if(e instanceof Player && e != player){
								a.add(((Player)e));
								Location eloc = e.getLocation();
								int xMax = (int) Math.max(eloc.getX() + 1, eloc.getX() - 1);
								int xMin = (int) Math.min(eloc.getX() + 1, eloc.getX() - 1);
								int yMax = (int) eloc.getY() + 1;
								int yMin = (int) eloc.getY();
								int zMax = (int) Math.max(eloc.getZ() + 1, eloc.getZ() - 1);
								int zMin = (int) Math.min(eloc.getZ() + 1, eloc.getZ() - 1);
								int xMeio = (((xMax - xMin) / 2) + xMin);
								int yMeio = yMin;
								int zMeio = (((zMax - zMin) / 2) + zMin);
								for(int x = xMin;x <= xMax;x++){
									for(int y = yMin;y <= yMax;y++){
										for(int z = zMin;z <= zMax;z++){
											Location a = new Location(eloc.getWorld(), x, y, z);
											if(a.getBlock().getRelative(BlockFace.DOWN).getType() != Material.SOUL_SAND && a.getBlock().getType() == Material.AIR){
												blocks.put(a, a.getBlock().getType());
												a.getBlock().setType(Material.WEB);
												a.getWorld().playEffect(a, org.bukkit.Effect.STEP_SOUND, Material.WEB);
											}
										}
									}
								}
								e.teleport(new Location(e.getWorld(), xMeio, yMeio, zMeio));
								entity.remove();
								cancel();
								new BukkitRunnable(){
									@Override
									public void run() {
										a.remove(((Player)e));
										for(Entry<Location, Material> b : blocks.entrySet()){
											b.getKey().getBlock().setType(b.getValue());
											b.getKey().getWorld().playEffect(b.getKey(), org.bukkit.Effect.STEP_SOUND, Material.WEB);
										}
									}
								}.runTaskLater(SmashMobs.getInstance(), ticks);
							}
						}
					}else{
						entity.remove();
						cancel();
					}
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
	}

}
