package minigames.ibieel.com;

import net.minecraft.server.v1_7_R2.ChatClickable;
import net.minecraft.server.v1_7_R2.ChatHoverable;
import net.minecraft.server.v1_7_R2.ChatMessage;
import net.minecraft.server.v1_7_R2.ChatModifier;
import net.minecraft.server.v1_7_R2.ChatSerializer;
import net.minecraft.server.v1_7_R2.EnumClickAction;
import net.minecraft.server.v1_7_R2.EnumHoverAction;
import net.minecraft.server.v1_7_R2.IChatBaseComponent;

public class HoverChatUtil {

	public static IChatBaseComponent chatHover(String texto, String msgHover){
		IChatBaseComponent chatB = new ChatMessage(texto);
		chatB.setChatModifier(new ChatModifier());
		chatB.getChatModifier().a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage(msgHover)));
		String chatBClick = ChatSerializer.a(chatB);
		IChatBaseComponent chat = ChatSerializer.a(chatBClick);
		return chat;
	}
	
	public static IChatBaseComponent chatComando(String texto, String msgHover, String comando){
		IChatBaseComponent chatB = new ChatMessage(texto);
		chatB.setChatModifier(new ChatModifier());
		chatB.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.RUN_COMMAND, comando));
		chatB.getChatModifier().a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage(msgHover)));
		String chatBClick = ChatSerializer.a(chatB);
		IChatBaseComponent chat = ChatSerializer.a(chatBClick);
		return chat;
	}
	
}
