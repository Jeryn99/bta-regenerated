package mc.jeryn.dev.regen.bta;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.util.Locale;

import static mc.jeryn.dev.regen.bta.Regeneration.MOD_ID;

public class RegenConfig {

	private static final TomlConfigHandler config;

	static {
		Toml toml = new Toml(MOD_ID.toUpperCase(Locale.ROOT));

		toml.addCategory("Skin Changing")
			.addEntry("allowSkinChanging", false);

		toml.addCategory("Item Ids")
			.addEntry("fobWatchID", 20007);

		toml.addCategory("Loot")
			.addEntry("fobWatchWeight", 10);

		config = new TomlConfigHandler(null, MOD_ID, toml);
	}

	private final boolean allowSkinChanging;
	private final int fobWatchID;
	private final int fobWatchWeight;


	public boolean isAllowSkinChanging() {
		return allowSkinChanging;
	}

	public int getFobWatchID() {
		return fobWatchID;
	}

	public int getFobWatchWeight() {
		return fobWatchWeight;
	}

	public RegenConfig() {
		allowSkinChanging = config.getBoolean("Skin Changing.allowSkinChanging");
		fobWatchID = config.getInt("Item Ids.fobWatchID");
		fobWatchWeight = config.getInt("Loot.fobWatchWeight");
	}


}
