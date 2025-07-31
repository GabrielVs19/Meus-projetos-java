package br.com.minigame.speedbuilders;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import br.com.minigame.speedbuilders.listeners.Listeners;
import br.com.minigame.speedbuilders.listeners.PreListeners;
import br.com.minigame.speedbuilders.manager.BuildField;
import br.com.minigame.speedbuilders.manager.Round;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.MiniGame;


public class SpeedBuilders extends JavaPlugin implements CommandExecutor
{
	
	
	private static SpeedBuilders instance;
	
	private static int radius = 0;
	private static int deep = 0;
	private static List<Block> cube = new ArrayList<Block>();
	
	private static List<String> schematics = new ArrayList<String>();
	
	private static boolean teamMode;
	private Map<String, String> dupla = new HashMap<String, String>();
	
	private static boolean notBuildEliminate = false;
	
	public static SpeedBuilders getInstance()
	{
		return instance;
	}
	
	public static List<String> getSchematics()
	{
		return schematics;
	}
	
	public static boolean isTeamMode()
	{
		return teamMode;
	}
	
	public static boolean isNotBuildEliminate()
	{
		return notBuildEliminate;
	}
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		getConfig().set("dupla", getConfig().get("dupla", false));
		getConfig().set("notBuildEliminate", getConfig().get("notBuildEliminate", false));
		saveConfig();
		
		teamMode = getConfig().getBoolean("dupla");
		new PreListeners(this);
		
		loadSchematics();
		
		toggle(true);
		CoreD.setCancelBlockBreakEvent(true);
		CoreD.setAllowTeamChat(true);
		CoreD.setCancelPlayerDropItemEvent(true);
		CoreD.setCancelWeatherChangeEvent(true);
		CoreD.setCancelFoodLevelChangeEvent(true);
		CoreD.setCancelPlayerDropItemEvent(true);
		CoreD.setDisableDamage(true);
		CoreD.setMaxPlayers(12);
		CoreD.setMiniGame(new MiniGame("SpeedBuilders", "start", isTeamMode() ? 16 : 8));
		
		notBuildEliminate = getConfig().getBoolean("notBuildEliminate");
		
		if(isTeamMode())
		{
			CoreD.setAllowTeamChat(true);
			CoreD.setMaxPlayers(24);
			
			CoreD.getMiniGame().setBungeeName("d-speedbuilders");
		}
		else
		{
			CoreD.getMiniGame().setFastStart(true);
		}
		
		getCommand("addspawn").setExecutor(this);
		getCommand("start").setExecutor(this);
		getCommand("setradius").setExecutor(this);
		getCommand("setdeep").setExecutor(this);
		generateCube();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender.isOp())
		{
			if(commandLabel.equalsIgnoreCase("start"))
			{
				CoreD.setRunning();
				configureMiniGame();
				
				new BukkitRunnable()
				{
					
					
					public void run()
					{
						new Listeners(SpeedBuilders.getInstance());
						Round.start();
					}
				}.runTaskLater(this, 20);
				return true;
			}
			
			if(commandLabel.equalsIgnoreCase("addspawn") && args.length == 3)
			{
				if(radius == 0)
				{
					System.out.println("Você deve atribuir o valor do raio primeiro!");
					return false;
				}
				
				if(deep == 0)
				{
					System.out.println("Você deve atribuir o valor da profundidade primeiro!");
				}
				
				try
				{
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					int z = Integer.parseInt(args[2]);
					Location loc = new Location(Bukkit.getWorlds().get(0), x, y, z);
					
					BuildField.getBuildFieldList().add(new BuildField(loc, radius, deep));
					System.out.println("Spawn adicionado!");
				}
				catch(Exception e)
				{
					System.out.println("Coordenadas Inválidas!");
					return false;
				}
				
				return true;
			}
			
			if(commandLabel.equalsIgnoreCase("setradius") && args.length == 1)
			{
				try
				{
					radius = Integer.parseInt(args[0]);
					System.out.println("Raio alterado!");
				}
				catch(Exception e)
				{
					System.out.println("Raio Inválido!");
					return false;
				}
				
				return true;
			}
			
			if(commandLabel.equalsIgnoreCase("setdeep") && args.length == 1)
			{
				try
				{
					deep = Integer.parseInt(args[0]);
					System.out.println("Deep alterado!");
				}
				catch(Exception e)
				{
					System.out.println("Deep Inválido!");
					return false;
				}
				
				return true;
			}
		}
		
		Player player = (Player) sender;
		
		if(commandLabel.equalsIgnoreCase("dupla"))
		{
			if(!isTeamMode())
			{
				player.sendMessage("§c» Apenas o SpeedBuilders DUPLA tem esse comando.");
				return false;
			}
			
			if(args.length != 1)
			{
				player.sendMessage("§cUtilize: /dupla <nick>");
				return false;
			}
			
			Team team = player.getScoreboard().getPlayerTeam(player);
			
			if(team != null)
			{
				player.sendMessage("§cVocê já faz parte de uma dupla!");
				return false;
			}
			
			String nick = args[0];
			Player target = Bukkit.getPlayer(nick);
			
			if(target == null)
			{
				player.sendMessage("§cNinguém encontrado com esse nick!");
				return false;
			}
			
			if(player == target)
			{
				player.sendMessage("§cVocê não pode convidar você mesmo!");
				return false;
			}
			
			Team teamTarget = target.getScoreboard().getPlayerTeam(target);
			
			if(teamTarget != null)
			{
				player.sendMessage("§c" + target.getName() + " já faz parte de uma dupla!");
				return false;
			}
			
			if(dupla.containsKey(player.getName()))
			{
				if(dupla.get(player.getName()).equalsIgnoreCase(target.getName()))
				{
					player.sendMessage("§cVocê já convidou " + target.getName() + " para uma dupla!");
					return false;
				}
				
				Player oldTarget = Bukkit.getPlayer(dupla.get(player.getName()));
				
				if(oldTarget != null)
				{
					oldTarget.sendMessage("§c» " + player.getName() + " cancelou o convite de dupla.");
				}
			}
			
			if(dupla.containsKey(target.getName()))
			{
				if(dupla.get(target.getName()).equalsIgnoreCase(player.getName()))
				{
					String name = player.getName() + ":" + target.getName();
					
					if(name.length() > 16)
					{
						name = name.substring(0, 14);
					}
					
					Team duplaT = Bukkit.getScoreboardManager().getNewScoreboard().registerNewTeam(name);
					duplaT.setAllowFriendlyFire(false);
					duplaT.setPrefix("§a[Dupla] ");
					duplaT.addPlayer(player);
					duplaT.addPlayer(target);
					player.setScoreboard(duplaT.getScoreboard());
					target.setScoreboard(duplaT.getScoreboard());
					
					player.sendMessage("§a» Dupla formada com " + target.getName() + ". Boa sorte!");
					target.sendMessage("§a» Dupla formada com " + player.getName() + ". Boa sorte!");
					
					dupla.remove(player.getName());
					dupla.remove(target.getName());
					
					return true;
				}
			}
			
			dupla.put(player.getName(), target.getName());
			player.sendMessage("§a» Convite de dupla enviado para " + target.getName());
			target.sendMessage("\n§a» " + player.getName() + " te convidou para uma dupla!\nDigite §2/dupla " + player.getName() + " §apara aceitar.\n\n ");
			
			return true;
		}
		return false;
	}
	
	public static void toggle(boolean b)
	{
		CoreD.setCancelBlockPlaceEvent(b);
		CoreD.setCancelHangingBreakByEntityEvent(b);
		CoreD.setCancelPlayerBucketEmptyEvent(b);
		CoreD.setCancelPlayerBucketFillEvent(b);
		CoreD.setCancelPlayerInteractEntityEvent(b);
		CoreD.setCancelPlayerInteractEvent(b);
		CoreD.setCancelPlayerPickupItemEvent(b);
	}
	
	protected void configureMiniGame()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(BuildField.getBuildField(player.getName()) != null)
			{
				continue;
			}
			
			BuildField bf = BuildField.getNextEmptyBuildField();
			
			if(bf == null)
			{
				System.out.println("Número de jogadores maior que o numero de arenas!");
				CoreD.sendToLobby(player, "Infelizmente não sobrou arenas para você");
				continue;
			}
			Scoreboard scoreboard = player.getScoreboard();
			
			if(isTeamMode())
			{
				Team teamPlayer = scoreboard.getPlayerTeam(player);
				Team duplaTeam = null;
				
				if(teamPlayer == null)
				{
					scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
					duplaTeam = scoreboard.registerNewTeam(bf.getLocation().getBlockX() + ":" + bf.getLocation().getBlockY() + ":" + bf.getLocation().getBlockZ());
					duplaTeam.setPrefix("§a[Dupla] ");
					duplaTeam.setAllowFriendlyFire(false);
					duplaTeam.addPlayer(player);
					
					Player player2 = null;
					
					for(Player p2 : Bukkit.getOnlinePlayers())
					{
						if(player == p2)
						{
							continue;
						}
						
						Team teamPlayer2 = p2.getScoreboard().getPlayerTeam(p2);
						
						if(teamPlayer2 == null)
						{
							player2 = p2;
							break;
						}
					}
					
					if(player2 != null)
					{
						duplaTeam.addPlayer(player2);
						
						player.sendMessage("§a» Dupla formada com " + player2.getName() + ". Boa sorte!");
						player2.sendMessage("§a» Dupla formada com " + player.getName() + ". Boa sorte!");
					}
					else
					{
						player.sendMessage("§a» Você não teve ninguem para fazer dupla. Boa sorte!");
					}
				}
				else
				{
					duplaTeam = teamPlayer;
				}
				
				String names = "";
				
				for(OfflinePlayer oP : duplaTeam.getPlayers())
				{
					Player p = Bukkit.getPlayer(oP.getName());
					
					if(p != null)
					{
						names += p.getName() + " e ";
						p.setScoreboard(scoreboard);
					}
				}
				
				bf.setPlayersNick(names.substring(0, names.length() - 3));
			}
			else
			{
				scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
				bf.setPlayersNick(player.getName());
				
				Player p = Bukkit.getPlayer(player.getName());
				
				if(p != null)
				{
					p.setScoreboard(scoreboard);
				}
			}
		}
		
		for(BuildField bf : new ArrayList<BuildField>(BuildField.getBuildFieldList()))
		{
			if(bf.getPlayersNick() == null)
			{
				bf.unregister(false);
			}
		}
	}
	
	protected void generateCube()
	{
		Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
		Location loc = new Location(spawn.getWorld(), spawn.getX(), spawn.getY() + 40, spawn.getZ());
		Block block = loc.getBlock();
		
		for(int m = 0; m < 20; m++)
		{
			for(int n = 0; n < 20; n++)
			{
				if((m == 0) || (n == 0) || (m == 20 - 1) || (n == 20 - 1))
				{
					int o = m - 20 / 2;
					int p = n - 20 / 2;
					int[] xyz = {block.getLocation().getBlockX() + o, block.getLocation().getBlockY(), block.getLocation().getBlockZ() + p};
					
					for(int i = block.getY(); i < block.getY() + 10; i++)
					{
						Block bb = block.getLocation().getWorld().getBlockAt(xyz[0], i, xyz[2]);
						bb.setType(Material.GLASS);
						cube.add(bb);
					}
				}
			}
		}
		
		for(int x = block.getX() - 10; x < block.getX() + 10; x++)
		{
			for(int z = block.getZ() - 10; z < block.getZ() + 10; z++)
			{
				Block bb = block.getLocation().getWorld().getBlockAt(x, block.getY(), z);
				bb.setType(Material.GLASS);
				cube.add(bb);
			}
		}
	}
	
	public static void deleteCube()
	{
		for(Block block : cube)
		{
			block.setType(Material.AIR);
		}
		cube.clear();
	}
	
	public void loadSchematics()
	{
		schematics.clear();
		
		File dir = new File(getDataFolder(), "/Pictures/");
		
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		for(File file : dir.listFiles())
		{
			if(!file.getPath().endsWith(".schematic"))
			{
				continue;
			}
			
			schematics.add(file.getName());
		}
		
		if(getSchematics().isEmpty())
		{
			getLogger().log(Level.INFO, "Nenhuma schematic encontrada...");
			Bukkit.shutdown();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static List<ItemStack> getRightItem(int id, int amount, int data)
	{
		List<ItemStack> rightItens = new ArrayList<ItemStack>();
		
		switch(id)
		{
			case 78:
				amount += data;
				break;
			case 17:
				data %= 4;
				break;
			case 44:
				data %= 8;
				break;
			case 126:
				data %= 6;
				break;
			case 170:
				data = 0;
				break;
			case 61:
				data = 0;
				break;
			case 62:
				data = 0;
				break;
			case 53:
				data = 0;
				break;
			case 67:
				data = 0;
				break;
			case 108:
				data = 0;
				break;
			case 109:
				data = 0;
				break;
			case 114:
				data = 0;
				break;
			case 128:
				data = 0;
				break;
			case 134:
				data = 0;
				break;
			case 135:
				data = 0;
				break;
			case 136:
				data = 0;
				break;
			case 156:
				data = 0;
				break;
			case 162:
				data %= 2;
				break;
			case 163:
				data = 0;
				break;
			case 164:
				data = 0;
				break;
			case 155:
				if(data == 3 || data == 4)
				{
					data = 2;
				}
				break;
			case 43:
				id = 44;
				amount++;
				break;
			case 181:
				id = 182;
				amount++;
				break;
			case 125:
				id = 126;
				amount++;
				break;
			case 140:
				id = 390;
				switch(data)
				{
					case 1:
						rightItens.add(new ItemStack(38, 1, (short) 0));
						break;
					case 2:
						rightItens.add(new ItemStack(37, 1, (short) 0));
						break;
					case 3:
						rightItens.add(new ItemStack(6, 1, (short) 0));
						break;
					case 4:
						rightItens.add(new ItemStack(6, 1, (short) 1));
						break;
					case 5:
						rightItens.add(new ItemStack(6, 1, (short) 2));
						break;
					case 6:
						rightItens.add(new ItemStack(6, 1, (short) 3));
						break;
					case 7:
						rightItens.add(new ItemStack(40, 1, (short) 0));
						break;
					case 8:
						rightItens.add(new ItemStack(39, 1, (short) 0));
						break;
					case 9:
						rightItens.add(new ItemStack(81, 1, (short) 0));
						break;
					case 10:
						rightItens.add(new ItemStack(31, 1, (short) 0));
						break;
					case 11:
						rightItens.add(new ItemStack(31, 1, (short) 2));
						break;
					case 12:
						rightItens.add(new ItemStack(6, 1, (short) 4));
						break;
					case 13:
						rightItens.add(new ItemStack(6, 1, (short) 5));
						break;
				}
				data = 0;
				break;
			case 83:
				id = 338;
				break;
			case 115:
				id = 372;
				break;
			case 59:
				id = 295;
				break;
			case 104:
				id = 361;
				break;
			case 105:
				id = 362;
				break;
			case 127:
				id = 351;
				data = 3;
				break;
			case 141:
				id = 391;
				break;
			case 142:
				id = 392;
				break;
			case 55:
				id = 331;
				break;
			case 64:
				id = 324;
				break;
			case 71:
				id = 330;
				break;
			case 75:
				id = 76;
				break;
			case 93:
				id = 356;
				break;
			case 94:
				id = 356;
				break;
			case 117:
				id = 379;
				break;
			case 118:
				id = 380;
				break;
			case 74:
				id = 73;
				break;
			case 68:
				id = 323;
				break;
			case 63:
				id = 323;
				break;
			case 26:
				id = 355;
				break;
			case 92:
				id = 354;
				break;
			case 124:
				id = 123;
				break;
			case 132:
				id = 131;
				break;
			case 144:
				id = 397;
				break;
			case 149:
				id = 404;
				break;
			case 150:
				id = 404;
				break;
			case 145:
				data /= 4;
		}
		
		rightItens.add(new ItemStack(id, amount, (short) data));
		
		return rightItens;
	}
	
	public static void launchProjectile(LivingEntity entity, Class<? extends Projectile> projectile, Location target)
	{
		Location l = entity.getLocation();
		
		org.bukkit.util.Vector from = new org.bukkit.util.Vector(l.getX(), l.getY(), l.getZ());
		org.bukkit.util.Vector to = new org.bukkit.util.Vector(target.getX(), target.getY(), target.getZ());
		
		org.bukkit.util.Vector vector = to.subtract(from);
		
		entity.launchProjectile(Fireball.class, vector.multiply(0.3));
	}
	
	public static void playEffectFromTo(Location loc1, Location loc2, Effect effect)
	{
		double distance = loc1.distance(loc2);
		double x = (loc2.getX() - loc1.getX()) / distance;
		double y = (loc2.getY() - loc1.getY()) / distance;
		double z = (loc2.getZ() - loc1.getZ()) / distance;
		
		while(loc1.getBlockX() != loc2.getBlockX() || loc1.getBlockY() != loc2.getBlockY() || loc1.getBlockZ() != loc2.getBlockZ())
		{
			if(loc1.getBlockX() != loc2.getBlockX())
			{
				loc1.add(x, 0, 0);
			}
			
			if(loc1.getBlockY() != loc2.getBlockY())
			{
				loc1.add(0, y, 0);
			}
			
			if(loc1.getBlockZ() != loc2.getBlockZ())
			{
				loc1.add(0, 0, z);
			}
			
			loc1.getWorld().playSound(loc1, Sound.LAVA, 100, 100);
			loc1.getWorld().playEffect(loc1, effect, 1000);
			loc1.getWorld().playEffect(loc1, effect, 1000);
			loc1.getWorld().playEffect(loc1, effect, 1000);
		}
	}
	
	public static Vector getDirectionFromTo(Location from, Location to)
	{
		Vector vFrom = new Vector(from.getBlockX(), from.getBlockY(), to.getBlockZ());
		Vector vTo = new Vector(to.getBlockX(), to.getBlockY(), to.getBlockZ());
		
		return vTo.subtract(vFrom);
	}
}
