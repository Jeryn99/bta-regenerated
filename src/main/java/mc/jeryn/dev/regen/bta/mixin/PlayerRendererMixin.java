package mc.jeryn.dev.regen.bta.mixin;

import mc.jeryn.dev.regen.bta.Regeneration;
import mc.jeryn.dev.regen.bta.access.ModelPlayerAccess;
import mc.jeryn.dev.regen.bta.access.RegenerationDataAccess;
import mc.jeryn.dev.regen.bta.client.RenderRegenerationLayer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.model.ModelPlayer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.GetSkinUrlThread;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

	@Shadow
	@Final
	private ModelPlayer modelThick;

	@Shadow
	@Final
	private ModelPlayer modelSlim;

	@Shadow
	public abstract void loadEntityTexture(EntityPlayer entity);

	@Shadow
	@Final
	private ModelBiped modelArmor;
	@Shadow
	private ModelBiped modelBipedMain;
	@Shadow
	@Final
	private ModelBiped modelArmorChestplate;
	GetSkinUrlThread THREAD;


	@Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/EntityPlayer;F)V", at = @At("TAIL"), cancellable = true, remap = false)
	public void render(EntityPlayer entity, float partialTick, CallbackInfo ci) {
		RegenerationDataAccess playerRegenData = (RegenerationDataAccess) entity;

		updateModelReference(entity);

		if(Regeneration.regenConfig.isAllowSkinChanging()) {
			if (!playerRegenData.getSkin().isEmpty()) {
				if (playerRegenData.getRegenerationTicksElapsed() == 101 || entity.tickCount == 20) {
					entity.skinURL = playerRegenData.getSkin();
					loadEntityTexture(entity);
					entity.slimModel = playerRegenData.isSlim();
				}
			} else {
				if (playerRegenData.getRegenerationTicksElapsed() == 101 || entity.tickCount == 20) {
					if (THREAD == null) {
						THREAD = new GetSkinUrlThread(entity);
					}
					THREAD.start();
					loadEntityTexture(entity);
				}
			}
		}



		RenderRegenerationLayer.renderSpecials(entity.slimModel ? this.modelSlim : this.modelThick, entity, partialTick);
	}

	private void updateModelReference(EntityPlayer entity) {
		ModelPlayerAccess modelPlayerAccessSlim = (ModelPlayerAccess) modelSlim;
		modelPlayerAccessSlim.setLivingEntity(entity);

		ModelPlayerAccess modelPlayerAccessThick = (ModelPlayerAccess) modelThick;
		modelPlayerAccessThick.setLivingEntity(entity);

		ModelPlayerAccess modelArmorAccess = (ModelPlayerAccess) modelArmor;
		modelArmorAccess.setLivingEntity(entity);

		ModelPlayerAccess modelBipedMainAccess = (ModelPlayerAccess) modelBipedMain;
		modelBipedMainAccess.setLivingEntity(entity);

		ModelPlayerAccess modelArmorChestplateAccess = (ModelPlayerAccess) modelArmorChestplate;
		modelArmorChestplateAccess.setLivingEntity(entity);
	}


}
