package br.com.minigame.speedbuilders.manager;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import br.com.minigame.speedbuilders.SpeedBuilders;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;


public class Round
{
	
	
	private static Blaze judge = null;
	private static RoundStatus roundStatus = RoundStatus.START_TIME;
	private static List<BuildField> scoreTop = new ArrayList<BuildField>();
	
	public static RoundStatus getRoundStatus()
	{
		return roundStatus;
	}
	
	public static void setRoundStatus(RoundStatus roundStatus)
	{
		Round.roundStatus = roundStatus;
	}
	
	@SuppressWarnings("deprecation")
	public static void start()
	{
		setRoundStatus(RoundStatus.START_TIME);
		
		new BukkitRunnable()
		{
			
			
			Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
			Location judgeSpawn = new Location(spawn.getWorld(), spawn.getX() + 0.5, spawn.getY() + 10, spawn.getZ() + 0.5);
			
			public void run()
			{
				if(judge == null)
				{
					judgeSpawn.getBlock().getRelative(BlockFace.DOWN).setType(Material.GLASS);
					judge = (Blaze) spawn.getWorld().spawnEntity(judgeSpawn, EntityType.BLAZE);
					judge.setCustomName("§6Juiz");
					judge.setCustomNameVisible(true);
					judge.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999999));
				}
				else
				{
					judge.teleport(judgeSpawn);
				}
			}
		}.runTaskTimer(SpeedBuilders.getInstance(), 5, 5);
		
		if(SpeedBuilders.getSchematics().size() == 0)
		{
			SpeedBuilders.getInstance().loadSchematics();
		}
		
		String picname = SpeedBuilders.getSchematics().get((int) (Math.random() * SpeedBuilders.getSchematics().size()));
		SpeedBuilders.getSchematics().remove(picname);
		
		Picture.set(picname);
		Picture.paste(Bukkit.getWorlds().get(0).getSpawnLocation());
		
		for(BuildField buildField : BuildField.getBuildFieldList())
		{
			buildField.setScore(0);
			buildField.clear();
			Picture.paste(buildField.getLocation());
			buildField.summonPlayers();
		}
		
		updateSidebars(new ArrayList<BuildField>());
		
		SpeedBuilders.deleteCube();
		
		for(Player player : CoreDPlayer.getPlayersWhoArentSpectators())
		{
			player.setAllowFlight(true);
			player.setFlying(true);
			
			player.closeInventory();
			player.getInventory().clear();
			player.updateInventory();
			
		}
		
		Bukkit.broadcastMessage("§d§lMemorize a construção, você terá que reproduzi-la!");
		CoreD.setBossBar("§d§lMemorize a construção, você terá que reproduzi-la!");
		
		new BukkitRunnable()
		{
			
			
			int seconds = 10;
			
			@Override
			public void run()
			{
				if(seconds == 1)
				{
					Picture.clear(Bukkit.getWorlds().get(0).getSpawnLocation());
					for(BuildField buildField : BuildField.getBuildFieldList())
					{
						Picture.clear(buildField.getLocation());
					}
				}
				
				if(seconds == 0)
				{
					draw();
					cancel();
					return;
				}
				
				seconds--;
			}
		}.runTaskTimer(SpeedBuilders.getInstance(), 0, 20);
	}
	
	private static void draw()
	{
		setRoundStatus(RoundStatus.DRAW_TIME);
		
		for(BuildField bf : BuildField.getBuildFieldList())
		{
			bf.getPlayerBlocks().clear();
			
			for(Entry<Vector, BaseBlock> block : Picture.getBlocks().entrySet())
			{
				BaseBlock baseBlock = block.getValue();
				
				int id = baseBlock.getId();
				int data = baseBlock.getData();
				
				if(baseBlock.getNbtId().equalsIgnoreCase("Skull"))
				{
					data = Integer.parseInt(baseBlock.getNbtData().getValue().get("SkullType").getValue() + "");
				}
				
				List<ItemStack> itens = SpeedBuilders.getRightItem(id, 1, data);
				
				for(ItemStack itemStack : itens)
				{
					bf.giveItemPlayers(itemStack);
				}
			}
		}
		
		Bukkit.broadcastMessage("§2§lValendo! Construa o que você viu!");
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			player.playSound(player.getLocation(), Sound.DOOR_OPEN, 100, 100);
		}
		
		SpeedBuilders.toggle(false);
		
		new BukkitRunnable()
		{
			
			
			int seconds = 40;
			int fullTime = seconds;
			
			@Override
			public void run()
			{
				if(seconds < 0)
				{
					for(Entity entity : new ArrayList<Entity>(Bukkit.getWorlds().get(0).getEntities()))
					{
						if(entity.getType() == EntityType.DROPPED_ITEM)
						{
							entity.remove();
						}
					}
					
					SpeedBuilders.toggle(true);
					
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§lAcabou o tempo, o juiz julgará suas construções!!");
					Bukkit.broadcastMessage("");
					
					for(Player online : CoreDPlayer.getPlayersWhoArentSpectators())
					{
						online.getInventory().clear();
						online.closeInventory();
					}
					
					CoreD.removeBossBar();
					score();
					cancel();
					return;
				}
				
				updateBossBar(seconds, fullTime);
				
				seconds--;
			}
		}.runTaskTimer(SpeedBuilders.getInstance(), 20, 20);
	}
	
	private static void updateBossBar(int seconds, int fullTime)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, seconds);
		
		String timeColor = "§a§l";
		
		if(seconds <= 10)
		{
			timeColor = "§c§l";
			
			for(Player player : Bukkit.getOnlinePlayers())
			{
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 1);
			}
		}
		
		String time = timeColor + new SimpleDateFormat("mm:ss").format(cal.getTime());
		
		float percent = seconds * 100 / fullTime;
		
		if(percent > 100)
		{
			percent = 100;
		}
		
		if(percent < 0)
		{
			percent = 0;
		}
		
		CoreD.setBossBar("§6§lTEMPO RESTANTE: " + time, percent);
	}
	
	private static void score()
	{
		setRoundStatus(RoundStatus.SCORE_TIME);
		Picture.paste(Bukkit.getWorlds().get(0).getSpawnLocation());
		
		Bukkit.broadcastMessage("§a§lContabilizando pontos...");
		
		new BukkitRunnable()
		{
			
			
			List<BuildField> buildFieldList = new ArrayList<>(BuildField.getBuildFieldList());
			int count = buildFieldList.size() - 1;
			
			public void run()
			{
				if(count < 0)
				{
					List<BuildField> top = new ArrayList<BuildField>();
					
					for(int i = 0; i < 15; i++)
					{
						BuildField topBF = null;
						
						for(BuildField bf : new ArrayList<BuildField>(BuildField.getBuildFieldList()))
						{
							if(top.contains(bf))
							{
								continue;
							}
							
							if(topBF != null && bf.getScore() < topBF.getScore())
							{
								continue;
							}
							
							topBF = bf;
						}
						
						if(topBF != null)
						{
							top.add(topBF);
						}
					}
					
					scoreTop = top;
					award();
					cancel();
					return;
				}
				
				final BuildField bf = buildFieldList.get(count);
				
				SpeedBuilders.playEffectFromTo(judge.getLocation(), bf.getLocation(), Effect.VILLAGER_THUNDERCLOUD);
				
				if(bf.getPlayerBlocks().isEmpty() && SpeedBuilders.isNotBuildEliminate())
				{
					Bukkit.broadcastMessage("§c" + bf.getPlayersNick() + (bf.getPlayers().length == 1 ? " não construiu nada e foi eliminado!" : " não construiram nada e foram elimidados!"));
					judge.getLocation().setDirection(SpeedBuilders.getDirectionFromTo(judge.getLocation(), bf.getLocation()));
					SpeedBuilders.launchProjectile(judge, Fireball.class, bf.getLocation());
					new BukkitRunnable()
					{
						
						
						public void run()
						{
							bf.unregister(true);
						}
					}.runTaskLater(SpeedBuilders.getInstance(), 10);
				}
				else
				{
					bf.setScore(Picture.verify(bf.getLocation()));
					bf.sendMessagePlayers((bf.getScore() == 100 ? "§6§lPARABENS!!! " : "") + "§bVocê conseguiu construir §a" + bf.getScore() + "% §bda figura");
				}
				
				updateSidebars(BuildField.getBuildFieldList());
				count--;
				
			}
		}.runTaskTimer(SpeedBuilders.getInstance(), 70, 70);
	}
	
	private static void award()
	{
		setRoundStatus(RoundStatus.AWARD_TIME);
		
		if(scoreTop.size() == 0)
		{
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§4§lNão houve vencedores");
			Bukkit.broadcastMessage("");
			Bukkit.shutdown();
			return;
		}
		
		updateSidebars(scoreTop);
		
		List<BuildField> scoreTop = new ArrayList<>(Round.scoreTop);
		
		List<BuildField> bests = new ArrayList<BuildField>();
		
		int scoreBest = 0;
		
		for(BuildField buildField : scoreTop)
		{
			if(buildField.getScore() < scoreBest)
			{
				break;
			}
			
			bests.add(buildField);
			scoreBest = buildField.getScore();
		}
		
		for(BuildField best : bests)
		{
			Bukkit.broadcastMessage("§6" + best.getPlayersNick() + (best.getPlayers().length == 1 ? " teve" : " tiveram") + " a melhor construção da rodada!");
		}
		
		if(scoreTop.size() > bests.size())
		{
			Collections.reverse(scoreTop);
			final List<BuildField> worses = new ArrayList<BuildField>();
			int scoreWorse = 100;
			
			for(BuildField buildField : scoreTop)
			{
				if(buildField.getScore() > scoreWorse)
				{
					break;
				}
				
				worses.add(buildField);
				scoreWorse = buildField.getScore();
			}
			
			new BukkitRunnable()
			{
				
				
				int count = worses.size() - 1;
				
				public void run()
				{
					if(count >= 0)
					{
						final BuildField worse = worses.get(count);
						Bukkit.broadcastMessage("§4" + worse.getPlayersNick() + (worse.getPlayers().length == 1 ? " teve" : " tiveram") + " a pior construção da rodada!");
						
						judge.getLocation().setDirection(SpeedBuilders.getDirectionFromTo(judge.getLocation(), worse.getLocation()));
						SpeedBuilders.launchProjectile(judge, Fireball.class, worse.getLocation());
						new BukkitRunnable()
						{
							
							
							public void run()
							{
								worse.unregister(true);
							}
						}.runTaskLater(SpeedBuilders.getInstance(), 20 * 1);
						
						count--;
					}
					else
					{
						finish();
						cancel();
						return;
					}
				}
			}.runTaskTimer(SpeedBuilders.getInstance(), 1 * 20, 1 * 20);
		}
		else
		{
			Bukkit.broadcastMessage("§4Não houve perdedores essa rodada!");
			finish();
		}
	}
	
	private static void finish()
	{
		new BukkitRunnable()
		{
			
			
			public void run()
			{
				if(BuildField.getBuildFieldList().size() == 1)
				{
					BuildField bfWinner = BuildField.getBuildFieldList().get(0);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§6§l" + bfWinner.getPlayersNick() + (bfWinner.getPlayers().length == 1 ? " é o grande vencedor!" : " são os grandes vencedores!"));
					Bukkit.broadcastMessage("");
					
					for(Player player : Bukkit.getOnlinePlayers())
					{
						if(Arrays.asList(bfWinner.getPlayers()).contains(player.getName()))
						{
							CoreD.sendToLobby(player, TeleportToLobbyReason.WINNER);
						}
						else
						{
							CoreD.sendToLobby(player, TeleportToLobbyReason.LOSER);
						}
					}
				}
				else
				{
					Picture.clear(Bukkit.getWorlds().get(0).getSpawnLocation());
					start();
				}
				return;
			}
		}.runTaskLater(SpeedBuilders.getInstance(), 20 * 3);
	}
	
	private static void updateSidebars(List<BuildField> scores)
	{
		for(BuildField buildField : BuildField.getBuildFieldList())
		{
			buildField.setSideBar(scores);
		}
		
		setSideBarsSpectators(scores);
	}
	
	@SuppressWarnings("deprecation")
	private static void setSideBarsSpectators(List<BuildField> scores)
	{
		if(CoreDPlayer.getSpectators() == null || CoreDPlayer.getSpectators().size() == 0)
		{
			return;
		}
		
		Player player = Bukkit.getPlayer(CoreDPlayer.getSpectators().get(0));
		
		if(player == null)
		{
			return;
		}
		
		Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		
		if(objective != null)
		{
			objective.unregister();
		}
		
		objective = player.getScoreboard().registerNewObjective("sbstatus", "dummy");
		objective.setDisplayName("§6§lSpeedBuilders");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(int i = 0; i < scores.size(); i++)
		{
			BuildField bf = scores.get(i);
			
			String toAdd = bf.getPlayersNick();
			int score = bf.getScore();
			
			if(score == 0)
			{
				continue;
			}
			
			OfflinePlayer of = null;
			if(toAdd.length() > 16)
			{
				Team team = objective.getScoreboard().getTeam(toAdd.substring(0, 16));
				
				if(team == null)
				{
					team = objective.getScoreboard().registerNewTeam(toAdd.substring(0, 16));
					team.setPrefix(team.getName());
				}
				
				if(toAdd.length() > 32)
				{
					team.setSuffix(toAdd.substring(32, toAdd.length()));
					of = Bukkit.getOfflinePlayer(toAdd.substring(16, toAdd.length() - 16));
				}
				else
				{
					of = Bukkit.getOfflinePlayer(toAdd.substring(16, toAdd.length()));
				}
				
				team.addPlayer(of);
			}
			else
			{
				of = Bukkit.getOfflinePlayer(toAdd);
			}
			
			if(of != null)
			{
				objective.getScore(of).setScore(score);
			}
		}
	}
	
	public enum RoundStatus
	{
		START_TIME,
		DRAW_TIME,
		SCORE_TIME,
		AWARD_TIME,
		FINISH_TIME;
	}
}
