package br.com.gvs.mobs.util;


import net.minecraft.server.v1_7_R2.Entity;
import br.com.gvs.mobs.mobs.CustomBlaze;
import br.com.gvs.mobs.mobs.CustomCaveSpider;
import br.com.gvs.mobs.mobs.CustomChicken;
import br.com.gvs.mobs.mobs.CustomCow;
import br.com.gvs.mobs.mobs.CustomCreeper;
import br.com.gvs.mobs.mobs.CustomEgg;
import br.com.gvs.mobs.mobs.CustomEnderman;
import br.com.gvs.mobs.mobs.CustomGhast;
import br.com.gvs.mobs.mobs.CustomHorse;
import br.com.gvs.mobs.mobs.CustomIronGolem;
import br.com.gvs.mobs.mobs.CustomLargeFireball;
import br.com.gvs.mobs.mobs.CustomMagmaCube;
import br.com.gvs.mobs.mobs.CustomMushroomCow;
import br.com.gvs.mobs.mobs.CustomOcelot;
import br.com.gvs.mobs.mobs.CustomPig;
import br.com.gvs.mobs.mobs.CustomPigZombie;
import br.com.gvs.mobs.mobs.CustomSheep;
import br.com.gvs.mobs.mobs.CustomSilverfish;
import br.com.gvs.mobs.mobs.CustomSkeleton;
import br.com.gvs.mobs.mobs.CustomSlime;
import br.com.gvs.mobs.mobs.CustomSnowball;
import br.com.gvs.mobs.mobs.CustomSnowman;
import br.com.gvs.mobs.mobs.CustomSpider;
import br.com.gvs.mobs.mobs.CustomSquid;
import br.com.gvs.mobs.mobs.CustomVillager;
import br.com.gvs.mobs.mobs.CustomWitch;
import br.com.gvs.mobs.mobs.CustomWitherSkull;
import br.com.gvs.mobs.mobs.CustomWolf;
import br.com.gvs.mobs.mobs.CustomZombie;


public enum MobType
{
	
	
	
	BLAZE(6, "Blaze", 61, CustomBlaze.class, MobB.MONSTER),
	CAVE_SPIDER(4, "Aranha Venenosa", 59, CustomCaveSpider.class, MobB.MONSTER),
	CHICKEN(3, "Galinha", 93, CustomChicken.class, MobB.ANIMAL),
	COW(2, "Vaca", 92, CustomCow.class, MobB.ANIMAL),
	CREEPER(5, "Creeper", 50, CustomCreeper.class, MobB.MONSTER),
	ENDERMAN(7, "Enderman", 58, CustomEnderman.class, MobB.MONSTER),
	GHAST(8, "Ghast", 56, CustomGhast.class, MobB.MONSTER),
	HORSE(23, "Cavalo", 100, CustomHorse.class, MobB.ANIMAL),
	IRON_GOLEM(22, "Golem", 99, CustomIronGolem.class, MobB.ANIMAL),
	MAGMA_CUBE(12, "Cubo De Magma", 62, CustomMagmaCube.class, MobB.MONSTER),
	MUSHROOM_COW(13, "Vaca Cogumelo", 96, CustomMushroomCow.class, MobB.ANIMAL),
	PIG_ZOMBIE(17, "Porco Zumbi", 57, CustomPigZombie.class, MobB.MONSTER),
	PIG(1, "Porco", 90, CustomPig.class, MobB.ANIMAL),
	OCELOT(26, "Jaguatirica", 98, CustomOcelot.class, MobB.ANIMAL),
	SHEEP(14, "Ovelha", 91, CustomSheep.class, MobB.ANIMAL),
	SILVERFISH(15, "Silverfish", 60, CustomSilverfish.class, MobB.MONSTER),
	SKELETON(20, "Esqueleto", 51, CustomSkeleton.class, MobB.MONSTER),
	SLIME(10, "Slime", 55, CustomSlime.class, MobB.MONSTER),
	SNOWMAN(11, "Boneco De Neve", 97, CustomSnowman.class, MobB.ANIMAL),
	SPIDER(25, "Aranha", 52, CustomSpider.class, MobB.MONSTER),
	SQUID(24, "Lula", 94, CustomSquid.class, MobB.ANIMAL),
	VILLAGER(19, "Aldeão", 120, CustomVillager.class, MobB.ANIMAL),
	WITCH(18, "Bruxa", 66, CustomWitch.class, MobB.MONSTER),
	WOLF(21, "Lobo", 95, CustomWolf.class, MobB.ANIMAL),
	ZOMBIE(16, "Zumbi", 54, CustomZombie.class, MobB.MONSTER),
	EGG(26, "Ovo", 7, CustomEgg.class),
	SNOWBALL(27, "Bola De Neve", 11, CustomSnowball.class),
	WITHER_SKULL(28, "Cabeça de wither", 19, CustomWitherSkull.class),
	LARGE_FIREBALL(29, "Bola de fogo", 12, CustomLargeFireball.class);
	
	private int mobID;
	private String name;
	private int minecraftID;
	private MobB mobB;
	private Class<? extends Entity> clazz;
	
	MobType(int mobID, String name, int minecraftID, Class<? extends Entity> clazz, MobB mobB)
	{
		this.mobID = mobID;
		this.name = name;
		this.mobB = mobB;
		this.minecraftID = minecraftID;
		this.clazz = clazz;
	}
	
	MobType(int mobID, String name, int minecraftID, Class<? extends Entity> clazz)
	{
		this.mobID = mobID;
		this.name = name;
		this.minecraftID = minecraftID;
		this.clazz = clazz;
	}
	
	public enum MobB {
		MONSTER, ANIMAL;
	}
	
	public static MobType getMobByID(int id)
	{
		for(MobType mt : values())
		{
			if(mt.getMobID() == id)
			{
				return mt;
			}
		}
		return null;
	}
	
	public MobB getMobTypeB(){
		return this.mobB;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getMobID()
	{
		return this.mobID;
	}
	
	public int getMinecraftID()
	{
		return this.minecraftID;
	}
	
	public Class<? extends Entity> getMobClass()
	{
		return this.clazz;
	}
	
	public static MobType getMobType(String name)
	{
		for(MobType mobType : MobType.values())
		{
			if(mobType.name().equalsIgnoreCase(name))
			{
				return mobType;
			}
		}
		return null;
	}
	
}
