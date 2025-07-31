package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class PassosCongelantesSkill implements Listener {

	private Skills skill = Skills.PASSOS_CONGELANTES;
	private SkillType skillType = skill.getSkillType();
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	//Quantidade de ticks que vai ficar com a habilidade ativa
	int ticks = 80;
	public SmashMobs plugin;
	public PassosCongelantesSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				CooldownManager.cooldown(player.getName(), skill);
				UseSkillUtil.add(player, skill, true);
				new BukkitRunnable(){
					@Override
					public void run() {
						UseSkillUtil.remove(player, skill, true);
						for(Location loc : blocks){
							loc.getBlock().setType(Material.AIR);
						}
						cancel();
					}
				}.runTaskLater(SmashMobs.getInstance(), ticks);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
	}

	private ArrayList<Location> blocks = new ArrayList<>();

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(SkillEventUtil.playerHasSkill(e.getPlayer(), skill) && UseSkillUtil.using(e.getPlayer(), skill)){
			Location eloc = e.getPlayer().getLocation();
			int xMax = (int) Math.max(eloc.getX() + 1, eloc.getX() - 1);
			int xMin = (int) Math.min(eloc.getX() + 1, eloc.getX() - 1);
			int yMax = (int) eloc.getY() - 1;
			int yMin = (int) eloc.getY() - 1;
			int zMax = (int) Math.max(eloc.getZ() + 1, eloc.getZ() - 1);
			int zMin = (int) Math.min(eloc.getZ() + 1, eloc.getZ() - 1);
			for(int x = xMin;x <= xMax;x++){
				for(int y = yMin;y <= yMax;y++){
					for(int z = zMin;z <= zMax;z++){
						Location loc1 = new Location(eloc.getWorld(), x, y, z);
						if(loc1.getBlock().getType() == Material.AIR){
							loc1.getBlock().setType(Material.ICE);
							blocks.add(loc1);
							loc1.getWorld().playEffect(loc1, org.bukkit.Effect.STEP_SOUND, Material.ICE);
						}
					}
				}
			}
		}
	}

}
