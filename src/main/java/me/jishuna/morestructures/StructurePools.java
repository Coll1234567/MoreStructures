package me.jishuna.morestructures;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.data.worldgen.WorldGenFeaturePieces;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructurePoolLegacySingle;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructurePoolStructure;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructurePoolTemplate;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructurePoolTemplate.Matching;

public class StructurePools {

	public static WorldGenFeatureDefinedStructurePoolTemplate TEST_POOL;
	public static MinecraftKey TEST = new MinecraftKey("customstructures:test_structure_pool");

	static {
		TEST_POOL = WorldGenFeaturePieces.a(new WorldGenFeatureDefinedStructurePoolTemplate(TEST,
				new MinecraftKey("empty"),
				List.of(Pair.of(WorldGenFeatureDefinedStructurePoolStructure.a("igloo/top"), 2)),
				Matching.b));
	}

	public static void init() {
	}

}
