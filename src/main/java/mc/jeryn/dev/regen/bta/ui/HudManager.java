package mc.jeryn.dev.regen.bta.ui;

import mc.jeryn.dev.regen.bta.Regeneration;
import net.minecraft.client.gui.hud.ComponentAnchor;
import net.minecraft.client.gui.hud.HudComponent;
import net.minecraft.client.gui.hud.HudComponents;
import net.minecraft.client.gui.hud.SnapLayout;

public class HudManager {

	/*private static final HudComponent REGEN_HUD = HudComponents.register(new RegenHudComponent(
		"regen_hud",
		182, 7,

		new SnapLayout(

			// Parent component
			HudComponents.HOTBAR,

			// Parent anchor
			ComponentAnchor.TOP_LEFT,

			// Anchor
			ComponentAnchor.BOTTOM_LEFT

		)));*/

	public static void init() {
		Regeneration.LOGGER.debug("Registering HUD components");
	}
}
