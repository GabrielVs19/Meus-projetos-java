package ibieel.minigames.com;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;

public class StartAndFinish {

	public static void start(){
		Bukkit.broadcastMessage("§6Você tem 5 minutos para fazer o máximo de comidas que conseguir e colocar em seu bau.");
		Bukkit.broadcastMessage("§aEspalhados pelo mapa existem mesas de trabalhos, fornalhas e os baus para você colocar suas comidas.");
		Bukkit.broadcastMessage("§bNo mapa existem também lagos, você pode pescar lá.");
		Bukkit.broadcastMessage("§9Utilize o comando §a/comidas §9para visualizar todos os tipos de comidas que contam pontos e sua pontuação.");
		CoreD.setCancelBlockBreakEvent(false);
		CoreD.setDisablePvP(false);
		CoreD.setCancelPlayerInteractEvent(false);
		CoreD.setDisableDamage(false);
		CoreD.setCancelPlayerDropItemEvent(true);
		Main.useEnderChest = true;
		starterKit();
		for(Player p : Bukkit.getOnlinePlayers()){
			p.getEnderChest().clear();
		}
		for(Entity e : Bukkit.getWorlds().get(0).getEntities()){
			if(!(e instanceof Player)){
				e.remove();
			}
		}
		MobsManager.spawn();
		Timer.countDownTimer();
		anuncio();
	}

	public static void anuncio(){
		new BukkitRunnable(){
			public void run() {
				Bukkit.broadcastMessage("§d[CookingMama] Lembrem-se: Só vão ser contabilizados os pontos de comidas colocadas no bau!");
			}
			
		}.runTaskTimer(Main.getInstance(), 0, 20 * 60);
	}
	
	public static void finalizar(){
		Entry<String, Double> vencedor = null;
		double valueTop = -10000;
		for(Entry<String, Double> top : CountPoints.getTop15().entrySet()){
			if(top.getValue() >= valueTop){
				valueTop = top.getValue();
				vencedor = top;
			}
		}
		if(vencedor == null){
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage("§b§lNão houve vencedores!");
			Bukkit.broadcastMessage(" ");
			for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
				CoreD.sendToLobby(p, TeleportToLobbyReason.LOSER);
			}
			return;
		}
		double pontos = vencedor.getValue(); 
		BigDecimal bd = new BigDecimal(pontos).setScale(2, RoundingMode.HALF_EVEN); 
		pontos = bd.doubleValue();
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage("§b" + vencedor.getKey() + " §6é o vencedor com §b" + pontos + " pontos§6!");
		Bukkit.broadcastMessage(" ");
		final Entry<String, Double> vencedor1 = vencedor;
		new BukkitRunnable(){
			public void run() {
				for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
					if(p.getName().equalsIgnoreCase(vencedor1.getKey())){
						CoreD.sendToLobby(p, TeleportToLobbyReason.WINNER);
						CoreD.award(p);
					}else{
						CoreD.sendToLobby(p, TeleportToLobbyReason.LOSER);
					}
				}
				cancel();
			}
		}.runTaskLater(Main.instance, 20 * 6);
	}

	private static void starterKit(){
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			ItemStack axe = new ItemStack(Material.STONE_AXE, 1);
			ItemMeta axeMeta = axe.getItemMeta();
			axeMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
			axeMeta.addEnchant(Enchantment.DURABILITY, 50, true);
			axe.setItemMeta(axeMeta);
			p.getInventory().addItem(new ItemStack[]{axe});
			ItemStack fishingRod = new ItemStack(Material.FISHING_ROD, 1);
			p.getInventory().addItem(new ItemStack[]{fishingRod});
		}
	}

}
