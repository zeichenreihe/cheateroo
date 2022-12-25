package local.pixy.cheateroo.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.BuiltChunk;
import net.minecraft.util.math.BlockPos;

public class Callbacks {
	public static void init() {/*
		FeatureToggle.REDUCE_BEACON_RENDERING.getBooleanConfig().setValueChangeCallback((newValue, oldValue) -> {
			WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;

			for(WorldRenderer.ChunkInfo lv14 : renderer.visibleChunks) {
				BuiltChunk lv15 = lv14.field_10830;
				if (lv15.method_10173() || set2.contains(lv15)) {
					this.scheduleTerrainUpdate = true;
					BlockPos lv16 = lv15.getPos().add(8, 8, 8);
					boolean bl5 = lv16.getSquaredDistance(lv2) < 768.0;
					if (!lv15.method_12431() && !bl5) {
						this.chunksToRebuild.add(lv15);
					} else {
						this.client.profiler.push("build near");
						this.chunkBuilder.method_10131(lv15);
						lv15.method_12430();
						this.client.profiler.pop();
					}
				}
			}
		});
	*/}
}
