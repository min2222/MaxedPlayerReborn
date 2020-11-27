
package com.min01.kriltsutsaroth.entity;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;

import java.util.Iterator;
import java.util.ArrayList;

import com.min01.kriltsutsaroth.ElementsKriltsutsarothMod;
import com.min01.kriltsutsaroth.item.ItemBloodStaff;
import com.min01.kriltsutsaroth.item.ItemDeathStaff;
import com.min01.kriltsutsaroth.item.ItemMysticAirStaff;
import com.min01.kriltsutsaroth.item.ItemMysticEarthStaff;
import com.min01.kriltsutsaroth.item.ItemMysticFireStaff;
import com.min01.kriltsutsaroth.item.ItemMysticFireStaff.EntityArrowCustom;
import com.min01.kriltsutsaroth.item.ItemMysticIceStaff;
import com.min01.kriltsutsaroth.item.ItemPolyporeStaff;
import com.min01.kriltsutsaroth.item.ItemWeakenStaff;

@ElementsKriltsutsarothMod.ModElement.Tag
public class EntityMaxedPlayer extends ElementsKriltsutsarothMod.ModElement {
	public static final int ENTITYID = 3;
	public static final int ENTITYID_RANGED = 4;
	public EntityMaxedPlayer(ElementsKriltsutsarothMod instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> EntityEntryBuilder.create().entity(EntityCustom.class)
				.id(new ResourceLocation("kriltsutsaroth", "maxed_player"), ENTITYID).name("maxed_player").tracker(64, 3, true).egg(-1, -1).build());
	}

	private Biome[] allbiomes(net.minecraft.util.registry.RegistryNamespaced<ResourceLocation, Biome> in) {
		Iterator<Biome> itr = in.iterator();
		ArrayList<Biome> ls = new ArrayList<Biome>();
		while (itr.hasNext())
			ls.add(itr.next());
		return ls.toArray(new Biome[ls.size()]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityCustom.class, renderManager -> {
			RenderBiped customRender = new RenderBiped(renderManager, new ModelZombie(), 0.5f) {
				protected ResourceLocation getEntityTexture(Entity entity) {
					return new ResourceLocation("kriltsutsaroth:textures/completionist-cape.png");
				}
			};
			customRender.addLayer(new net.minecraft.client.renderer.entity.layers.LayerBipedArmor(customRender) {
				protected void initArmor() {
					this.modelLeggings = new ModelBiped(0.5f);
					this.modelArmor = new ModelBiped(1);
				}
			});
			return customRender;
		});
	}
	public static class EntityCustom extends EntityMob implements IRangedAttackMob {
	    private final EntityAIAttackRangedBow<EntityMaxedPlayer.EntityCustom> aiArrowAttack = new EntityAIAttackRangedBow<EntityMaxedPlayer.EntityCustom>(this, 0.30000001192092896D, 30, 10.0F);
	    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 0.3100000023841858D, false);
		boolean attacked;
		int unretaliatedHits;
		public EntityCustom(World world) {
			super(world);
			setSize(0.6f, 1.8f);
			experienceValue = 0;
			this.isImmuneToFire = false;
			this.unretaliatedHits = 0;
			this.attacked = false;
			setNoAI(!true);
			enablePersistence();
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemMysticFireStaff.block, (int) (1)));
		}

		@Override
		protected void initEntityAI() {
			super.initEntityAI();
			this.tasks.addTask(1, new EntityAIAttackRanged(this, 0.30000001192092896D, 30, 10.0F));
			this.tasks.addTask(2, new EntityAIWander(this, 1));
			this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
			this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityLiving.class, false, false));
			this.tasks.addTask(5, new EntityAILookIdle(this));
			this.tasks.addTask(6, new EntityAISwimming(this));
			//this.tasks.addTask(7, new EntityAIAttackMelee(this, 0.3100000023841858D, false));
		}

		@Override
		public EnumCreatureAttribute getCreatureAttribute() {
			return EnumCreatureAttribute.UNDEFINED;
		}

		@Override
		protected boolean canDespawn() {
			return false;
		}

		@Override
		protected Item getDropItem() {
			return null;
		}

		@Override
		public net.minecraft.util.SoundEvent getAmbientSound() {
			return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.skeleton.ambient"));
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.skeleton.hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.skeleton.death"));
		}

		@Override
		protected float getSoundVolume() {
			return 1.0F;
		}

		@Override
		protected void applyEntityAttributes() {
			super.applyEntityAttributes();
			if (this.getEntityAttribute(SharedMonsterAttributes.ARMOR) != null)
				this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(24D);
			if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED) != null)
				this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
			if (this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH) != null)
				this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1390D);
			if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
				this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10D);				
		}
		
		@Override
		public void onLivingUpdate() {
			this.heal(1.0f);
	        if (this.attacked || this.unretaliatedHits > 10) {
	            this.unretaliatedHits = 0;
	            double rand2 = 0.0;
	            if (this.world.getDifficulty() == EnumDifficulty.HARD) {
	                rand2 = Math.random() * 10.0;
	            }
	            else 
	            {
	                rand2 = Math.random() * 6.0;
	            }
	            if (rand2 > 8.0) {
	                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemWeakenStaff.block, (int) (1)));
	            }
	            else if(rand2 > 7.0)
	            {
	            	 this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemDeathStaff.block, (int) (1)));
	            }
	            else if(rand2 > 6.0)
	            {
	            	 this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemBloodStaff.block, (int) (1)));
	            }
	            else if(rand2 > 5.0)
	            {
	            	 this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemPolyporeStaff.block, (int) (1)));
	            }
	            /*else if(rand2 > 5.0)
	            {
	            	 this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.APPLE, (int) (1)));
	            }*/
	            /*else if(rand2 > 5.0)
	            {
	                if (this.world.getDifficulty().getDifficultyId() == 3) {
	                    //setCurrentItemOrArmor(0, new ItemStack(Wildycraft.chaoticsword));
	                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.APPLE, (int) (1)));
	                    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(120.0D);
	                  } else {
	                    //setCurrentItemOrArmor(0, new ItemStack(Wildycraft.dragonsword));
	                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.APPLE, (int) (1)));
	                    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(60.0D);
	                  }
	            }*/
	            else if (rand2 > 4.0) 
	            {
	            	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemMysticAirStaff.block, (int) (1)));
	            }
	            else if (rand2 > 3.0) 
	            {
	            	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemMysticIceStaff.block, (int) (1)));
	            }
	            else if (rand2 > 2.0) 
	            {
	            	 this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemMysticEarthStaff.block, (int) (1)));
	            }
	            else if (rand2 > 1.0)
	            {
	            	 
	            	 this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemMysticFireStaff.block, (int) (1)));
	            }
	            this.attacked = false;
	        }
	
			super.onLivingUpdate();
		}
		
	    public boolean attackEntityFrom(final DamageSource par1DamageSource, final float par2) {
	        if (super.attackEntityFrom(par1DamageSource, par2)) {
	            if (!par1DamageSource.isUnblockable()) {
	                ++this.unretaliatedHits;
	            }
	            return true;
	        }
	        return false;
	    }
	    
	    public void setCombatTask()
	    {
	            this.tasks.removeTask(this.aiAttackOnCollide);
	            this.tasks.removeTask(this.aiArrowAttack);
	            ItemStack var1 = this.getHeldItemMainhand();

	            if (var1 != null && (var1.getItem() == ItemMysticFireStaff.block || var1.getItem() == ItemMysticAirStaff.block || var1.getItem() == ItemMysticIceStaff.block || var1.getItem() == ItemMysticEarthStaff.block ||
	            		var1.getItem() == ItemWeakenStaff.block || var1.getItem() == ItemBloodStaff.block || var1.getItem() == ItemDeathStaff.block || var1.getItem() == ItemPolyporeStaff.block))
	            {
	                this.tasks.addTask(4, this.aiArrowAttack);
	            }
	            else
	            {
	                this.tasks.addTask(4, this.aiAttackOnCollide);
	            }
	    }

		@Override
		public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
			for (int i = 0; i < 10; ++i) {
				EntityLivingBase entity = this;
				if (this.getHeldItemMainhand().getItem() == ItemMysticFireStaff.block) 
				{   
					EntityArrowCustom var2 = new EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.fizz"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                final double d0 = target.posX - this.posX;
		    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
		    	        final double d3 = target.posZ - this.posZ;
		    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	                var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	                var2.setDamage(35);
	                this.world.spawnEntity((Entity)var2);        
	            }
				else if (this.getHeldItemMainhand().getItem() == ItemMysticIceStaff.block) 
				{
					com.min01.kriltsutsaroth.item.ItemMysticIceStaff.EntityArrowCustom var2 = new com.min01.kriltsutsaroth.item.ItemMysticIceStaff.EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.pop"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                this.world.spawnEntity((Entity)var2);
	                final double d0 = target.posX - this.posX;
	    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
	    	        final double d3 = target.posZ - this.posZ;
	    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	    	        var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	    	        var2.setDamage(35);
	            }
				else if (this.getHeldItemMainhand().getItem() == ItemMysticAirStaff.block) 
				{
					com.min01.kriltsutsaroth.item.ItemMysticAirStaff.EntityArrowCustom var2 = new com.min01.kriltsutsaroth.item.ItemMysticAirStaff.EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.breath"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                this.world.spawnEntity((Entity)var2);
	                final double d0 = target.posX - this.posX;
	    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
	    	        final double d3 = target.posZ - this.posZ;
	    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	    	        var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	    	        var2.setDamage(35);
	            }
				
				else if (this.getHeldItemMainhand().getItem() == ItemMysticEarthStaff.block) 
				{
					com.min01.kriltsutsaroth.item.ItemMysticEarthStaff.EntityArrowCustom var2 = new com.min01.kriltsutsaroth.item.ItemMysticEarthStaff.EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.pop"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                this.world.spawnEntity((Entity)var2);
	                final double d0 = target.posX - this.posX;
	    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
	    	        final double d3 = target.posZ - this.posZ;
	    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	    	        var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	    	        var2.setDamage(35);
	            }
				else if (this.getHeldItemMainhand().getItem() == ItemWeakenStaff.block) 
				{
					com.min01.kriltsutsaroth.item.ItemWeakenStaff.EntityArrowCustom var2 = new com.min01.kriltsutsaroth.item.ItemWeakenStaff.EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.pop"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                this.world.spawnEntity((Entity)var2);
	                final double d0 = target.posX - this.posX;
	    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
	    	        final double d3 = target.posZ - this.posZ;
	    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	    	        var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	    	        var2.setDamage(35);
	            }
				else if (this.getHeldItemMainhand().getItem() == ItemDeathStaff.block) 
				{
					com.min01.kriltsutsaroth.item.ItemDeathStaff.EntityArrowCustom var2 = new com.min01.kriltsutsaroth.item.ItemDeathStaff.EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.pop"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                this.world.spawnEntity((Entity)var2);
	                final double d0 = target.posX - this.posX;
	    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
	    	        final double d3 = target.posZ - this.posZ;
	    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	    	        var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	    	        var2.setDamage(35);
	            }
				else if (this.getHeldItemMainhand().getItem() == ItemBloodStaff.block) 
				{
					com.min01.kriltsutsaroth.item.ItemBloodStaff.EntityArrowCustom var2 = new com.min01.kriltsutsaroth.item.ItemBloodStaff.EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.pop"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                this.world.spawnEntity((Entity)var2);
	                final double d0 = target.posX - this.posX;
	    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
	    	        final double d3 = target.posZ - this.posZ;
	    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	    	        var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	    	        var2.setDamage(35);
	            }
				else if (this.getHeldItemMainhand().getItem() == ItemPolyporeStaff.block) 
				{
					com.min01.kriltsutsaroth.item.ItemPolyporeStaff.EntityArrowCustom var2 = new com.min01.kriltsutsaroth.item.ItemPolyporeStaff.EntityArrowCustom(this.world, this);
					world.playSound((EntityPlayer) null, (double) this.posX, (double) this.posY, (double) this.posZ,
							(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY
									.getObject(new ResourceLocation(("kriltsutsaroth:random.pop"))),
							SoundCategory.NEUTRAL, 1, 1f / ((this.getRNG().nextFloat() * 0.4f + 0.8f)));
	                this.world.spawnEntity((Entity)var2);
	                final double d0 = target.posX - this.posX;
	    	        final double d2 = target.posY + target.getEyeHeight() - 1.100000023841858 - var2.posY;
	    	        final double d3 = target.posZ - this.posZ;
	    	        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
	    	        var2.shoot(d0, d2 + f1, d3, 1.6f, 12.0f);
	    	        var2.setDamage(35);
	            }
				}
			}
		@Override
		public void setSwingingArms(boolean swingingArms) {
			// TODO Auto-generated method stub
			
		}
		}

}
