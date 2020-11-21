package com.min01.kriltsutsaroth.procedure;

import net.minecraft.entity.Entity;

import java.util.Map;

import com.min01.kriltsutsaroth.ElementsKriltsutsarothMod;

@ElementsKriltsutsarothMod.ModElement.Tag
public class ProcedureMysticFireStaffBulletHitsLivingEntity extends ElementsKriltsutsarothMod.ModElement {
	public ProcedureMysticFireStaffBulletHitsLivingEntity(ElementsKriltsutsarothMod instance) {
		super(instance, 3);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			System.err.println("Failed to load dependency entity for procedure MysticFireStaffBulletHitsLivingEntity!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		entity.setFire((int) 100);
	}
}
