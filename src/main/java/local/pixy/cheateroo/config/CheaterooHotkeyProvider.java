package local.pixy.cheateroo.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.input.Hotkey;
import fi.dy.masa.malilib.input.HotkeyCategory;
import fi.dy.masa.malilib.input.HotkeyProvider;
import local.pixy.cheateroo.Reference;

import java.util.List;

public class CheaterooHotkeyProvider implements HotkeyProvider {
	public static final HotkeyProvider INSTANCE = new CheaterooHotkeyProvider();

	@Override
	public List<? extends Hotkey> getAllHotkeys() {
		ImmutableList.Builder<Hotkey> builder = new ImmutableList.Builder<>();

		builder.addAll(FeatureToggle.TOGGLE_HOTKEYS);

		return builder.build();
	}

	@Override
	public List<HotkeyCategory> getHotkeysByCategories() {
		return ImmutableList.of(
				new HotkeyCategory(Reference.MOD_INFO, "cheateroo.hotkeys.category.feature_toggle", FeatureToggle.TOGGLE_HOTKEYS)
		);
	}
}
