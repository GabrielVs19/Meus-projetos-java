package minigames.ibieel.com;

import java.util.ArrayList;

public class PlayerCaptcha {

	private String player;
	private int numero1;
	private int numero2;
	private char sinal;
	private Thread thread;
	private static ArrayList<PlayerCaptcha> playersCaptcha = new ArrayList<PlayerCaptcha>();
	public PlayerCaptcha(String player, int numero1, int numero2, char sinal, Thread thread){
		this.player = player;
		this.numero1 = numero1;
		this.numero2 = numero2;
		this.sinal = sinal;
		this.thread = thread;
	}
	
	public static void add(PlayerCaptcha pCaptcha){
		playersCaptcha.add(pCaptcha);
	}
	
	public static void remove(PlayerCaptcha pCaptcha){
		playersCaptcha.remove(pCaptcha);
	}
	
	public static PlayerCaptcha getPlayerCaptcha(String player){
		PlayerCaptcha pc = null;
		for(PlayerCaptcha pCaptcha : playersCaptcha){
			if(pCaptcha.getPlayer().equalsIgnoreCase(player)){
				pc = pCaptcha;
				break;
			}
		}
		if(pc != null){
			return pc;
		}else{
			return null;
		}
	}
	
	public Thread getThread(){
		return this.thread;
	}
	
	public String getPlayer(){
		return this.player;
	}
	
	public int getNumero1(){
		return this.numero1;
	}
	
	public int getNumero2(){
		return this.numero2;
	}
	
	public char getSinal(){
		return this.sinal;
	}
}
