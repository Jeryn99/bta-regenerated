package mc.jeryn.dev.regen.bta.ui;

;

import mc.jeryn.dev.regen.bta.access.RegenerationDataAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.hud.Layout;
import net.minecraft.client.gui.hud.MovableHudComponent;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.gamemode.Gamemode;
import org.lwjgl.opengl.GL11;

public class RegenHudComponent extends MovableHudComponent {

	private int width = 16;
	private int height = 16;

	private final String texture = "assets/regenerated/textures/hud/heart_flame.png";

	public RegenHudComponent(String key, int xSize, int ySize, Layout layout) {
		super(key, xSize, ySize, layout);
	}

	@Override
	public boolean isVisible(Minecraft mc) {
		EntityPlayer player = mc.thePlayer;
		RegenerationDataAccess regenerationDataAccess = (RegenerationDataAccess) player;

		if (player == null)
			return true;

		return regenerationDataAccess.getRegensLeft() > 0;
	}

	private void renderSprite(Minecraft mc, Gui gui, int x, int y) {
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		gui.drawTexturedModalRect(x, y, 0, 0, width, height);
	}

	@Override
	public void render(Minecraft mc, GuiIngame gui, int xSizeScreen, int ySizeScreen, float partialTick) {
		int x = this.getLayout().getComponentX(mc, this, xSizeScreen);
		int y = this.getLayout().getComponentY(mc, this, ySizeScreen);

		renderSprite(mc, gui, x, y);
	}

	@Override
	public void renderPreview(Minecraft mc, Gui gui, Layout layout, int xSizeScreen, int ySizeScreen) {
		int x = layout.getComponentX(mc, this, xSizeScreen);
		int y = layout.getComponentY(mc, this, ySizeScreen);

		renderSprite(mc, gui, x, y);
	}
}
