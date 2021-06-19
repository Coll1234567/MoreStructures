package me.jishuna.morestructures;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureVillageConfiguration;

public class ConfiguredStructures {

	public static final StructureFeature<WorldGenFeatureVillageConfiguration, ?> TEST = MoreStructures.TEST
			.a(new WorldGenFeatureVillageConfiguration(() -> StructurePools.TEST_POOL, 10));

	public static void init() {
	}

}
