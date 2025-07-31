package br.com.gvs.mobs.bosses;

import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityWither;
import net.minecraft.server.v1_7_R2.IRangedEntity;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;

import br.com.gvs.mobs.mobs.CustomWitherSkull;
import br.com.gvs.mobs.pathfinders.PathfinderGoalTpToSpawn;
import br.com.gvs.mobs.util.Boss;

public class CustomWither extends EntityWither implements CustomBoss, IRangedEntity{
	
	private Boss boss;
	public CustomWither(World world, Boss boss) {
		super(world);
		this.boss = boss;
		this.fireProof = true;
	}

	@Override
	public Boss getBoss() {
		return this.boss;
	}
	
	@Override
	public void resetAI() {
		
	}

	@Override
	public void setAI() {
		
	}
	
	@Override
	public void setWalkToLocation(Location loc) {
		this.goalSelector.a(9, new PathfinderGoalTpToSpawn(this, loc, 100));	
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
	  public void a(EntityLiving entityliving, float f){
		witherShootHead(0, entityliving);
	  }
	
	  public void witherShootHead(int i, EntityLiving entityliving){
		  shootWitherHead(i, entityliving.locX, entityliving.locY + entityliving.getHeadHeight() * 0.5D, entityliving.locZ, (i == 0) && (this.random.nextFloat() < 0.001F));
	  }
	
	public void shootWitherHead(int i, double d0, double d1, double d2, boolean flag){
	    this.world.a((EntityHuman)null, 1014, (int)this.locX, (int)this.locY, (int)this.locZ, 0);
	    double d3 = u(i);
	    double d4 = v(i);
	    double d5 = w(i);
	    double d6 = d0 - d3;
	    double d7 = d1 - d4;
	    double d8 = d2 - d5;
	    CustomWitherSkull entitywitherskull = new CustomWitherSkull(this.world, this, d6, d7, d8);
	    if (flag) {
	      entitywitherskull.a(true);
	    }
	    entitywitherskull.locY = d4;
	    entitywitherskull.locX = d3;
	    entitywitherskull.locZ = d5;
	    this.world.addEntity(entitywitherskull);
	  }

	private double u(int i){
	    if (i <= 0) {
	      return this.locX;
	    }
	    float f = (this.aM + 180 * (i - 1)) / 180.0F * 3.141593F;
	    float f1 = MathHelper.cos(f);
	    
	    return this.locX + f1 * 1.3D;
	  }
	  
	  private double v(int i){
	    return i <= 0 ? this.locY + 3.0D : this.locY + 2.2D;
	  }
	  
	  private double w(int i){
	    if (i <= 0) {
	      return this.locZ;
	    }
	    float f = (this.aM + 180 * (i - 1)) / 180.0F * 3.141593F;
	    float f1 = MathHelper.sin(f);
	    
	    return this.locZ + f1 * 1.3D;
	  }
	
}
