package local.pixy.cheateroo.mixin;

import fi.dy.masa.malilib.gui.BaseScreen;
import local.pixy.cheateroo.config.FeatureToggle;
import local.pixy.cheateroo.gui.ConfigScreen;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BeaconBlock.class)
abstract class BeaconBlockMixin extends BlockWithEntity {
	protected BeaconBlockMixin(Material arg) {
		super(arg);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(CallbackInfo ci) {
		super.setItemGroup(ItemGroup.BREWING);
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	public void use(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction direction, float f, float g, float h, CallbackInfoReturnable<Boolean> cir) {
		if (world.isClient) {
			//MaLiLibConfigScreen.open();
			BaseScreen.openScreen(ConfigScreen.create());
		} else {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isFullBoundsCubeForCulling", at = @At("HEAD"), cancellable = true)
	public void isFullBoundsCubeForCulling(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(FeatureToggle.REDUCE_BEACON_RENDERING.getBooleanValue());
	}

	@Inject(method = "getRenderLayerType", at = @At("HEAD"), cancellable = true)
	public void getRenderLayerType(CallbackInfoReturnable<RenderLayer> cir) {
		if (FeatureToggle.REDUCE_BEACON_RENDERING.getBooleanValue())
			cir.setReturnValue(RenderLayer.SOLID);
	}
}