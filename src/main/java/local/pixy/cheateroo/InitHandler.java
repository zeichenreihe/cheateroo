package local.pixy.cheateroo;

import fi.dy.masa.malilib.config.BaseModConfig;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import local.pixy.cheateroo.config.Callbacks;
import local.pixy.cheateroo.config.CheaterooHotkeyProvider;
import local.pixy.cheateroo.config.Configs;
import local.pixy.cheateroo.config.FeatureToggle;
import local.pixy.cheateroo.gui.ConfigScreen;
import local.pixy.cheateroo.gui.widget.FeatureToggleConfigWidget;

public class InitHandler implements InitializationHandler {
	@Override
	public void registerModHandlers() {
		Registry.CONFIG_MANAGER.registerConfigHandler(BaseModConfig.createDefaultModConfig(Reference.MOD_INFO, 1, Configs.CATEGORIES));

		Registry.CONFIG_SCREEN.registerConfigScreenFactory(Reference.MOD_INFO, ConfigScreen::create);
		Registry.CONFIG_TAB.registerConfigTabProvider(Reference.MOD_INFO, ConfigScreen::getConfigTabs);

		Registry.CONFIG_WIDGET.registerConfigWidgetFactory(FeatureToggle.class, FeatureToggleConfigWidget::new);

		Registry.HOTKEY_MANAGER.registerHotkeyProvider(CheaterooHotkeyProvider.INSTANCE);

		Callbacks.init();
	}
}
