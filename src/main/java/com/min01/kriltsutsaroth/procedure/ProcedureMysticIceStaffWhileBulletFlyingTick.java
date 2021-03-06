package com.min01.kriltsutsaroth.procedure;

import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import net.minecraft.util.EnumParticleTypes;

import java.util.Map;

import com.min01.kriltsutsaroth.ElementsKriltsutsarothMod;

@ElementsKriltsutsarothMod.ModElement.Tag
public class ProcedureMysticIceStaffWhileBulletFlyingTick extends ElementsKriltsutsarothMod.ModElement {
	public ProcedureMysticIceStaffWhileBulletFlyingTick(ElementsKriltsutsarothMod instance) {
		super(instance, 5);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			System.err.println("Failed to load dependency x for procedure MysticIceStaffWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			System.err.println("Failed to load dependency y for procedure MysticIceStaffWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			System.err.println("Failed to load dependency z for procedure MysticIceStaffWhileBulletFlyingTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			System.err.println("Failed to load dependency world for procedure MysticIceStaffWhileBulletFlyingTick!");
			return;
		}
		int x = (int) dependencies.get("x");
		int y = (int) dependencies.get("y");
		int z = (int) dependencies.get("z");
		World world = (World) dependencies.get("world");
		if (world instanceof WorldServer)
			((WorldServer) world).spawnParticle(EnumParticleTypes.SNOWBALL, x, y, z, (int) 10, 1, 1, 1, 0.01, new int[0]);
	}
}
