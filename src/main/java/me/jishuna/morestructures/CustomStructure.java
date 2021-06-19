package me.jishuna.morestructures;

import org.bukkit.Bukkit;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.data.worldgen.WorldGenFeaturePillagerOutpostPieces;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenStage.Decoration;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.feature.WorldGenFeaturePillagerOutpost;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureVillageConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructureJigsawPlacement;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.WorldGenFeaturePillagerOutpostPoolPiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;

public class CustomStructure extends StructureGenerator<WorldGenFeatureVillageConfiguration> {
	public CustomStructure() {
		super(WorldGenFeatureVillageConfiguration.a);
	}

	@Override
	public StructureGenerator.a<WorldGenFeatureVillageConfiguration> a() {
		return Start::new;
	}

	@Override
	public Decoration d() {
		return Decoration.e;
	}

	public static class Start extends StructureStart<WorldGenFeatureVillageConfiguration> {

		public Start(StructureGenerator<WorldGenFeatureVillageConfiguration> generator, ChunkCoordIntPair chunkCoord,
				int i, long l) {
			super(generator, chunkCoord, i, l);
		}

		@Override
		public void a(IRegistryCustom registry, ChunkGenerator generator, DefinedStructureManager structureManager,
				ChunkCoordIntPair chunkCoord, BiomeBase biome, WorldGenFeatureVillageConfiguration config,
				LevelHeightAccessor world) {
			int x = (chunkCoord.b << 4) + 7;
			int z = (chunkCoord.c << 4) + 7;

			/*
			 * We pass this into addPieces to tell it where to generate the structure. If
			 * addPieces's last parameter is true, blockpos's Y value is ignored and the
			 * structure will spawn at terrain height instead. Set that parameter to false
			 * to force the structure to spawn at blockpos's Y value instead. You got
			 * options here!
			 */
			BlockPosition blockpos = new BlockPosition(x, 0, z);
			StructurePools.init();

			WorldGenFeatureDefinedStructureJigsawPlacement.a(registry,
					new WorldGenFeatureVillageConfiguration(() -> WorldGenFeaturePillagerOutpostPieces.a, 2),
					WorldGenFeaturePillagerOutpostPoolPiece::new, generator, structureManager, blockpos, this, this.d,
					true, true, world);
			this.b();

			System.out.println(config.c().get().b().toString());

			Bukkit.getLogger().info(
					"Thing at " + this.c.get(0).f().g() + " " + this.c.get(0).f().h() + " " + this.c.get(0).f().i());
		}
	}
}