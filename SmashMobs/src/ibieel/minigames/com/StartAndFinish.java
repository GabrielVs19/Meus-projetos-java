package ibieel.minigames.com;

import ibieel.minigames.com.Managers.ClassManager;
import ibieel.minigames.com.Managers.ClassManager.ClassType;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Managers.ScoreboardManager1;
import ibieel.minigames.com.Managers.TimerManager;

import java.util.HashMap;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;

public class StartAndFinish {

	public static void finishTime(){
		Bukkit.broadcastMessage("§c§lO tempo acabou!");
		HashMap<String, Integer> winners = new HashMap<>();
		int vida = -1000;
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			if(PlayerManager.getVidas(p.getName()) > vida){
				winners.clear();
				winners.put(p.getName(), PlayerManager.getVidas(p.getName()));
				vida = PlayerManager.getVidas(p.getName());
				continue;
			}
			if(PlayerManager.getVidas(p.getName()) == vida){
				winners.put(p.getName(), PlayerManager.getVidas(p.getName()));
				continue;
			}
		}
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			if(winners.containsKey(p.getName())){
				CoreD.award(p);
				CoreD.sendToLobby(p, TeleportToLobbyReason.WINNER);
			}else{
				CoreD.sendToLobby(p, TeleportToLobbyReason.LOSER);
			}
		}
	}

	public static void finishWinner(){
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			CoreD.award(p);
			CoreD.sendToLobby(p, TeleportToLobbyReason.WINNER);
		}
	}

	public static void start(){
		TimerManager timer = new TimerManager(1200);
		timer.run();
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			if(!PlayerManager.hasClass(p.getName())){
				ClassType classType = ClassManager.getRandomClass();
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 100, 1);
				PlayerManager pm = new PlayerManager(classType);
				PlayerManager.add(p.getName(), pm);
				p.sendMessage("§9Classe> §cVocê não selecionou nenhuma classe.");
				p.sendMessage("§9Classe> §6Selecionamos uma classe aleatoria para você, a classe: " + ClassManager.getClass(classType).getName() +"§6.");
			}
		}
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			PlayerManager.setVidas(p.getName(), 4);
			p.setLevel(0);
			p.setExp(1.0F);
		}
		ScoreboardManager1.updateScoreboard();
		CoreD.setRunning();
		//TimerManager.craftLandia();
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			p.setFireTicks(0);
			int random = (int) (Math.random() * SmashMobs.spawns.size());
			Location spawn = SmashMobs.spawns.get(random);
			p.teleport(spawn);
			p.setFireTicks(0);
			GiveItems.give(p, PlayerManager.getClasse(p.getName()));
			Disguise d = DisguiseAPI.getDisguise(p);
			if(d != null){
				d.removeDisguise();
			}
			MobDisguise mobdisguise = new MobDisguise(ClassManager.getClass(PlayerManager.getClasse(p.getName())).getDisguiseType());
			DisguiseAPI.disguiseToAll(p, mobdisguise);
			Disguise disguise = DisguiseAPI.getDisguise(p);
			FlagWatcher watcher = disguise.getWatcher();
			ItemStack air = new ItemStack(Material.AIR);
			ItemStack[] armour = {air, air, air, air, air};
			watcher.setArmor(armour);
			disguise.startDisguise();
			if((disguise.getWatcher() instanceof LivingWatcher)) {
				((LivingWatcher) disguise.getWatcher()).setCustomName(p.getName());
				((LivingWatcher) disguise.getWatcher()).setCustomNameVisible(true);
			}
			if(disguise.getType() == DisguiseType.SLIME){
				SlimeWatcher slime = (SlimeWatcher) disguise.getWatcher();
				slime.setSize(3);
			}
		}
		Bukkit.broadcastMessage("§c§l[AVISO] §cPara melhor jogabilidade é recomendável que utilizem as particulas ativadas!");
		Bukkit.broadcastMessage("§7§l[DICA] §7Para ativar: ESC > Opções > Configurações de video > Particulas");
		Bukkit.broadcastMessage("§9Invencibilidade> §eVocê tem 5 segundos de invencibilidade!");
		new BukkitRunnable(){
			@Override
			public void run() {
				CoreD.setDisablePvP(false);
				CoreD.setDisableDamage(false);
				Bukkit.broadcastMessage("§9Invencibilidade> §cAcabou sua invencibilidade, boa sorte!");
			}
		}.runTaskLater(SmashMobs.getInstance(), 20 * 5);
	}

}
