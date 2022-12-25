package local.pixy.cheateroo.gui;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.gui.BaseScreen;
import fi.dy.masa.malilib.gui.config.BaseConfigScreen;
import fi.dy.masa.malilib.gui.config.BaseConfigTab;
import fi.dy.masa.malilib.gui.config.ConfigTab;
import fi.dy.masa.malilib.gui.tab.ScreenTab;
import fi.dy.masa.malilib.util.data.ModInfo;
import local.pixy.cheateroo.Reference;
import local.pixy.cheateroo.config.Configs;
import local.pixy.cheateroo.config.FeatureToggle;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class ConfigScreen {
	public static final ModInfo MOD_INFO = Reference.MOD_INFO;

	private static final BaseConfigTab GENERIC =
			new BaseConfigTab(MOD_INFO, "generic", 200, Configs.Generic.OPTIONS, ConfigScreen::create);
	private static final BaseConfigTab CHEAT_TOGGLES =
			new BaseConfigTab(MOD_INFO, "cheats", 200, FeatureToggle.VALUES, ConfigScreen::create);

	private static final ImmutableList<ScreenTab> ALL_TABS = ImmutableList.of(
			GENERIC,
			CHEAT_TOGGLES
	);

	private static final ImmutableList<ConfigTab> CONFIG_TABS = ImmutableList.of(
			GENERIC,
			CHEAT_TOGGLES
	);

	public static BaseScreen create() {
		return new BaseConfigScreen(MOD_INFO, ALL_TABS, CHEAT_TOGGLES, "cheateroo.title.screen.configs", Reference.MOD_VERSION);
	}

	public static List<ConfigTab> getConfigTabs() {
		return CONFIG_TABS;
	}
}
