package ibieel.eventos.com;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import br.com.tlcm.eventod.EventoD;

public class Chuva implements Listener{

	public Main plugin;
	public Chuva(Main plugin){
		this.plugin = plugin;
	}

	public static void rainBlocks(){
		new BukkitRunnable(){
			int wave;
			double percent;
			public void run() {
				wave++;
				if(Main.getPlayerList().size() == 0){
					cancel();
					finish();
				}
				if(Main.getPlayerList().size() == 1){
					cancel();
					finish();
				}
				for(String p : Main.getPlayerList()){
					try{
						Bukkit.getPlayer(p).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, -5));
						Bukkit.getPlayer(p).sendMessage("�cN�o ser� poss�vel pular durante o evento!");
					}catch(Exception e){}
				}
				percent += 5;
				World world = Bukkit.getWorld(Main.getWorld());
				WorldGuardPlugin wgp = (WorldGuardPlugin)Bukkit.getPluginManager().getPlugin("WorldGuard");
				ProtectedRegion rg = wgp.getRegionManager(world).getRegion(Main.getRegion());
				Bukkit.broadcastMessage("�a[Chuva de Blocos] �6�lRodada: �1�l" + wave);
				int xMin = rg.getMinimumPoint().getBlockX();
				int xMax = rg.getMaximumPoint().getBlockX();
				int yMax = rg.getMaximumPoint().getBlockY();
				int zMin = rg.getMinimumPoint().getBlockZ();
				int zMax = rg.getMaximumPoint().getBlockZ();
				for (int x = xMin; x <= xMax; x++) {
					for (int y = yMax; y <= yMax; y++) {
						for (int z = zMin; z <= zMax; z++){
							double random = ((Math.random() * 99) +1);
							if(random <= percent){
								Block block = world.getBlockAt(x, y, z);
								block.setType(Material.ANVIL);
							}
						}
					}
				}
				clearAnvils(world);
			}
		}.runTaskTimer(Main.plugin, 0, 20 * 5);
	}

	public static void clearAnvils(final World world){
		new BukkitRunnable(){
			@Override
			public void run() {
				WorldGuardPlugin wgp = (WorldGuardPlugin)Bukkit.getPluginManager().getPlugin("WorldGuard");
				ProtectedRegion rg = wgp.getRegionManager(world).getRegion(Main.getRegion());
				int xMin = rg.getMinimumPoint().getBlockX();
				int xMax = rg.getMaximumPoint().getBlockX();
				int zMin = rg.getMinimumPoint().getBlockZ();
				int zMax = rg.getMaximumPoint().getBlockZ();
				int yMin = rg.getMinimumPoint().getBlockY();
				for (int x = xMin; x <= xMax; x++) {
					for (int y = yMin; y <= yMin; y++) {
						for (int z = zMin; z <= zMax; z++){
							Block block = world.getBlockAt(x, y, z);
							block.setType(Material.AIR);
						}
					}
				}
				cancel();
			}
		}.runTaskLater(Main.plugin, 20 * 5);
	}

	public static void start(){
		Main.setRunning(true);
		Bukkit.broadcastMessage("�a[Chuva de Blocos] �6�lIniciando o evento!");
		rainBlocks();
	}

	public static void finish(){
		clearAnvils(Bukkit.getWorld(Main.getWorld()));
		String ganhador = Main.getPlayerList().get(0);
		if(Main.getPlayerList().size() == 1){
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("�d[Evento Autom�tico] " + ganhador + " venceu o evento Chuva de Blocos!");
			Bukkit.broadcastMessage("");
			EventoD eventod = (EventoD)Bukkit.getPluginManager().getPlugin("EventoD");
			eventod.enableEvents();
			if (Main.plugin.getConfig().getBoolean("servers.normais.status")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "money grant " + ganhador + " " + Main.plugin.getConfig().getInt("servers.normais.coins"));
			}
			if (Main.plugin.getConfig().getBoolean("servers.hc.status")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give @" + ganhador + " diamond " + Main.plugin.getConfig().getInt("servers.hc.diamond"));
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "vencedor " + ganhador);
			Main.setRunning(false);
			Bukkit.getPlayer(ganhador).removePotionEffect(PotionEffectType.JUMP);
			Main.getPlayerList().clear();
		}else{
			Bukkit.broadcastMessage("�d[Evento Autom�tico] N�o houve vencedores!");
			Main.setRunning(false);
		}
	}

}
