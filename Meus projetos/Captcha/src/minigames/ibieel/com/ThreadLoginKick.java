package minigames.ibieel.com;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ThreadLoginKick extends Thread {

	private String player;
	public ThreadLoginKick(String player){
		this.player = player;
	}

	public void run(){
		try {
			sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!Captcha.getCaptchaPlayers().contains(player)){
			if(Bukkit.getServer().getPlayer(player) != null){
				Bukkit.getServer().getPlayer(player).kickPlayer(ChatColor.RED + "Você demorou demais para resolver o captcha.");
			}
		}
		interrupt();
	}

}
