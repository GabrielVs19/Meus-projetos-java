package br.gvs.com.Chat;


import java.util.List;

import net.minecraft.server.v1_7_R2.IChatBaseComponent;
import net.minecraft.server.v1_7_R2.PacketPlayOutChat;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatUtil extends JavaPlugin {

	public void onEnable(){
		System.out.println("EclipseChatUtil enable - Author iBieel - v0.1");
	}

	public static void sendMessage(Player player, IChatBaseComponent iChatBaseComponent){
		CraftPlayer cPlayer = ((CraftPlayer)player);
		PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent);
		cPlayer.getHandle().playerConnection.sendPacket(packetPlayOutChat);
	}

	public static void sendMessage(Player player, IChatBaseComponent[] iChatBaseComponent){
		CraftPlayer cPlayer = ((CraftPlayer)player);
		for(IChatBaseComponent ic : iChatBaseComponent){
			PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(ic);
			cPlayer.getHandle().playerConnection.sendPacket(packetPlayOutChat);
		}
	}

	public static void sendMessage(List<Player> players, IChatBaseComponent iChatBaseComponent){
		for(Player p : players){
			CraftPlayer cPlayer = ((CraftPlayer)p);
				PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent);
				cPlayer.getHandle().playerConnection.sendPacket(packetPlayOutChat);
		}
	}

	public static void sendMessage(List<Player> players, IChatBaseComponent[] iChatBaseComponent){
		for(Player p : players){
			CraftPlayer cPlayer = ((CraftPlayer)p);
			for(IChatBaseComponent ic : iChatBaseComponent){
				PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(ic);
				cPlayer.getHandle().playerConnection.sendPacket(packetPlayOutChat);
			}
		}
	}

}
