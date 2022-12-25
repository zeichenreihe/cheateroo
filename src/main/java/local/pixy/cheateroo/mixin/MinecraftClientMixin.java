package local.pixy.cheateroo.mixin;

import de.skyrising.litefabric.runtime.ModResourcePack;
import local.pixy.cheateroo.Reference;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Final
	private List<ResourcePack> resourcePacks;
	@Inject(method = "initializeGame", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, remap = false))
	private void cheateroo$addResourcePack(CallbackInfo ci) {
		Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(Reference.MOD_ID);

		container.ifPresent(modContainer ->
				resourcePacks.add(new ModResourcePack(Reference.MOD_ID, modContainer))
		);
	}
}
