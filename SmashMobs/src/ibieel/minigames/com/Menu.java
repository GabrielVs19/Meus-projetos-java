package ibieel.minigames.com;

import ibieel.minigames.com.Managers.ClassManager;
import ibieel.minigames.com.Managers.ClassManager.ClassType;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Util.ChatUtil;
import ibieel.minigames.com.Util.ItemStackUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;

public class Menu implements Listener{

	public SmashMobs plugin;
	public Menu(SmashMobs plugin){
		this.plugin = plugin;
	}

	private ItemStack classItem(){
		return ItemStackUtil.getItemStack(Material.BEACON, 1, "§6Escolha sua classe");
	}

	public static Inventory classInventoy(){
		Inventory classInventory = Bukkit.createInventory(null, 18, "§1§lEscolha sua classe");
		int a = 0;
		for(ClassType c : ClassType.values()){
			a++;
			if(ClassManager.getClass(c) != null){
				classInventory.addItem(ClassManager.getClass(c).getIconMenu());
			}
		}
		for(int i = a; i <= classInventory.getSize() - 1;i++){
			classInventory.setItem(i, ItemStackUtil.getItemStack(Material.STAINED_GLASS_PANE, 1, "§c§lEM BREVE", (short) 15));
		}
		return classInventory;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent e){
		if(!CoreD.isRunning()){
			e.getPlayer().getInventory().setItem(8, this.classItem());
			e.getPlayer().sendMessage(" ");
			((CraftPlayer)e.getPlayer()).getHandle().sendMessage(ChatUtil.chatComando("§9Aviso> §6Escolha sua classe usando o §b§lBEACON§6.", "§eClique para abrir o menu de classes.", "/a"));
			e.getPlayer().sendMessage(" ");
		}
	}

	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getItemInHand().equals(this.classItem())){
				if(!CoreD.isRunning()){
					if(CoreDPlayer.getPlayersWhoArentSpectators().contains(e.getPlayer())){
						e.getPlayer().openInventory(classInventoy());
					}else{
						e.getPlayer().sendMessage("§cVocê não pode escolher classes estando no modo espectador.");
					}
				}else{
					e.getPlayer().sendMessage("§cA partida já começou.");
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		Inventory inventory = e.getInventory();
		if(inventory.getName().equalsIgnoreCase(classInventoy().getName())){
			if(!CoreD.isRunning()){
				e.setCancelled(true);
				if(item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().contains("§")){
					boolean valido = false;
					for(ClassType c : ClassType.values()){
						if(ClassManager.getClass(c) != null){
							if(item.getItemMeta().getDisplayName().equalsIgnoreCase(ClassManager.getClass(c).getName())){
								player.sendMessage("§9Classe> §6Você escolheu a classe " + ClassManager.getClass(c).getName() + "§6.");
								player.closeInventory();
								player.playSound(player.getLocation(), Sound.ANVIL_LAND, 100, 1);
								valido = true;
								PlayerManager pm = new PlayerManager(c);
								PlayerManager.add(player.getName(), pm);
								((CraftPlayer)player).getHandle().sendMessage(ChatUtil.chatHover("§8§l========= §a§lSkills §8§l=========", "§eSkills da classe: " + ClassManager.getClass(c).getName()));
								for(Skills k : PlayerManager.getSkills(player.getName())){
									((CraftPlayer)player).getHandle().sendMessage(ChatUtil.chatHover("§9>> " + k.getName(), "§eCooldown: §f" + k.getCooldown() + " §esegundo(s)"));
									player.sendMessage("§7§o" + k.getDesc());
								}
								player.sendMessage("§8§l========================");
								break;
							}
						}
					}
					if(!valido){
						player.closeInventory();
						player.sendMessage("§cVocê não selecionou uma classe valida.");
					}
				}else{
					player.closeInventory();
					player.sendMessage("§cVocê não selecionou uma classe valida.");
				}
			}else{
				e.setCancelled(true);
				player.closeInventory();
				player.sendMessage("§cA partida já começou.");
			}
		}
	}

	

}
