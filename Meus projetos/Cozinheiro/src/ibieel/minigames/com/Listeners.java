package ibieel.minigames.com;

import java.util.ArrayList;
import java.util.List;

import me.libraryaddict.virtual.Furnaces;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;

public class Listeners implements Listener{

	@EventHandler
	public void openVirtualFurnace(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.BURNING_FURNACE) && !(CoreD.isRunning())){
			e.setCancelled(true);
			return;
		}
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.BURNING_FURNACE) && CoreD.isRunning()){
			e.setCancelled(true);
			Furnaces.openFurnace((Player) e.getPlayer());
			return;
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreakEvent(final BlockBreakEvent e){
		final Block block = e.getBlock();
		final Material blockM = block.getType();
		final byte blockD = e.getBlock().getData();
		if(!Main.getPlantacoes().contains(block.getType())){
			e.setCancelled(true);
			return;
		}
		final boolean pEspaciais = blockM == Material.MELON_BLOCK || blockM == Material.PUMPKIN || blockM == Material.SUGAR_CANE_BLOCK;
		new BukkitRunnable(){
			public void run(){
				if(pEspaciais){
					if(block.getType() == Material.SUGAR_CANE_BLOCK){
						Block down = e.getBlock().getRelative(BlockFace.DOWN);
						if(down.getType() == Material.DIRT || down.getType() == Material.SAND){
							e.getBlock().setType(blockM);
							e.getBlock().getRelative(BlockFace.UP).setType(blockM);
							e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(blockM);
						}
						return;
					}
				}
				block.setType(blockM);
				block.setData(blockD);
			}
		}.runTaskLater(Main.instance, 20 * (pEspaciais ? 10 : 11));
	}

	@EventHandler
	public void cancelBreakFarmWithJump(PlayerInteractEvent e){
		if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL){
			e.setCancelled(true);
		}
	}

	public Inventory fakeEnderChest(){
		Inventory inv = Bukkit.createInventory(null, 27, "§6Bau de comidas");
		return inv;
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.ENDER_CHEST){
				Player player = e.getPlayer();
				if(!Main.isUseEnderChest()){
					e.setCancelled(true);
					player.sendMessage("§cVocê só pode usar o bau enquanto a partida estiver acontecendo.");
				}else{
					Inventory inv = fakeEnderChest();
					inv.clear();
					inv.setContents(player.getEnderChest().getContents());
					e.setCancelled(true);
					player.openInventory(inv);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		if(e.getInventory().getTitle().contains("Bau de comidas")){
			e.getPlayer().getEnderChest().clear();
			e.getPlayer().getEnderChest().setContents(e.getInventory().getContents());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDamageEvent(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			e.setDamage(0);
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e){
		ItemStack item = e.getItem().getItemStack();
		Material material = item.getType();
		if(Main.getComidas().containsKey(material)){
			ItemMeta meta = item.getItemMeta();
			List<String> pontos = new ArrayList<String>();
			pontos.add("§6Pontos: §a" + Main.getComidas().get(material));
			meta.setLore(pontos);
			item.setItemMeta(meta);
		}
	}

	@EventHandler
	public void onExtractItemEvent(InventoryClickEvent e){
		if(e.getCurrentItem() != null){
			ItemStack item = e.getCurrentItem();
			Material material = item.getType();
			if(e.getInventory().getType() == InventoryType.FURNACE){
				if(e.getSlotType() == SlotType.RESULT){
					if(Main.getComidas().containsKey(material)){
						ItemMeta meta = item.getItemMeta();
						List<String> pontos = new ArrayList<String>();
						pontos.add("§6Pontos: §a" + Main.getComidas().get(material));
						meta.setLore(pontos);
						item.setItemMeta(meta);
						e.getCurrentItem().setItemMeta(meta);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerCraftItem(PrepareItemCraftEvent e){
		ItemStack item = e.getRecipe().getResult();
		Material material = item.getType();
		if(Main.getComidas().containsKey(material)){
			ItemMeta meta = item.getItemMeta();
			List<String> pontos = new ArrayList<String>();
			pontos.add("§6Pontos: §a" + Main.getComidas().get(material));
			meta.setLore(pontos);
			item.setItemMeta(meta);
			e.getInventory().getResult().setItemMeta(meta);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getInventory().getName().equals("§6§lComidas")){
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
		}
	}


	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e){
		int random = (int) ((Math.random() * 99) + 1);
		if(e.getEntity().getType() == EntityType.COW){
			MobsManager.cows -= 1;
			if(random >= 60){
				e.getDrops().add(new ItemStack(Material.MILK_BUCKET, 1));
			}
			return;	
		}
		if(e.getEntity() instanceof Chicken){
			MobsManager.chickens -= 1;
			if(random >= 60){
				e.getDrops().add(new ItemStack(Material.EGG, 1));
			}
			return;	
		}
		if(e.getEntity().getType() == EntityType.MUSHROOM_COW){
			MobsManager.mushroomCows -= 1;
			if(random >= 60){
				e.getDrops().add(new ItemStack(Material.BOWL, 1));
				e.getDrops().add(new ItemStack(Material.RED_MUSHROOM, 1));
				e.getDrops().add(new ItemStack(Material.BROWN_MUSHROOM, 1));
			}
			return;	
		}
		if(e.getEntity() instanceof Pig){
			MobsManager.pigs -= 1;
			if(random >= 60){
				e.getDrops().add(new ItemStack(Material.APPLE, 1));
			}
			return;
		}
		if(e.getEntity() instanceof PigZombie){
			MobsManager.pigZombies -= 1;
			if(random >= 60){
				e.getDrops().add(new ItemStack(Material.GOLD_NUGGET, 5));
			}
			return;	
		}
	}
}

