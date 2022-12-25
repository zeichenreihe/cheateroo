package local.pixy.cheateroo.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TraderOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VillagerEntity.class)
public interface VillagerEntityAccessor {
	@Accessor("offers")
	TraderOfferList getOffers$cheateroo();
}
