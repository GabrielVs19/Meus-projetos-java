package br.gvs.com.Chat;


import net.minecraft.server.v1_7_R2.IChatBaseComponent;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatUtil extends JavaPlugin {

	public void onEnable(){
		System.out.println("EclipseChatUtil enable - Author iBieel - v0.1");
	}
	
	public static void sendMessage(Player player, IChatBaseComponent iChatBaseComponent){
		CraftPlayer cPlayer = ((CraftPlayer)player);
		cPlayer.getHandle().sendMessage(iChatBaseComponent);
	}
	
	public static void sendMessage(Player player, IChatBaseComponent[] iChatBaseComponent){
		CraftPlayer cPlayer = ((CraftPlayer)player);
		cPlayer.getHandle().sendMessage(iChatBaseComponent);
	}

}
