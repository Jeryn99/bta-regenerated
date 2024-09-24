package mc.jeryn.dev.regen.bta.access;

import net.minecraft.core.entity.player.EntityPlayer;

public interface ModelPlayerAccess {


	public void setLivingEntity(EntityPlayer player);

	public EntityPlayer getLivingEntity();
}
