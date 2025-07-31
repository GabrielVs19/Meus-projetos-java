package br.com.gvs.mobs.bosses;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EntityBat;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityPotion;
import net.minecraft.server.v1_7_R2.EntityWitch;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

public class CustomPandora extends EntityWitch implements CustomBoss{

	private Boss boss;
	public CustomPandora(World world, Boss boss) {
		super(world);
		this.boss = boss;
		this.resetAI();
		this.setAI();
		
	}

	@Override
	public Boss getBoss() {
		return this.boss;
	}
	
	@Override
	public void resetAI() {
		try{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(this.goalSelector, new UnsafeList<Object>());
			bField.set(this.targetSelector, new UnsafeList<Object>());
			cField.set(this.goalSelector, new UnsafeList<Object>());
			cField.set(this.targetSelector, new UnsafeList<Object>());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setAI() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.0D, 60, 10.0F));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
	}
	
	/*@Override
	public boolean damageEntity(DamageSource paramDamageSource, float paramFloat)
	{
		BossDamageByPlayerEvent event = new BossDamageByPlayerEvent(this.getBukkitEntity(), this.boss);
		CraftEventFactory.callEvent(event);
		if(event.isCancelled()){
			return super.damageEntity(paramDamageSource, paramFloat);
		}
		return false;
	}*/
	
	@Override
	public void die(DamageSource damagesource)
	{
		super.die(damagesource);
		Location loc = this.getBukkitEntity().getLocation();
		loc.setY(loc.getY() + 1.5);
		for(int i = 0; i <= 5;i++){
			EntityBat bat = new EntityBat(this.world);
			bat.setCustomName("Morcego");
			bat.setCustomNameVisible(true);
			bat.setPosition(loc.getX(), loc.getY(), loc.getZ());
			this.world.addEntity(bat);
		}
	}
	
	public boolean damageEntity(DamageSource damagesource, float f)
	  {
		pandoraInv();
		return super.damageEntity(damagesource, f);
	  }
	
	private void pandoraInv(){
		int random = (int) (Math.random() * 99) +1;
		if(random <= 35){
			this.setInvisible(true);
			new BukkitRunnable(){

				@Override
				public void run() {
					setInvisible(false);
				}
				
			}.runTaskLater(Mobs.getInstance(), 20 * 2);
		}
	}

	public void a(EntityLiving entityliving, float f){
		if (!bZ()){
			EntityPotion entitypotion = new EntityPotion(this.world, this, 32732);

			entitypotion.pitch -= -20.0F;
			double d0 = entityliving.locX + entityliving.motX - this.locX;
			double d1 = entityliving.locY + entityliving.getHeadHeight() - 1.100000023841858D - this.locY;
			double d2 = entityliving.locZ + entityliving.motZ - this.locZ;
			float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2);
			if ((f1 >= 8.0F) && (!entityliving.hasEffect(MobEffectList.SLOWER_MOVEMENT))) {
				entitypotion.setPotionValue(32698);
			} else if ((entityliving.getHealth() >= 8.0F) && (!entityliving.hasEffect(MobEffectList.POISON))) {
				entitypotion.setPotionValue(32660);
			} else if ((f1 <= 3.0F) && (!entityliving.hasEffect(MobEffectList.WEAKNESS)) && (this.random.nextFloat() < 0.25F)) {
				entitypotion.setPotionValue(32696);
			}
			entitypotion.shoot(d0, d1 + f1 * 0.2F, d2, 0.75F, 8.0F);
			this.world.addEntity(entitypotion);
			}
	}

	@Override
	public void setWalkToLocation(Location loc) {
		this.goalSelector.a(5, new PathfinderGoalWalkToSpawn(this, loc));	
	}

}
