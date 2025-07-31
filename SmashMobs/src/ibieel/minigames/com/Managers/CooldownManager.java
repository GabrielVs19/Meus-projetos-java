package ibieel.minigames.com.Managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;
import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Util.Cooldown;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;
import me.confuser.barapi.BarAPI;

public class CooldownManager {

	public static ArrayList<Cooldown> cooldowns = new ArrayList<>();

	public static void cooldown(String player, Skills skill){
		if(getCooldown(player, skill.getSkillType()) == null){
			Cooldown cooldown = new Cooldown(player, skill);
			cooldowns.add(cooldown);
		}
		runCooldown(player, skill.getSkillType(), skill, skill.getCooldown());
	}

	private static void runCooldown(final String player, final SkillType skillType, final Skills skill, final double seconds){
		new BukkitRunnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				try{
					Player p = Bukkit.getPlayer(player);
					if(p == null){
						cancel();
						return;
					}
					getCooldown(player, skillType).setTime(getCooldown(player, skillType).getTime() + 0.1);
					if(getCooldown(player, skillType).getTime() >= skill.getCooldown()){
						removeCooldown(player, skillType);
						Bukkit.getPlayer(player).setLevel(0);
						Bukkit.getPlayer(player).setExp(1.0F);
						BarAPI.removeBar(p);
						cancel();
						if(skillType != SkillType.DOUBLE_JUMP){
							p.sendMessage("§9Skill> §7Sua skill §a" + skill.getName() + " §7está pronta para uso.");
						}
					}
					float percent = (float) (((seconds - getCooldown(player, skillType).getTime()) * 100) / seconds);
					ItemStack itemStack = p.getItemInHand();
					String message = "§9§l" + skill.getName();
					if((skillType == SkillType.DOUBLE_JUMP && (BarAPI.getMessage(p).isEmpty() || BarAPI.getMessage(p).equalsIgnoreCase(message))) || (itemStack != null && itemStack.getType() == getItem(skillType))){
						CoreD.setBossBar(p, message, (percent <= 0 || percent > 100 ? 0 : percent));
					}else{
						if(skillType != SkillType.DOUBLE_JUMP){
							BarAPI.removeBar(p);
						}
					}
				}catch(Exception e){
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 1, 1);
	}

	public static Material getItem(SkillType skillType){
		Material a = null;
		switch(skillType){
		case SWORD:
			a = Material.IRON_SWORD;
			break;
		case AXE:
			a = Material.IRON_AXE;
			break;
		case SHOVEL:
			a = Material.IRON_SPADE;
			break;
		default:
			a = Material.AIR;
			break;
		}
		return a;
	}

	private static void removeCooldown(String player, SkillType skillType){
		cooldowns.remove(getCooldown(player, skillType));
	}

	public static boolean isCooldown(String player, SkillType skillType){
		if(getCooldown(player, skillType) != null){
			return true;
		}else{
			return false;
		}
	}

	public static List<Cooldown> getCooldown(String playerName){
		List<Cooldown> cooldownList = new ArrayList<Cooldown>();
		for(Cooldown cooldown : new ArrayList<Cooldown>(cooldowns)){
			if(cooldown.getPlayer().equalsIgnoreCase(playerName)){
				cooldownList.add(cooldown);
			}
		}
		return cooldownList;
	}

	public static List<Cooldown> getCooldown(SkillType skillType){
		List<Cooldown> cooldownList = new ArrayList<Cooldown>();
		for(Cooldown cooldown : new ArrayList<Cooldown>(cooldowns)){
			if(cooldown.getSkill().getSkillType() == skillType){
				cooldownList.add(cooldown);
			}
		}
		return cooldownList;
	}

	public static Cooldown getCooldown(String playerName, SkillType skillType){
		for(Cooldown cooldown : new ArrayList<Cooldown>(cooldowns)){
			if(cooldown.getPlayer().equalsIgnoreCase(playerName) && cooldown.getSkill().getSkillType() == skillType){
				return cooldown;
			}
		}
		return null;
	}
}


