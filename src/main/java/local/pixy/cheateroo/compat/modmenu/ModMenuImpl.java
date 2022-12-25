package local.pixy.cheateroo.compat.modmenu;

import com.enderzombie102.modmenu.api.ConfigScreenFactory;
import com.enderzombie102.modmenu.api.ModMenuApi;
import local.pixy.cheateroo.gui.ConfigScreen;

public class ModMenuImpl implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		return (screen) -> ConfigScreen.create();
	}
}
