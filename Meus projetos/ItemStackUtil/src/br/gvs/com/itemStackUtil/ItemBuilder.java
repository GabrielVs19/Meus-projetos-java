package br.gvs.com.itemStackUtil;

import java.util.List;
import java.util.Map;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder {

	private ItemStack item;
	private ItemMeta itemMeta;
	public ItemBuilder(Material material){
		item = new ItemStack(material);
		itemMeta = item.getItemMeta();
	}

	public ItemBuilder(ItemStack itemStack){
		item = itemStack;
		itemMeta = item.getItemMeta();
	}

	public ItemBuilder(Material material, int amount){
		item = new ItemStack(material, amount);
		itemMeta = item.getItemMeta();
	}

	public ItemBuilder(Material material, int amount, String name){
		item = new ItemStack(material, amount);
		itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
	}

	public ItemBuilder(Material material, int amount, short data){
		item = new ItemStack(material, amount, data);
		itemMeta = item.getItemMeta();
	}

	public ItemBuilder(Material material, int amount, String name, short data){
		item = new ItemStack(material, amount, data);
		itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
	}

	public ItemBuilder setDisplayName(String name){
		itemMeta.setDisplayName(name);
		return this;
	}

	public ItemBuilder setAmount(int amount){
		item.setAmount(amount);
		return this;
	}

	public ItemBuilder setMaterial(Material material){
		item.setType(material);
		return this;
	}

	public ItemBuilder setData(MaterialData materialData){
		item.setData(materialData);
		return this;
	}

	public ItemBuilder setDurability(short durability){
		item.setDurability(durability);
		return this;
	}

	public ItemBuilder addEnchant(Enchantment enchant, int level){
		itemMeta.addEnchant(enchant, level, true);
		return this;
	}

	public ItemBuilder addUnsafeEnchant(Enchantment enchant, int level){
		item.addUnsafeEnchantment(enchant, level);
		return this;
	}

	public ItemBuilder removeEnchant(Enchantment enchant){
		itemMeta.removeEnchant(enchant);
		return this;
	}

	public ItemBuilder setItemMeta(ItemMeta meta){
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(List<String> lore){
		itemMeta.setLore(lore);
		return this;
	}

	public ItemBuilder removeLore(){
		itemMeta.setLore(null);
		return this;
	}

	public boolean hasDisplayName(){
		return itemMeta.hasDisplayName() ? true : false;
	}

	public boolean hasLore(){
		return itemMeta.hasLore() ? true : false;
	}

	public List<String> getLore(){
		if(hasLore()){
			return itemMeta.getLore();
		}else{
			return null;
		}
	}

	public boolean hasEnchant(Enchantment enchant){
		return itemMeta.hasEnchant(enchant) ? true : false;
	}

	public boolean hasEnchants(){
		return itemMeta.hasEnchants() ? true : false;
	}

	public int getEnchantLevel(Enchantment enchant){
		return itemMeta.getEnchantLevel(enchant);
	}

	public Map<Enchantment, Integer> getEnchants(){
		return itemMeta.getEnchants();
	}

	public String getDisplayName(){
		if(hasDisplayName()){
			return itemMeta.getDisplayName();
		}else{
			return null;
		}
	}

	public int getAmount(){
		return item.getAmount();
	}

	public int getDurability(){
		return item.getDurability();
	}

	public int getMaxStackSize(){
		return item.getMaxStackSize();
	}

	@SuppressWarnings("deprecation")
	public int getTypeId(){
		return item.getTypeId();
	}

	public Material getType(){
		return item.getType();
	}

	public ItemBuilder addGlow(){
		itemMeta.addEnchant(new Glow(70), 1, true);
		return this;
	}

	public ItemBuilder removeGlow(){
		if(itemMeta.hasEnchant(new Glow(70))){
			itemMeta.removeEnchant(new Glow(70));
		}
		return this;
	}

	public boolean hasGlow(){
		return itemMeta.hasEnchant(new Glow(70));
	}

	public double getDamage(){
		net.minecraft.server.v1_7_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = null;
		if(nmsStack.hasTag()){
			compound = nmsStack.getTag();
		}else{
			compound = new NBTTagCompound();
		}
		compound.getList("AttributeModifiers", 0).get(0).get("Amount").toString();
		return 0;
	}

	public ItemBuilder setDamage(double damageValue){
		item.setItemMeta(itemMeta);
		net.minecraft.server.v1_7_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
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
			modifiers.add(damage);
			compound.set("AttributeModifiers", modifiers);
			nmsStack.setTag(compound);
			item = CraftItemStack.asBukkitCopy(nmsStack);
			itemMeta = CraftItemStack.asBukkitCopy(nmsStack).getItemMeta();
		}
		return this;
	}

	public ItemStack build(){
		item.setItemMeta(itemMeta);
		return item;
	}

}
