package mc.jeryn.dev.regen.bta;

import net.minecraft.core.sound.SoundTypes;
import turniplabs.halplibe.helper.SoundHelper;

public class RegenerationSounds {

	public static void init(){
		SoundTypes.register("regenerated.regen");
		SoundHelper.addSound(Regeneration.MOD_ID, "regen.ogg");
	}

}
