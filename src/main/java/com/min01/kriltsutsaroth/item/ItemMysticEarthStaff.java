
package com.min01.kriltsutsaroth.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.World;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.EnumAction;
import net.minecraft.init.Blocks;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import com.min01.kriltsutsaroth.ElementsKriltsutsarothMod;
import com.min01.kriltsutsaroth.item.ItemMysticFireStaff.EntityArrowCustom;

@ElementsKriltsutsarothMod.ModElement.Tag
public class ItemMysticEarthStaff extends ElementsKriltsutsarothMod.ModElement {
	@GameRegistry.ObjectHolder("kriltsutsaroth:mystic_earth_staff")
	public static final Item block = null;
	public static final int ENTITYID = 23;
	public ItemMysticEarthStaff(ElementsKriltsutsarothMod instance) {
		super(instance, 7);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new RangedItem());
		elements.entities.add(() -> EntityEntryBuilder.create().entity(EntityArrowCustom.class)
				.id(new ResourceLocation("kriltsutsaroth", "entitybulletmystic_earth_staff"), ENTITYID).name("entitybulletmystic_earth_staff")
				.tracker(64, 1, true).build());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("kriltsutsaroth:mystic_earth_staff", "inventory"));
	}

	public static class RangedItem extends Item {
		public RangedItem() {
			super();
			setMaxDamage(100);
			setFull3D();
			setUnlocalizedName("mystic_earth_staff");
			setRegistryName("mystic_earth_staff");
			maxStackSize = 1;
			setCreativeTab(CreativeTabs.COMBAT);
		}

		@Override
		public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityLivingBase entityLivingBase, int timeLeft) {
			for (int i = 0; i < 15; i++) {
			if (!world.isRemote && entityLivingBase instanceof EntityPlayerMP) {
				EntityPlayerMP entity = (EntityPlayerMP) entityLivingBase;
				float power = 1f;
				EntityArrowCustom entityarrow = new EntityArrowCustom(world, entity);
				entityarrow.shoot(entity.getLookVec().x, entity.getLookVec().y, entity.getLookVec().z, power * 2, 0);
				entityarrow.setSilent(true);
				entityarrow.setIsCritical(false);
				entityarrow.setDamage(35);
				entityarrow.setKnockbackStrength(1);
				itemstack.damageItem(1, entity);
		        Random rand = new Random();
		        entityarrow.motionX += rand.nextGaussian() * 0.2D;
		        entityarrow.motionY *= Math.random() * 0.2D + 0.9D;
		        entityarrow.motionZ += rand.nextGaussian() * 0.2D;
				int x = (int) entity.posX;
				int y = (int) entity.posY;
				int z = (int) entity.posZ;
				world.playSound((EntityPlayer) null, (double) x, (double) y, (double) z,
						(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(("kriltsutsaroth:random.pop"))),
						SoundCategory.NEUTRAL, 1, 1f / (itemRand.nextFloat() * 0.5f + 1f) + (power / 2));
				entityarrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
				if (!world.isRemote)
					world.spawnEntity(entityarrow);
			}
			}
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entity, EnumHand hand) {
			entity.setActiveHand(hand);
			return new ActionResult(EnumActionResult.SUCCESS, entity.getHeldItem(hand));
		}

		@Override
		public EnumAction getItemUseAction(ItemStack itemstack) {
			return EnumAction.BOW;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack itemstack) {
			return 72000;
		}
	}

	public static class EntityArrowCustom extends EntityTippedArrow {
		public EntityArrowCustom(World a) {
			super(a);
		}

		public EntityArrowCustom(World worldIn, double x, double y, double z) {
			super(worldIn, x, y, z);
		}

		public EntityArrowCustom(World worldIn, EntityLivingBase shooter) {
			super(worldIn, shooter);
		}

		@Override
		protected void arrowHit(EntityLivingBase entity) {
			super.arrowHit(entity);
			entity.setArrowCountInEntity(entity.getArrowCountInEntity() - 1);
			int x = (int) this.posX;
			int y = (int) this.posY;
			int z = (int) this.posZ;
			World world = this.world;
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1.0F, true);
		}

		@Override
		public void onUpdate() {
			super.onUpdate();
			int x = (int) this.posX;
			int y = (int) this.posY;
			int z = (int) this.posZ;
			World world = this.world;
			Entity entity = (Entity) shootingEntity;
			int var9;
			for (var9 = 0; var9 < 4; var9++)
		        this.world.spawnParticle(EnumParticleTypes.TOWN_AURA, this.posX + this.motionX * var9 / 4.0D, this.posY + this.motionY * var9 / 4.0D, this.posZ + this.motionZ * var9 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ); 
			if (this.inGround) {
				this.world.removeEntity(this);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public abstract class RenderCustom<T extends EntityArrowCustom> extends Render<T>
	{
	    public RenderCustom(RenderManager renderManagerIn)
	    {
	        super(renderManagerIn);
	    }
	    
		@Override
		protected ResourceLocation getEntityTexture(EntityArrowCustom entity) {
			return new ResourceLocation("kriltsutsaroth:textures/fireblast.png");
		}

	    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	    {
	        this.bindEntityTexture(entity);
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.pushMatrix();
	        GlStateManager.disableLighting();
	        GlStateManager.translate((float)x, (float)y, (float)z);
	        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder bufferbuilder = tessellator.getBuffer();
	        int i = 0;
	        float f = 0.0F;
	        float f1 = 0.5F;
	        float f2 = 0.0F;
	        float f3 = 0.15625F;
	        float f4 = 0.0F;
	        float f5 = 0.15625F;
	        float f6 = 0.15625F;
	        float f7 = 0.3125F;
	        float f8 = 0.05625F;
	        GlStateManager.enableRescaleNormal();
	        float f9 = (float)entity.arrowShake - partialTicks;

	        if (f9 > 0.0F)
	        {
	            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
	            GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
	        }

	        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
	        GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);
	        GlStateManager.translate(-4.0F, 0.0F, 0.0F);

	        if (this.renderOutlines)
	        {
	            GlStateManager.enableColorMaterial();
	            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
	        }

	        GlStateManager.glNormal3f(0.05625F, 0.0F, 0.0F);
	        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	        bufferbuilder.pos(-7.0D, -2.0D, -2.0D).tex(0.0D, 0.15625D).endVertex();
	        bufferbuilder.pos(-7.0D, -2.0D, 2.0D).tex(0.15625D, 0.15625D).endVertex();
	        bufferbuilder.pos(-7.0D, 2.0D, 2.0D).tex(0.15625D, 0.3125D).endVertex();
	        bufferbuilder.pos(-7.0D, 2.0D, -2.0D).tex(0.0D, 0.3125D).endVertex();
	        tessellator.draw();
	        GlStateManager.glNormal3f(-0.05625F, 0.0F, 0.0F);
	        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	        bufferbuilder.pos(-7.0D, 2.0D, -2.0D).tex(0.0D, 0.15625D).endVertex();
	        bufferbuilder.pos(-7.0D, 2.0D, 2.0D).tex(0.15625D, 0.15625D).endVertex();
	        bufferbuilder.pos(-7.0D, -2.0D, 2.0D).tex(0.15625D, 0.3125D).endVertex();
	        bufferbuilder.pos(-7.0D, -2.0D, -2.0D).tex(0.0D, 0.3125D).endVertex();
	        tessellator.draw();

	        for (int j = 0; j < 4; ++j)
	        {
	            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
	            GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
	            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	            bufferbuilder.pos(-8.0D, -2.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
	            bufferbuilder.pos(8.0D, -2.0D, 0.0D).tex(0.5D, 0.0D).endVertex();
	            bufferbuilder.pos(8.0D, 2.0D, 0.0D).tex(0.5D, 0.15625D).endVertex();
	            bufferbuilder.pos(-8.0D, 2.0D, 0.0D).tex(0.0D, 0.15625D).endVertex();
	            tessellator.draw();
	        }

	        if (this.renderOutlines)
	        {
	            GlStateManager.disableOutlineMode();
	            GlStateManager.disableColorMaterial();
	        }

	        GlStateManager.disableRescaleNormal();
	        GlStateManager.enableLighting();
	        GlStateManager.popMatrix();
	        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	    }
	}
}
