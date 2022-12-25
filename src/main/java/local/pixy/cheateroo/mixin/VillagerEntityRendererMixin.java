package local.pixy.cheateroo.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TraderOfferList;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;

@Mixin(VillagerEntityRenderer.class)
public abstract class VillagerEntityRendererMixin extends MobEntityRenderer<VillagerEntity> {
	public VillagerEntityRendererMixin(EntityRenderDispatcher arg,
									   EntityModel arg2, float f) {
		super(arg, arg2, f);
	}

	@Override
	public void render(VillagerEntity entity, double x, double y, double z, float g, float h) {
		super.render(entity, x, y, z, g, h);

		if (false) { // TODO: continue writing the villager rendering thingy, add option for it
			this.renderVillagerOffers(entity, x, y, z, g, h, 64);
		}
	}


	private void renderVillagerOffers(VillagerEntity entity, double x, double y, double z, double g_, double h_, int maxDistance) {
		if (entity.squaredDistanceTo(this.dispatcher.field_11098) > maxDistance * maxDistance)
			return;

		TraderOfferList offers = ((VillagerEntityAccessor) entity).getOffers$cheateroo();

		if (offers != null) {
			ArrayList<String> lines = new ArrayList<>();

			for (TradeOffer offer: offers) {
				ItemStack firstStack = offer.getFirstStack();
				ItemStack secondStack = offer.getSecondStack();
				ItemStack resultStack = offer.getResult();

				StringBuilder builder = new StringBuilder();

				int firstCount = firstStack.getCount();
				if (firstCount != 1) {
					builder.append(firstCount).append("x ");
				}

				builder.append(firstStack.getItem().getDisplayName(firstStack));

				if (secondStack.getItem() != Items.AIR) {
					builder.append(" + ");
					int secondCount = secondStack.getCount();
					if (secondCount != 1) {
						builder.append(secondCount).append("x ");
					}
					builder.append(secondStack.getItem().getDisplayName(secondStack));
				}
				builder.append(" -> ");

				int resultCount = resultStack.getCount();
				if (resultCount != 1) {
					builder.append(resultCount).append("x ");
				}

				builder.append(resultStack.getItem().getDisplayName(resultStack));

				lines.add(builder.toString());
			}

			double deltaY = 0;

			for (String line: lines) {
				TextRenderer textRenderer = this.getFontRenderer();
				double g = y + entity.height + 0.5f + deltaY;
				int i = 0;
				boolean bl = this.dispatcher.field_2104.perspective == 2;
				double scaling = 0.025;
				{
					int stringWidth = textRenderer.getStringWidth(line);
					GlStateManager.pushMatrix();

					GlStateManager.translated(x, g, z);
					GlStateManager.method_12272(0.0f, 1.0f, 0.0f);

					// top is snapless, bottom snaps to 45° but 22.5° offset
					GlStateManager.rotatef(-this.dispatcher.yaw, 0.0F, 1.0F, 0.0F);
					//GlStateManager.rotatef(- 45 * (int) ((this.dispatcher.yaw + 22.5)/ 45), 0.0F, 1.0F, 0.0F);

					//GlStateManager.rotatef((bl ? -1 : 1) * this.dispatcher.pitch, 1.0f, 0.0f, 0.0f);

					GlStateManager.scaled(-scaling, -scaling, scaling);
					GlStateManager.disableLighting();
					GlStateManager.depthMask(false);
					GlStateManager.disableDepthTest();

					GlStateManager.enableBlend();
					GlStateManager.method_12288(
							GlStateManager.class_2870.SRC_ALPHA,
							GlStateManager.class_2866.ONE_MINUS_SRC_ALPHA,
							GlStateManager.class_2870.ONE,
							GlStateManager.class_2866.ZERO
					);
					int halfWidth = stringWidth / 2;
					GlStateManager.disableTexture();


					// draw the background box
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buf = tessellator.getBuffer();
					buf.begin(7, VertexFormats.POSITION_COLOR);
					buf.vertex(-halfWidth - 1, -1 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).next();
					buf.vertex(-halfWidth - 1, 8 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).next();
					buf.vertex(halfWidth + 1, 8 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).next();
					buf.vertex(halfWidth + 1, -1 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).next();
					tessellator.draw();


					GlStateManager.enableTexture();
					//if (!bl2) {
						textRenderer.draw(line, -halfWidth, i, 553648127);
						GlStateManager.enableDepthTest();
					//}

					GlStateManager.depthMask(true);
					textRenderer.draw(line, -halfWidth, i, -1);
					GlStateManager.enableLighting();
					GlStateManager.disableBlend();
					GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

					GlStateManager.popMatrix();
				}

				deltaY += scaling * textRenderer.fontHeight;
			}
		}
	}
}
