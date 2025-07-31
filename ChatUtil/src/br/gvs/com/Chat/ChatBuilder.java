package br.gvs.com.Chat;

import java.util.ArrayList;

import net.minecraft.server.v1_7_R2.ChatSerializer;
import net.minecraft.server.v1_7_R2.IChatBaseComponent;

public class ChatBuilder {
	
	private ArrayList<String> chat = new ArrayList<String>();
	public ChatBuilder(String text){
		 chat.add("{text:\"" + text + "\"}");
	}

	public ChatBuilder addCommand(String command){
		add("clickEvent:{action:run_command,value:\"" + command + "\"}");
		return this;
	}
	
	public ChatBuilder addSugestCommand(String command){
		add("clickEvent:{action:suggest_command,value:\"" + command + "\"}");
		return this;
	}
	
	public ChatBuilder addText(String text){
		 chat.add("{text:\"" + text + "\"}");
		return this;
	}
	
	public ChatBuilder addLink(String link){
		add("clickEvent:{action:open_url,value:\"" + link + "\"}");
		return this;
	}
	
	public ChatBuilder addHoverMessage(String message){
		add("hoverEvent:{action:show_text,value:\"" + message + "\"}");
		return this;
	}
	
	/*
	public ChatBuilder addItem(String itemId, String name){
		add("hoverEvent:{action:show_item,value:{id:\"" + itemId + "\",tag:{display:{name:\"" + name + "\"}}}}");
		return this;
	}
	
	public ChatBuilder addItem(String itemId, String name, List<String> lore){
		String l = null;
		for(String s : lore){
			l += "\"" + s + "\",";
		}
		l = l.substring(0, l.length() - 1);
		add("hoverEvent:{name:show_item,value:{id:\"" + itemId + "\",tag:{display:{name:\"" + name + "\"},lore:["+ l + "]}}}");
		return this;
	}
	*/
	
	private void add(String s) {
        String lText = chat.get(chat.size() - 1);
        lText = lText.substring(0, lText.length() - 1) + ","+s+"}";
        chat.remove(chat.size() - 1);
        chat.add(lText);
    }
	
	private String buildString() {
        if(chat.size() <= 1){
        	return chat.size() == 0 ? "{text:\"\"}" : chat.get(0);
        }
        String text = chat.get(0).substring(0, chat.get(0).length() - 1) + ",extra:[";
        chat.remove(0);;
        for (String extra : chat){
        	text = text + extra + ",";
        }
        text = text.substring(0, text.length() - 1) + "]}";
        System.out.println(text);
        return text;
    }
	
	public IChatBaseComponent build(){
		return ChatSerializer.a(buildString());
	}
	
	
}
