package br.com.gvs.mobs.util;

import org.bukkit.inventory.InventoryView;

import net.minecraft.server.v1_7_R2.Container;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntitySheep;

public class ContainerSheepBreed extends Container {

	public ContainerSheepBreed(EntitySheep paramEntitySheep) {
	}

	public boolean a(EntityHuman paramEntityHuman) {
		return false;
	}

	@Override
	public InventoryView getBukkitView() {
		return null;
	}

}
