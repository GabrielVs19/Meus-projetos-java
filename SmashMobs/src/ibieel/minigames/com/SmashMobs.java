package ibieel.minigames.com;

import ibieel.minigames.com.Skills.AbaloSismicoSkill;
import ibieel.minigames.com.Skills.AlcateiaSkill;
import ibieel.minigames.com.Skills.AliadosSkill;
import ibieel.minigames.com.Skills.AtiradorCabecasSkill;
import ibieel.minigames.com.Skills.AutoExplosaoSkill;
import ibieel.minigames.com.Skills.AvancoMortiferoSkill;
import ibieel.minigames.com.Skills.BaconSkill;
import ibieel.minigames.com.Skills.BolaNeveSkill;
import ibieel.minigames.com.Skills.BoteSkill;
import ibieel.minigames.com.Skills.ClonagemSkill;
import ibieel.minigames.com.Skills.CongelarSkill;
import ibieel.minigames.com.Skills.DoubleJumpSkill;
import ibieel.minigames.com.Skills.EncolherSkill;
import ibieel.minigames.com.Skills.EscalarParedesSkill;
import ibieel.minigames.com.Skills.EscudoMagicoSkill;
import ibieel.minigames.com.Skills.ExplosaoDeMassaSkill;
import ibieel.minigames.com.Skills.ExplosaoDeOssosSkill;
import ibieel.minigames.com.Skills.FuriaSkill;
import ibieel.minigames.com.Skills.GalinhaExplosivaSkill;
import ibieel.minigames.com.Skills.GosmaVenenosaSkill;
import ibieel.minigames.com.Skills.GranadaSkill;
import ibieel.minigames.com.Skills.InfernoSkill;
import ibieel.minigames.com.Skills.LadraoBlocosSkill;
import ibieel.minigames.com.Skills.LancaChamasSkill;
import ibieel.minigames.com.Skills.MordidaMortalSkill;
import ibieel.minigames.com.Skills.PassosCongelantesSkill;
import ibieel.minigames.com.Skills.PorcoExplosivoSkill;
import ibieel.minigames.com.Skills.PrisaoDeTeiaSkill;
import ibieel.minigames.com.Skills.RajadaFlechasSkill;
import ibieel.minigames.com.Skills.RajadaOvosSkill;
import ibieel.minigames.com.Skills.RegenerarVidaSkill;
import ibieel.minigames.com.Skills.SaltoSismicoSkill;
import ibieel.minigames.com.Skills.TeiaSkill;
import ibieel.minigames.com.Skills.TeleportSkill;
import ibieel.minigames.com.Skills.TerremotoSkill;
import ibieel.minigames.com.Skills.UivoSkill;
import ibieel.minigames.com.Skills.VenenoSkill;
import ibieel.minigames.com.Util.ChatUtil;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.MiniGame;
import de.slikey.effectlib.EffectLib;

public class SmashMobs extends JavaPlugin{

	private static Plugin plugin;
	public static ArrayList<Location> spawns = new ArrayList<>();
	public void onEnable(){
		plugin = this;
		Bukkit.getWorlds().get(0).setTime(0);
		Bukkit.getWorlds().get(0).setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorlds().get(0).setGameRuleValue("mobGriefing", "false");
		CoreD.setAllowJoinSpectators(false);
		CoreD.setAllowTeamChat(true);
		CoreD.setCancelBlockBreakEvent(true);
		CoreD.setCancelBlockPlaceEvent(true);
		CoreD.setDisableDeathScreen(true);
		CoreD.setDisablePvP(true);
		CoreD.setCancelHangingBreakByEntityEvent(true);
		CoreD.setCancelWeatherChangeEvent(true);
		CoreD.setToSpectatorOnRespawn(false);
		CoreD.setStore(false);
		CoreD.setCancelFoodLevelChangeEvent(true);
		CoreD.setCancelPlayerDropItemEvent(true);
		CoreD.setCancelPlayerPickupItemEvent(true);
		CoreD.setDisableDamage(true);
		CoreD.setMiniGame(new MiniGame("Smash Mobs", "start", 7));
		CoreD.setMaxPlayers(15);
		getCommand("start").setExecutor(this);
		getCommand("addspawn").setExecutor(this);
		getCommand("a").setExecutor(this);
		this.registerClasses();
		EscalarParedesSkill.defineNoVineBlocks();
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getServer().getPluginManager().registerEvents(new Menu(this), this);
		anuncioBeacon();
	}

	public static Plugin getInstance(){
		return plugin;
	}

	public static EffectLib getEffectLib(){
		return EffectLib.instance();
	}

	public void anuncioBeacon(){
		new BukkitRunnable(){
			@Override
			public void run() {
				if(CoreD.isRunning()){
					cancel();
				}else{
					for(Player p : Bukkit.getOnlinePlayers()){
						p.sendMessage(" ");
						((CraftPlayer)p).getHandle().sendMessage(ChatUtil.chatComando("§9Aviso> §6Escolha sua classe usando o §b§lBEACON§6.", "§eClique para abrir o menu de classes.", "/a"));
						p.sendMessage(" ");
					}
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 20 * 15);
	}

	private void registerClasses(){
		Classes.addSpider();
		Classes.addBlaze();
		Classes.addChicken();
		Classes.addIronGolem();
		Classes.addSkeleton();
		Classes.addCreeper();
		Classes.addPig();
		Classes.addSlime();
		Classes.addWolf();
		Classes.addPigman();
		//Classes.addEnderman();
		Classes.addWitherSkeleton();
		Classes.addSnowman();
	}

	private void registerEvents(){
		getServer().getPluginManager().registerEvents(new InfernoSkill(this), this);
		getServer().getPluginManager().registerEvents(new RajadaFlechasSkill(this), this);
		getServer().getPluginManager().registerEvents(new RajadaOvosSkill(this), this);
		getServer().getPluginManager().registerEvents(new AbaloSismicoSkill(this), this);
		getServer().getPluginManager().registerEvents(new SaltoSismicoSkill(this), this);
		getServer().getPluginManager().registerEvents(new TeiaSkill(this), this);
		getServer().getPluginManager().registerEvents(new VenenoSkill(this), this);
		getServer().getPluginManager().registerEvents(new GranadaSkill(this), this);
		getServer().getPluginManager().registerEvents(new EscudoMagicoSkill(this), this);
		getServer().getPluginManager().registerEvents(new DoubleJumpSkill(this), this);
		getServer().getPluginManager().registerEvents(new AutoExplosaoSkill(this), this);
		getServer().getPluginManager().registerEvents(new GalinhaExplosivaSkill(this), this);
		getServer().getPluginManager().registerEvents(new ExplosaoDeOssosSkill(this), this);
		getServer().getPluginManager().registerEvents(new BoteSkill(this), this);
		getServer().getPluginManager().registerEvents(new EscalarParedesSkill(this), this);
		getServer().getPluginManager().registerEvents(new TerremotoSkill(this), this);
		getServer().getPluginManager().registerEvents(new TeleportSkill(this), this);
		getServer().getPluginManager().registerEvents(new LadraoBlocosSkill(this), this);
		getServer().getPluginManager().registerEvents(new CongelarSkill(this), this);
		getServer().getPluginManager().registerEvents(new AtiradorCabecasSkill(this), this);
		getServer().getPluginManager().registerEvents(new BolaNeveSkill(this), this);
		getServer().getPluginManager().registerEvents(new PassosCongelantesSkill(this), this);
		getServer().getPluginManager().registerEvents(new AvancoMortiferoSkill(this), this);
		getServer().getPluginManager().registerEvents(new RegenerarVidaSkill(this), this);
		getServer().getPluginManager().registerEvents(new ClonagemSkill(this), this);
		getServer().getPluginManager().registerEvents(new BaconSkill(this), this);
		getServer().getPluginManager().registerEvents(new PorcoExplosivoSkill(this), this);
		getServer().getPluginManager().registerEvents(new ExplosaoDeMassaSkill(this), this);
		getServer().getPluginManager().registerEvents(new GosmaVenenosaSkill(this), this);
		getServer().getPluginManager().registerEvents(new EncolherSkill(this), this);
		getServer().getPluginManager().registerEvents(new UivoSkill(this), this);
		getServer().getPluginManager().registerEvents(new AlcateiaSkill(this), this);
		getServer().getPluginManager().registerEvents(new MordidaMortalSkill(this), this);
		getServer().getPluginManager().registerEvents(new PrisaoDeTeiaSkill(this), this);
		getServer().getPluginManager().registerEvents(new AliadosSkill(this), this);
		getServer().getPluginManager().registerEvents(new LancaChamasSkill(this), this);
		getServer().getPluginManager().registerEvents(new FuriaSkill(this), this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("a")){
			if(!CoreD.isRunning()){
				if(sender instanceof Player){
					((Player)sender).openInventory(Menu.classInventoy());
				}
			}else{
				sender.sendMessage("§cA partida já começou!");
			}
			return true;
		}
		if(sender.isOp()){
			if(commandLabel.equalsIgnoreCase("start")){
				CoreD.setRunning();
				registerEvents();
				StartAndFinish.start();
				return true;
			}
			if(commandLabel.equalsIgnoreCase("addspawn")){
				try{
					World world = Bukkit.getWorlds().get(0);
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					int z = Integer.parseInt(args[2]);

					spawns.add(new Location(world, x, y, z));
					System.out.println("Spawn Adicionado!");	
				}catch(Exception e){
					System.out.println("Coordenadas Invalidas! /addspawn <x> <y> <z>");
				}
				return true;
			}
		}
		return false;
	}

}
