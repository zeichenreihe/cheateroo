package local.pixy.cheateroo.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Shadow
	private boolean realmsNotificationsInitialized;

	@Inject(method = "init()V", at = @At("HEAD"))
	private void cheateroo$init(CallbackInfo ci) {

		// this disables the "Failed to load Realms module" error log
		realmsNotificationsInitialized = true;
	}
}
