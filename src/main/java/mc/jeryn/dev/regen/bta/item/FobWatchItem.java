package mc.jeryn.dev.regen.bta.item;

import mc.jeryn.dev.regen.bta.access.RegenerationDataAccess;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class FobWatchItem extends Item {


	public FobWatchItem(int id) {
		super(id);
	}

	public FobWatchItem(String name, int id) {
		super(name, id);
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		RegenerationDataAccess playerRegenData = (RegenerationDataAccess) entityplayer;
		playerRegenData.setRegensLeft(12);
		itemstack.stackSize = 0;
		entityplayer.sendMessage("You now " + playerRegenData.getRegensLeft() + " regenerations!");
		return super.onUseItem(itemstack, world, entityplayer);
	}

}
