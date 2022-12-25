package local.pixy.cheateroo.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.category.BaseConfigOptionCategory;
import fi.dy.masa.malilib.config.category.ConfigOptionCategory;
import fi.dy.masa.malilib.config.option.ConfigOption;
import fi.dy.masa.malilib.config.option.StringConfig;
import local.pixy.cheateroo.Reference;

import java.util.List;

public class Configs {
	public static final List<ConfigOptionCategory> CATEGORIES = ImmutableList.of(
			BaseConfigOptionCategory.normal(Reference.MOD_INFO, "Generic", Configs.Generic.OPTIONS),
			BaseConfigOptionCategory.normal(Reference.MOD_INFO, "CheatToggles", FeatureToggle.TOGGLE_CONFIGS),
			BaseConfigOptionCategory.normal(Reference.MOD_INFO, "CheatHotkeys", FeatureToggle.TOGGLE_HOTKEYS)
	);

	public static class Generic {
		//public static final StringConfig CUSTOM_TEST = new StringConfig("barbarbar", "foo");

		public static final ImmutableList<ConfigOption<?>> OPTIONS = ImmutableList.of(
				//CUSTOM_TEST
		);
	}
}
