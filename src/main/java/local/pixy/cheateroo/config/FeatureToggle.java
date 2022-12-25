package local.pixy.cheateroo.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.option.BooleanConfig;
import fi.dy.masa.malilib.config.option.ConfigInfo;
import fi.dy.masa.malilib.config.option.HotkeyConfig;
import fi.dy.masa.malilib.input.KeyBind;
import fi.dy.masa.malilib.input.KeyBindSettings;
import fi.dy.masa.malilib.input.callback.HotkeyCallback;
import fi.dy.masa.malilib.input.callback.ToggleBooleanWithMessageKeyCallback;
import fi.dy.masa.malilib.overlay.message.MessageHelpers;
import fi.dy.masa.malilib.util.data.ModInfo;
import local.pixy.cheateroo.Reference;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public enum FeatureToggle implements ConfigInfo {
	REDUCE_BEACON_RENDERING("reducedBeaconRendering", false),
	RENDER_SATURATION_BAR("renderSaturationBar", false),
	RENDER_SATURATION_BAR_TEXT("renderSaturationBarText", true),
	RENDER_SATURATION_BAR_REMOVE_WHEN_ZERO("renderSaturationBarRemoveWhenZero", true)
	;

	public static final ImmutableList<FeatureToggle> VALUES = ImmutableList.copyOf(values());
	public static final ImmutableList<HotkeyConfig> TOGGLE_HOTKEYS = ImmutableList.copyOf(
			VALUES.stream().map(FeatureToggle::getHotkeyConfig).collect(Collectors.toList()));
	public static final ImmutableList<BooleanConfig> TOGGLE_CONFIGS = ImmutableList.copyOf(
			VALUES.stream().map(FeatureToggle::getBooleanConfig).collect(Collectors.toList()));


	FeatureToggle(String name, boolean defaultValue) {
		this(name, defaultValue, KeyBindSettings.INGAME_DEFAULT);
	}

	private final BooleanConfig toggleStatus;
	private final HotkeyConfig toggleHotkey;


	/**
	 * @author masa
	 */
	FeatureToggle(String name, boolean defaultValue, KeyBindSettings settings) {
		this.toggleStatus = new BooleanConfig(name, defaultValue);
		this.toggleHotkey = new HotkeyConfig(name, "", settings);

		String nameLower = name;//.toLowerCase(Locale.ROOT);
		String nameKey = "cheateroo.feature_toggle.name." + nameLower;

		this.toggleHotkey.setNameTranslationKey(nameKey);
		this.toggleHotkey.setPrettyNameTranslationKey(nameKey);

		this.toggleStatus.setNameTranslationKey(nameKey);
		this.toggleStatus.setPrettyNameTranslationKey(nameKey);
		this.toggleStatus.setCommentTranslationKey("cheateroo.feature_toggle.comment." + nameLower);

		this.setSpecialToggleMessageFactory(null);
	}

	/**
	 * @author masa
	 * This will replace the default hotkey callback with the ToggleBooleanWithMessageKeyCallback
	 * variant that takes in the message factory
	 */
	public void setSpecialToggleMessageFactory(@Nullable MessageHelpers.BooleanConfigMessageFactory messageFactory)
	{
		HotkeyCallback callback = new ToggleBooleanWithMessageKeyCallback(this.toggleStatus, messageFactory);
		this.toggleHotkey.getKeyBind().setCallback(callback);
	}

	/**
	 * @author masa
	 */
	public void setHotkeyCallback(HotkeyCallback callback)
	{
		this.toggleHotkey.getKeyBind().setCallback(callback);
	}

	/**
	 * @author masa
	 */
	public boolean getBooleanValue()
	{
		return this.toggleStatus.getBooleanValue();
	}

	/**
	 * @author masa
	 */
	public BooleanConfig getBooleanConfig()
	{
		return this.toggleStatus;
	}

	/**
	 * @author masa
	 */
	public HotkeyConfig getHotkeyConfig()
	{
		return this.toggleHotkey;
	}

	/**
	 * @author masa
	 */
	public KeyBind getKeyBind()
	{
		return this.toggleHotkey.getKeyBind();
	}

	/**
	 * @author masa
	 */
	@Override
	public boolean isModified() {
		return this.toggleStatus.isModified() ||
				this.toggleHotkey.isModified();
	}

	/**
	 * @author masa
	 */
	@Override
	public void resetToDefault() {
		this.toggleStatus.resetToDefault();
		this.toggleHotkey.resetToDefault();
	}

	/**
	 * @author masa
	 */
	@Override
	public String getName() {
		return this.toggleStatus.getName();
	}

	/**
	 * @author masa
	 */
	@Override
	public String getDisplayName() {
		return this.toggleStatus.getDisplayName();
	}

	/**
	 * @author masa
	 */
	@Override
	public ModInfo getModInfo() {
		return Reference.MOD_INFO;
	}

	/**
	 * @author masa
	 */
	@Override
	public Optional<String> getComment() {
		return this.toggleStatus.getComment();
	}
}
