package br.com.gvs.GunGame;

import java.util.HashMap;

import org.bukkit.entity.Player;

/**
 * @author Gabriel
 *
 */
public class PlayerManager {

	private int level;
	private Player player;
	private int allKills;
	private int levelKills;
	private int deaths;
	private static HashMap<String, PlayerManager> players = new HashMap<>();
	private org.bukkit.scoreboard.Scoreboard scoreboard;
	public PlayerManager(Player player){
		this.player = player;
		this.level = 1;
		this.allKills = 0;
		this.levelKills = 0;
		this.deaths = 0;
	}

	/**
	 * @param player - Player a ser adicionado
	 * @param pManager - Manager das info do player no mini-game
	 */
	public static void addPlayer(String player, PlayerManager pManager){
		if(!players.containsKey(player)){
			players.put(player, pManager);
		}
	}
	
	public org.bukkit.scoreboard.Scoreboard getScoreboard(){
		return scoreboard;
	}
	
	public void setScoreboard(org.bukkit.scoreboard.Scoreboard scoreboard2){
		scoreboard = scoreboard2;
	}
	
	/**
	 * @param player - Player que você quer o PlayerManager
	 * @return - Retorna o PlayerManager do @param player
	 */
	public static PlayerManager getPlayerManager(String player){
		return players.containsKey(player) ? players.get(player) : null;
	}
	
	/**
	 * @return - Retorna lista de players
	 */
	public static HashMap<String, PlayerManager> getPlayers(){
		return players;
	}
	
	/**
	 * @return - Retorna o level do player
	 */
	public int getLevel(){
		return this.level;
	}
	
	/**
	 * @param level - Level desejado para setar
	 */
	public void setLevel(int level){
		this.level = level;
	}
	
	/**
	 * @return - Todas as kills do jogador na partida
	 */
	public int getAllKills(){
		return this.allKills;
	}
	
	/**
	 * Adiciona uma kill as kills do player
	 */
	public void addKillToAllKills(){
		this.allKills +=  1;
	}
	
	/**
	 * @return - Retorna as kills do player
	 */
	public int getLevelKills(){
		return this.levelKills;
	}
	
	/**
	 * Reseta as kills
	 */
	public void resetLevelKills(){
		this.levelKills = 0;
	}
	
	/**
	 * Adiciona kill
	 */
	public void addKillToLevelKills(){
		this.levelKills += 1;
	}
	
	/**
	 * @return - Retorna as mortes
	 */
	public int getDeaths(){
		return this.deaths;
	}

	/**
	 * Adiciona uma morte
	 */
	public  void addDeath(){
		this.deaths += 1;
	}
	
	/**
	 * @return - Retorna o player do PlayerManager
	 */
	public Player getPlayer(){
		return this.player;
	}
	
}
