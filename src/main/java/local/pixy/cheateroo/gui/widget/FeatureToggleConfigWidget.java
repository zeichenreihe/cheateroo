package local.pixy.cheateroo.gui.widget;

import fi.dy.masa.malilib.gui.config.ConfigWidgetContext;
import fi.dy.masa.malilib.gui.widget.list.entry.DataListEntryWidgetData;
import fi.dy.masa.malilib.gui.widget.list.entry.config.BaseHotkeyedBooleanConfigWidget;
import local.pixy.cheateroo.config.FeatureToggle;

public class FeatureToggleConfigWidget extends BaseHotkeyedBooleanConfigWidget<FeatureToggle> {
	public FeatureToggleConfigWidget(FeatureToggle config,
									 DataListEntryWidgetData constructData,
									 ConfigWidgetContext ctx) {
		super(config, config.getBooleanConfig(), config.getKeyBind(), constructData, ctx);
	}
}
