package me.jishuna.morestructures;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.core.IRegistry;
import net.minecraft.data.RegistryGeneration;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.StructureSettingsFeature;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureVillageConfiguration;

public class MoreStructures extends JavaPlugin implements Listener {

	private static Field structureField;
	private static Field structureConfigField;

	static {
		try {
			structureField = BiomeSettingsGeneration.class.getDeclaredField("g");
			structureField.setAccessible(true);

			structureConfigField = StructureSettings.class.getDeclaredField("d");
			structureConfigField.setAccessible(true);

		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public static final StructureGenerator<WorldGenFeatureVillageConfiguration> TEST = new CustomStructure();

	@Override
	public void onLoad() {
		StructureGenerator.a.put("test_structure", TEST);

		IRegistry.a(IRegistry.aW, new MinecraftKey("customstructures:teststucture"), TEST);

		try {
			ReflectionUtils.setStaticFieldUsingUnsafe(StructureGenerator.class.getDeclaredField("t"),
					ImmutableList.<StructureGenerator<?>>builder().addAll(StructureGenerator.t).add(TEST).build());

			ReflectionUtils.setStaticFieldUsingUnsafe(StructureSettings.class.getDeclaredField("b"),
					ImmutableMap.<StructureGenerator<?>, StructureSettingsFeature>builder().putAll(StructureSettings.b)
							.put(TEST, new StructureSettingsFeature(2, 1, 3565678)).build());

			RegistryGeneration.j.d().forEach(settings -> {
				Map<StructureGenerator<?>, StructureSettingsFeature> structureMap = settings.getValue().a().a();

				/*
				 * Pre-caution in case a mod makes the structure map immutable like datapacks
				 * do. I take no chances myself. You never know what another mods does...
				 *
				 * structureConfig requires AccessTransformer (See
				 * resources/META-INF/accesstransformer.cfg)
				 */
				if (structureMap instanceof ImmutableMap) {
					Map<StructureGenerator<?>, StructureSettingsFeature> tempMap = new HashMap<>(structureMap);
					tempMap.put(TEST, new StructureSettingsFeature(2, 1, 3565678));
					try {
						structureConfigField.set(settings.getValue().a(), tempMap);
					} catch (ReflectiveOperationException e) {
						e.printStackTrace();
					}
				} else {
					structureMap.put(TEST, new StructureSettingsFeature(2, 1, 3565678));
				}
			});
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		IRegistry.a(RegistryGeneration.f, new MinecraftKey("customstructures:teststuctureconfigured"),
				ConfiguredStructures.TEST);
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void worldLoad(WorldLoadEvent event) {
		WorldServer world = ((CraftWorld) event.getWorld()).getHandle();

		try {
			Map<StructureGenerator<?>, StructureSettingsFeature> map = world.getChunkProvider().d.getSettings().a();
			map.put(TEST, new StructureSettingsFeature(2, 1, 3565678));

			structureConfigField.set(world.getChunkProvider().d.getSettings(), map);

			Iterator<BiomeBase> iter = ((CraftServer) Bukkit.getServer()).getServer().l.b(IRegistry.aO).iterator();

			while (iter.hasNext()) {
				BiomeBase biome = iter.next();
			//	System.out.println(biome.toString());

				List<Supplier<StructureFeature<?, ?>>> features = new ArrayList<Supplier<StructureFeature<?, ?>>>(
						(List<Supplier<StructureFeature<?, ?>>>) structureField.get(biome.e()));

				features.add(() -> ConfiguredStructures.TEST);

				structureField.set(biome.e(), features);
			}

		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

}
