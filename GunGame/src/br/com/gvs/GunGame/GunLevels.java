package br.com.gvs.GunGame;

import java.util.UUID;

import net.minecraft.server.v1_7_R2.NBTTagCompound;
import net.minecraft.server.v1_7_R2.NBTTagDouble;
import net.minecraft.server.v1_7_R2.NBTTagInt;
import net.minecraft.server.v1_7_R2.NBTTagList;
import net.minecraft.server.v1_7_R2.NBTTagLong;
import net.minecraft.server.v1_7_R2.NBTTagString;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Gabriel
 *
 */
public class GunLevels {

	/**
	 * @author Gabriel
	 * Armas para cada nível e quantidade de kills restantes para o próximo nível
	 */
	public static enum Guns{
		GUN_01(1, ItemStackUtil.getItemStack(Material.DIAMOND_SWORD, 1, "§eEspada de diamante"), 8.0, 1),
		GUN_02(2, ItemStackUtil.getItemStack(Material.DIAMOND_AXE, 1, "§eMachado de Diamante"), 7.5, 1),
		GUN_03(3, ItemStackUtil.getItemStack(Material.DIAMOND_PICKAXE, 1, "§ePicareta de diamante"), 7.0, 1),
		GUN_04(4, ItemStackUtil.getItemStack(Material.DIAMOND_HOE, 1, "§eEnxada de diamante"), 6.5, 1),
		GUN_05(5, ItemStackUtil.getItemStack(Material.IRON_SWORD, 1, "§eEspada de ferro"), 6.0, 1),
		GUN_06(6, ItemStackUtil.getItemStack(Material.IRON_AXE, 1, "§eMachado de ferro"), 5.5, 1),
		GUN_07(7, ItemStackUtil.getItemStack(Material.IRON_SPADE, 1, "§ePá de ferro"), 5.0, 1),
		GUN_08(8, ItemStackUtil.getItemStack(Material.STONE_SWORD, 1, "§eEspada de pedra"), 4.5, 1),
		GUN_09(9, ItemStackUtil.getItemStack(Material.STONE_HOE, 1, "§eEnxada de pedra"), 4.0, 1),
		GUN_10(10, ItemStackUtil.getItemStack(Material.STONE_PICKAXE, 1, "§ePicareta de pedra"), 4.0, 1),
		GUN_11(11, ItemStackUtil.getItemStack(Material.GOLD_SWORD, 1, "§eEspada de ouro"), 4.0, 1),
		GUN_12(12, ItemStackUtil.getItemStack(Material.GOLD_AXE, 1, "§eMachado de ouro"), 3.5, 1),
		GUN_13(13, ItemStackUtil.getItemStack(Material.WOOD_AXE, 1, "§eMachado de madeira"), 3.0, 1),
		GUN_14(14, ItemStackUtil.getItemStack(Material.WOOD_SPADE, 1, "§ePá de madeira"), 3.0, 1),
		GUN_15(15, ItemStackUtil.getItemStack(Material.BOW, 1, "§eArco"), 0.0, 1);

		private ItemStack gun;
		private double damage;
		private int killsToNextGun;
		private int level;
		private Guns(int level, ItemStack gun, double damage, int killsToNextGun){
			this.gun = gun;
			this.damage = damage;
			this.killsToNextGun = killsToNextGun;
			this.level = level;
		}

		/**
		 * @return - Retorna a quantidade de kills que precisa para o próximo nível
		 */
		public int getKillsToNextGun(){
			return killsToNextGun;
		}

		/**
		 * @return - Retorna a arma do nível, já com os valores de dano editados
		 */
		public ItemStack getGun(){
			return changeDamage(gun, damage);
		}

		/**
		 * @return - Retorna o level da arma
		 */
		public int getLevel(){
			return this.level;
		}

		/**
		 * @return - Retorna o dano da arma
		 */
		public double getDamage(){
			return this.damage;
		}

		/**
		 * @param level - Level qual você quer pegar a arma
		 * @return - Retorna a arma do respectivo level que você chamou no parâmetro
		 */
		public static Guns getGunByLevel(int level){
			Guns gun = null;
			for(Guns g : values()){
				if(g.getLevel() == level){
					gun = g;
					break;
				}
			}
			return gun;
		}
	}

	/**
	 * @param i - Item a ser modificado
	 * @param damageValue - Novo valor de dano que será atribuido a arma
	 * @return - Retorna um item com seu damage alterado usando NBTTags
	 */
	private static ItemStack changeDamage(ItemStack i, double damageValue){
		net.minecraft.server.v1_7_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = null;
		if(nmsStack != null) {
			if(nmsStack.hasTag()){
				compound = nmsStack.getTag();
			}else{
				compound = new NBTTagCompound();
			}
			NBTTagList modifiers = new NBTTagList();
			NBTTagCompound damage = new NBTTagCompound();
			damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
			damage.set("Name", new NBTTagString("generic.attackDamage"));
			damage.set("Amount", new NBTTagDouble(damageValue));
			damage.set("Operation", new NBTTagInt(0));
			UUID randUUID = UUID.randomUUID();
			damage.set("UUIDLeast", new NBTTagLong(randUUID.getLeastSignificantBits()));
			damage.set("UUIDMost", new NBTTagLong(randUUID.getMostSignificantBits()));
			damage.setBoolean("Unbreakable", true);
			modifiers.add(damage);
			compound.set("AttributeModifiers", modifiers);
			nmsStack.setTag(compound);
			ItemStack item = CraftItemStack.asBukkitCopy(nmsStack);
			if(item.getType() == Material.BOW){
				item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 30);
			}else{
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 30);
			}
			return item;
		}
		return null;
	}

}