package local.pixy.cheateroo.mixin;

import local.pixy.cheateroo.config.FeatureToggle;
import net.minecraft.block.material.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
	@Final
	@Shadow
	private MinecraftClient client;

	@Inject(method = "renderStatusBars(Lnet/minecraft/client/util/Window;)V", at = @At("TAIL"))
	private void cheateroo$renderStatusBar(Window window, CallbackInfo ci) {
		if (!FeatureToggle.RENDER_SATURATION_BAR.getBooleanValue())
			return;

		if (this.client.getCameraEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) this.client.getCameraEntity();

			// get the saturation
			float saturation = player.getHungerManager().getSaturationLevel();
			int saturationLevel = (int) saturation;

			if (FeatureToggle.RENDER_SATURATION_BAR_REMOVE_WHEN_ZERO.getBooleanValue()) {
				if (saturationLevel == 0) {
					return;
				}
			}

			// where to render it
			int y = window.getHeight() - 39 - 10;
			int xBase = window.getWidth() / 2 + 91 - 9;

			if (FeatureToggle.RENDER_SATURATION_BAR_TEXT.getBooleanValue()) {
				String text = String.format("%.02f", saturation);

				client.textRenderer.draw(text, xBase + 9 + 3, y, 0xFFFFFF);

				// rendering texts requires to load the previous texture again
				this.client.getTextureManager().bindTexture(GUI_ICONS_TEXTURE);
			}

			// position and size on the texture
			int uBaseOuter = 16;
			int uBaseInner = 36;
			int vBase = 27;
			int width = 9;
			int height = 9;

			// make room for the "air" display
			if (player.isSubmergedIn(Material.WATER)) {
				y -= 9;
			}

			// for each of them
			for (int i = 0; i < 10; i++) {
				int innerUPlus = 16;
				int outerUPlus = 0;

				/* // for now, we'll ignore the hunger effect
				if (player.hasStatusEffect(StatusEffects.HUNGER)) {
					innerUPlus = 16 + 36;
					outerUPlus = 13 * 9;
				}*/

				int x = xBase - i * 8;

				// the base one
				drawTextureInvertedU(x, y, uBaseOuter + outerUPlus, vBase, width, height);

				boolean fullOne = 2 * i + 1 < saturationLevel;
				boolean halfOne = 2 * i + 1 == saturationLevel;

				if (fullOne || halfOne) {
					drawTextureInvertedU(x, y, uBaseInner + innerUPlus + (fullOne ? 0 : 9), vBase, width, height);
				}
			}
		}
	}
	private static void drawTextureInvertedU(int x, int y, int u, int v, int width, int height) {
		int imageSizeU = 256;
		int imageSizeV = 256;
		int deltaU = -width;
		drawTexture(x, y, u + width, v, deltaU, height, width, height, imageSizeU, imageSizeV);
	}
}
