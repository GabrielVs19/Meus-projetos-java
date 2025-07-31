package minigames.ibieel.com;


import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class Captcha extends JavaPlugin{

	private static ArrayList<String> captchaOk = new ArrayList<String>();
	
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}
	
	public static ArrayList<String> getCaptchaPlayers(){
		return captchaOk;
	}
	
	public static void addCaptcha(String player){
		captchaOk.add(player);
	}
	
	public static void removeCaptcha(String player){
		captchaOk.remove(player);
	}
}
