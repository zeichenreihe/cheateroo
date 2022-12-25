package local.pixy.cheateroo;

import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

public class Cheateroo implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	@Override
	public void onInitialize(){
		Registry.INITIALIZATION_DISPATCHER.registerInitializationHandler(new InitHandler());
	}
}
