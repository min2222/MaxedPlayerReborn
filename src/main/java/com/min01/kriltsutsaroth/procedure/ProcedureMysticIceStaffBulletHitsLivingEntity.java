package com.min01.kriltsutsaroth.procedure;

import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

import java.util.Map;

import com.min01.kriltsutsaroth.ElementsKriltsutsarothMod;

@ElementsKriltsutsarothMod.ModElement.Tag
public class ProcedureMysticIceStaffBulletHitsLivingEntity extends ElementsKriltsutsarothMod.ModElement {
	public ProcedureMysticIceStaffBulletHitsLivingEntity(ElementsKriltsutsarothMod instance) {
		super(instance, 4);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			System.err.println("Failed to load dependency entity for procedure MysticIceStaffBulletHitsLivingEntity!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if (entity instanceof EntityLivingBase)
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, (int) 100, (int) 3));
	}
}
