package mc.jeryn.dev.regen.bta;

import mc.jeryn.dev.regen.bta.item.ModItems;
import mc.jeryn.dev.regen.bta.skin.SkinDownloader;
import mc.jeryn.dev.regen.bta.ui.HudManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import java.util.Random;

public class Regeneration implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "regenerated";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Random MASTER_RANDOM = new Random();

	public static RegenConfig regenConfig = new RegenConfig();

    @Override
    public void onInitialize() {
		LOGGER.info("Regenerated initialized.");

	}
	@Override
	public void beforeGameStart() {
		if(regenConfig.isAllowSkinChanging()) {
			SkinDownloader.collectSkinData();
		}
		ModItems.init();
		RegenerationSounds.init();
	}



	@Override
	public void afterGameStart() {
		HudManager.init();
	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
