package mc.jeryn.dev.regen.bta.mixin;

import mc.jeryn.dev.regen.bta.access.ModelPlayerAccess;
import mc.jeryn.dev.regen.bta.access.RegenerationDataAccess;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.model.ModelPlayer;
import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mc.jeryn.dev.regen.bta.Regeneration.MASTER_RANDOM;
import static net.minecraft.client.render.model.ModelPlayer.func_178685_a;

@Mixin({ModelPlayer.class, ModelBiped.class})
public class ModelBipedMixin implements ModelPlayerAccess {

	private EntityPlayer player;


	@Override
	public void setLivingEntity(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public EntityPlayer getLivingEntity() {
		return player;
	}

	private final ModelBiped thisAs = (ModelBiped) ((Object) this);


	@Inject(method = "setRotationAngles(FFFFFF)V", at = @At("HEAD"), cancellable = true, remap = false)
	public void setRotationAngles(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci) {

		RegenerationDataAccess playerRegenData = (RegenerationDataAccess) getLivingEntity();

		if (playerRegenData != null && playerRegenData.getRegenerationTicksElapsed() > 0) {
			double armShake = MASTER_RANDOM.nextDouble();
			long currentTicks = playerRegenData.getRegenerationTicksElapsed();
			float armRotY = (float) currentTicks * 1.5F;
			float armRotZ = (float) currentTicks * 1.5F;
			float headRot = (float) currentTicks * 1.5F;

			if (armRotY > 95) {
				armRotY = 95;
			}

			if (armRotZ > 95) {
				armRotZ = 95;
			}

			if (headRot > 45) {
				headRot = 45;
			}


			// ARMS
			thisAs.bipedLeftArm.rotateAngleY = 0;
			thisAs.bipedRightArm.rotateAngleY = 0;

			thisAs.bipedLeftArm.rotateAngleX = 0;
			thisAs.bipedRightArm.rotateAngleX = 0;

			thisAs.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRotZ + armShake);
			thisAs.bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRotZ + armShake);
			thisAs.bipedLeftArm.rotateAngleY = (float) -Math.toRadians(armRotY);
			thisAs.bipedRightArm.rotateAngleY = (float) Math.toRadians(armRotY);

			// BODY
			thisAs.bipedBody.rotateAngleX = 0;
			thisAs.bipedBody.rotateAngleY = 0;
			thisAs.bipedBody.rotateAngleZ = 0;

			// LEGS
			thisAs.bipedLeftLeg.rotateAngleY = 0;
			thisAs.bipedRightLeg.rotateAngleY = 0;

			thisAs.bipedLeftLeg.rotateAngleX = 0;
			thisAs.bipedRightLeg.rotateAngleX = 0;

			thisAs.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
			thisAs.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

			thisAs.bipedHead.rotateAngleX = (float) Math.toRadians(-headRot);
			thisAs.bipedHead.rotateAngleY = (float) Math.toRadians(0);
			thisAs.bipedHead.rotateAngleZ = (float) Math.toRadians(0);

			copyAll();
			ci.cancel();
		} else {
			// Resets the legs, since Minecraft doesn't actually do this on its own
			thisAs.bipedLeftLeg.rotateAngleZ = 0;
			thisAs.bipedRightLeg.rotateAngleZ = 0;
		}
	}


	private void copyAll() {
		if(thisAs instanceof ModelPlayer) {
			ModelPlayer modelPlayer = (ModelPlayer) thisAs;
			func_178685_a(thisAs.bipedLeftLeg, modelPlayer.bipedLeftLegOverlay);
			func_178685_a(thisAs.bipedRightLeg, modelPlayer.bipedRightLegOverlay);
			func_178685_a(thisAs.bipedLeftArm, modelPlayer.bipedLeftArmOverlay);
			func_178685_a(thisAs.bipedRightArm, modelPlayer.bipedRightArmOverlay);
			func_178685_a(thisAs.bipedBody, modelPlayer.bipedBodyOverlay);
		}
		func_178685_a(thisAs.bipedHead, thisAs.bipedHeadOverlay);
	}


}
