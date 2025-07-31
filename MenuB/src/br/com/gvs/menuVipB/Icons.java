package br.com.gvs.menuVipB;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.smooph.insure.config.InsureConfig;
import br.com.smooph.karmas.config.KarmasConfig;
import br.com.smooph.karmas.manager.KarmaPlayer;
import br.com.tlcm.pvpd.PvPData;
import br.gvs.com.itemStackUtil.ItemBuilder;

public class Icons {
	
	private static ItemStack hat = null;
	private static ItemStack tag = null;
	private static ItemStack fertilizar = null;
	private static ItemStack montaria = null;
	private static ItemStack bench = null;
	private static ItemStack bau = null;
	private static ItemStack furnace = null;
	private static ItemStack kitnbvip = null;
	private static ItemStack warploja = null;
	private static ItemStack warpVip = null;
	private static ItemStack warpRegion = null;
	private static ItemStack warpBlocos = null;
	private static ItemStack warpMinerar = null;
	private static ItemStack mc = null;
	private static ItemStack spawn = null;
	private static ItemStack dummy = null;
	private static ItemStack desencantar = null;
	
	public static ItemStack getIconHat(){
		if(hat != null){
			return hat;
		}
		ItemBuilder item = new ItemBuilder(Material.GOLD_HELMET).setDisplayName("§b§lHAT");
		List<String> lore = new ArrayList<>(); 
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para definir o item em §7sua mão como seu novo chapeu."));
		item.setLore(lore);
		hat = item.build();
		return hat;
	}
	
	public static ItemStack getIcontTag(){
		if(tag != null){
			return tag;
		}
		ItemBuilder item = new ItemBuilder(Material.NAME_TAG).setDisplayName("§c§lTAGS");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ver ou mudar sua(s) §7tag(s)."));
		item.setLore(lore);
		tag = item.build();
		return tag;
	}
	
	public static ItemStack getIcontFertilizar(){
		if(fertilizar != null){
			return fertilizar;
		}
		ItemBuilder item = new ItemBuilder(Material.SUGAR_CANE).setDisplayName("§a§lFERTILIZAR");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Faz que todas as plantações §7do chunk (16x16x256 blocos) §7que §7você está cresça."));
		item.setLore(lore);
		fertilizar = item.build();
		return fertilizar;
	}
	
	public static ItemStack getIcontMontaria(){
		if(montaria != null){
			return montaria;
		}
		ItemBuilder item = new ItemBuilder(Material.DIAMOND_BARDING).setDisplayName("§3§lMONTARIAS");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para abrir o menu de §7Montarias."));
		item.setLore(lore);
		montaria = item.build();
		return montaria;
	}
	
	public static ItemStack getIcontDesencantar(){
		if(desencantar != null){
			return desencantar;
		}
		ItemBuilder item = new ItemBuilder(Material.ENCHANTED_BOOK).setDisplayName("§5§lDESENCANTAR");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para §7desencantar §7o §7item §7em §7sua §7mão."));
		item.setLore(lore);
		desencantar = item.build();
		return desencantar;
	}
	
	public static ItemStack getIcontBench(){
		if(bench != null){
			return bench;
		}
		ItemBuilder item = new ItemBuilder(Material.WORKBENCH).setDisplayName("§6§lMESA DE TRABALHO");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para abrir a mesa de §7trabalho."));
		item.setLore(lore);
		bench = item.build();
		return bench;
	}
	
	public static ItemStack getIconBau(){
		if(bau != null){
			return bau;
		}
		ItemBuilder item = new ItemBuilder(Material.CHEST).setDisplayName("§6§lBAÚ");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para abrir seu bau virtual."));
		item.setLore(lore);
		bau = item.build();
		return bau;
	}
	
	public static ItemStack getIconFurnace(){
		if(furnace != null){
			return furnace;
		}
		ItemBuilder item = new ItemBuilder(Material.FURNACE).setDisplayName("§7§lFORNALHA");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para abrir a fornalha."));
		item.setLore(lore);
		furnace = item.build();
		return furnace;
	}
	
	public static ItemStack getIconKitNbVip(){
		if(kitnbvip != null){
			return kitnbvip;
		}
		ItemBuilder item = new ItemBuilder(Material.GOLD_INGOT).setDisplayName("§6§lKIT NBVIP");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para receber o kit nbvip."));
		item.setLore(lore);
		kitnbvip = item.build();
		return kitnbvip;
	}
	
	public static ItemStack getIconPvpOff(Player player){
		boolean pvp = PvPData.getPvP(player).isPvPDisabled();
		ItemBuilder item = new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(pvp ? "§a§lATIVAR PVP" : "§c§lDESATIVAR PVP");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("§7Status: " + (pvp ? "§cDESATIVADO" : "§aATIVADO"));
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para " + (pvp ? "§aATIVAR" : "§cDESATIVAR") + " §7o §7pvp."));
		item.setLore(lore);
		return item.build();
	}
	
	public static ItemStack getIconWarpLoja(){
		if(warploja != null){
			return warploja;
		}
		ItemBuilder item = new ItemBuilder(Material.SIGN).setDisplayName("§e§lWARP LOJA");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir até a loja."));
		item.setLore(lore);
		warploja = item.build();
		return warploja;
	}
	
	public static ItemStack getIconWarpVip(){
		if(warpVip != null){
			return warpVip;
		}
		ItemBuilder item = new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§6§lWARP VIP");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir até a loja vip."));
		item.setLore(lore);
		warpVip = item.build();
		return warpVip;
	}
	
	public static ItemStack getIconWarpRegion(){
		if(warpRegion != null){
			return warpRegion;
		}
		ItemBuilder item = new ItemBuilder(Material.FENCE).setDisplayName("§3§lWARP REGION");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir ao mundo de proteção §7por terrenos."));
		item.setLore(lore);
		warpRegion = item.build();
		return warpRegion;
	}
	
	public static ItemStack getIconWarpBlocos(){
		if(warpBlocos != null){
			return warpBlocos;
		}
		ItemBuilder item = new ItemBuilder(Material.BRICK).setDisplayName("§c§lWARP BLOCOS");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir ao mundo de proteção §7por blocos."));
		item.setLore(lore);
		warpBlocos = item.build();
		return warpBlocos;
	}
	
	public static ItemStack getIconWarpDummy(){
		if(dummy != null){
			return dummy;
		}
		ItemBuilder item = new ItemBuilder(Material.BOW).setDisplayName("§c§lWARP DUMMY");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir até o local §7para §7treinamento §7de pvp e up de §7skills."));
		lore.add(" ");
		lore.addAll(parseToLines("§7Só é possível upar skills somente §7até o §clv100"));
		item.setLore(lore);
		dummy = item.build();
		return dummy;
	}
	
	public static ItemStack getIconSpawn(){
		if(spawn != null){
			return spawn;
		}
		ItemBuilder item = new ItemBuilder(Material.BEACON).setDisplayName("§d§lSPAWN");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir até o spawn."));
		item.setLore(lore);
		spawn = item.build();
		return spawn;
	}
	
	public static ItemStack getIconMinerar(){
		if(warpMinerar != null){
			return warpMinerar;
		}
		ItemBuilder item = new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName("§b§lWARP MINERAR");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir ao mundo de mineração."));
		item.setLore(lore);
		warpMinerar = item.build();
		return warpMinerar;
	}
	
	public static ItemStack getIconMc(){
		if(mc != null){
			return mc;
		}
		ItemBuilder item = new ItemBuilder(Material.GLASS).setDisplayName("§b§lMC");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Clique para ir ativar ou desativar §7o §7mc."));
		item.setLore(lore);
		mc = item.build();
		return mc;
	}
	
	public static ItemStack getIconLastKill(Player player){
		ItemBuilder item = new ItemBuilder(Material.SKULL_ITEM).setDisplayName("§c§lULTIMO ABATE");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		String lastPlayer = Listeners.lastKill.containsKey(player.getName()) ? Listeners.lastKill.get(player.getName()) : "Você ainda não matou nenhum jogador";
		lore.addAll(parseToLines("§7O ultimo jogador que voce matou §7foi:"));
		lore.add("§f" + lastPlayer);
		item.setLore(lore);
		return item.build();
	}
	
	public static ItemStack getIconLastDeath(Player player){
		ItemBuilder item = new ItemBuilder(Material.REDSTONE).setDisplayName("§c§lULTIMA MORTE");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		String lastPlayer = Listeners.lastDeath.containsKey(player.getName()) ? Listeners.lastDeath.get(player.getName()) : "Você ainda não morreu para outro jogador";
		lore.addAll(parseToLines("§7Sua ultima morte foi causada §7pelo player:"));
		lore.add("§f" + lastPlayer);
		item.setLore(lore);
		return item.build();
	}
	
	public static ItemStack getIconTempoVip(Player player){
		ItemBuilder item = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setDisplayName("§6§lTEMPO VIP");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Seu vip expira em:"));
		lore.add("§6" + MenuVipB.getVipExpire(player));
		item.setLore(lore);
		return item.build();
	}
	
	public static ItemStack getIconKarma(Player player){
		ItemBuilder item = new ItemBuilder(Material.EMERALD).setDisplayName("§9§lKARMA");
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		String karma = KarmaPlayer.getKarmaPlayer(player).getKarma().getColor() + KarmasConfig.getSymbol();
		lore.addAll(parseToLines("§7Seu karma atual é: " + karma));
		item.setLore(lore);
		return item.build();
	}
	
	public static ItemStack getIconInsure(Player player){
		boolean insure = InsureConfig.hasInsure(player.getName());
		ItemBuilder item = new ItemBuilder(Material.DRAGON_EGG).setDisplayName((insure ? "§a§lINSURE" : "§c§lINSURE"));
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.addAll(parseToLines("§7Seu insure está: " + (insure ? "§aATIVADO" : "§cDESATIVADO")));
		if(insure){
			lore.add(" ");
			lore.add("§7Clique para §aATIVAR §7o §7insure.");
		}
		item.setLore(lore);
		return item.build();
	}
	
	
	
	private static List<String> parseToLines(String text){
		List<String> lines = new ArrayList<String>();
		String line = null;
		
		for(String word : text.split(" ")){
			if(line == null){
				line = word;
			}else{
				line += " " + word;
			}
			
			if(line.length() > 28){
				lines.add(line);
				line = null;
			}
		}
		
		if(line != null){
			lines.add(line);
		}
		return lines;
	}
	
}
