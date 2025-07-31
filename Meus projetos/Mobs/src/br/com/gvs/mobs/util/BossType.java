package br.com.gvs.mobs.util;


import br.com.gvs.mobs.bosses.CustomDragao;
import br.com.gvs.mobs.bosses.CustomLucifron;
import br.com.gvs.mobs.bosses.CustomMvP;
import br.com.gvs.mobs.bosses.CustomNefarian;
import br.com.gvs.mobs.bosses.CustomOvelhaDoCapiroto;
import br.com.gvs.mobs.bosses.CustomPandora;
import br.com.gvs.mobs.bosses.CustomShadowPelt;
import br.com.gvs.mobs.bosses.CustomShaldren;
import br.com.gvs.mobs.bosses.CustomSquit;
import br.com.gvs.mobs.bosses.CustomTips;
import br.com.gvs.mobs.bosses.CustomWither;
import br.com.gvs.mobs.bosses.CustomZac;
import net.minecraft.server.v1_7_R2.Entity;


public enum BossType{
	
	NEFARIAN(1, "Nefarian", 63, CustomNefarian.class),
	MVP(2, "MvP", 53, CustomMvP.class),
	WITHER(5, "Wither", 64, CustomWither.class),
	DRAGAO(6, "Dragão", 63, CustomDragao.class),
	SHADOW_PELT(9, "Shadow pelt", 100, CustomShadowPelt.class),
	SHALDREN(10, "Shaldren", 62, CustomShaldren.class),
	ZAC(11, "Zac", 55, CustomZac.class),
	PANDORA(12, "Pandora", 66, CustomPandora.class),
	LUCIFRON(4, "Lucifron", 64, CustomLucifron.class),
	TIPS(8, "Tips", 90, CustomTips.class),
	SQUIT(7, "Squit", 51, CustomSquit.class),
	OVELHA_DO_CAPIROTO(3, "Ovelha do capiroto", 91, CustomOvelhaDoCapiroto.class);
	
	private int bossID;
	private String name;
	private int minecraftID;
	private Class<? extends Entity> clazz;
	
	BossType(int bossID, String name, int minecraftID, Class<? extends Entity> clazz)
	{
		this.bossID = bossID;
		this.name = name;
		this.minecraftID = minecraftID;
		this.clazz = clazz;
	}
	
	public static BossType getBossByID(int id)
	{
		for(BossType mt : values())
		{
			if(mt.getBossID() == id)
			{
				return mt;
			}
		}
		return null;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getBossID()
	{
		return this.bossID;
	}
	
	public int getMinecraftID()
	{
		return this.minecraftID;
	}
	
	public Class<? extends Entity> getBossClass()
	{
		return this.clazz;
	}
	
	public static BossType getBossType(String name)
	{
		for(BossType bossType : BossType.values())
		{
			if(bossType.name().equalsIgnoreCase(name))
			{
				return bossType;
			}
		}
		return null;
	}
	
}
