package br.gvs.com.Chat;

import java.util.regex.Pattern;

import mkremins.fanciful.FancyMessage;
import net.minecraft.server.v1_7_R2.ChatSerializer;
import net.minecraft.server.v1_7_R2.IChatBaseComponent;

import org.bukkit.ChatColor;

public class ChatBuilder {

	private FancyMessage fMessage;

	public ChatBuilder(String text){
		//chat.add("{text:\"" + text + "\"}");
		fMessage = new FancyMessage(text);
	}

	public ChatBuilder withColor(ChatColor color) {
		fMessage.color(color);
		return this;
	}

	 public ChatBuilder withColor(String color){
	    while (color.length() != 1) {
	      color = color.substring(1).trim();
	    }
	    withColor(ChatColor.getByChar(color));
	    return this;
	  }

	public ChatBuilder addCommand(String command){
		fMessage.command(command);
		return this;
	}

	public ChatBuilder addSugestCommand(String command){
		fMessage.suggest(command);
		return this;
	}

	public ChatBuilder addText(String text) {
		 String regex = "[&§]{1}([a-fA-Fl-oL-O0-9]){1}";
		    text = text.replaceAll(regex, "§$1");
		    if (!Pattern.compile(regex).matcher(text).find())
		    {
		      this.fMessage.then(text);
		      return this;
		    }
		    String[] words = text.split(regex);
		    
		    int index = 0;
		    for (String word : words) {
		      if ((word != null) && (!word.isEmpty()))
		      {
		        this.fMessage.then(word);
		        if ((words[0].isEmpty()) || (index != 0))
		        {
		          withColor("§" + text.toCharArray()[(index + 1)]);
		          

		          index += 2;
		        }
		        index += word.length();
		      }
		    }
		    return this;
	}

	public ChatBuilder addLink(String link){
		fMessage.link(link);
		return this;
	}

	public ChatBuilder addHoverMessage(String message){
		fMessage.tooltip(message);
		return this;
	}

	public ChatBuilder addHoverMessage(String... messages){
		fMessage.tooltip(messages);
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

	public IChatBaseComponent build(){
		return ChatSerializer.a(fMessage.toJSONString());
	}


}
