package mc.jeryn.dev.regen.bta.item;

import mc.jeryn.dev.regen.bta.RegenConfig;
import mc.jeryn.dev.regen.bta.Regeneration;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

public final class ModItems {
	private ModItems() {} // Makes it impossible to accidentally call 'new ModItems()'

	public static Item fob_watch;


	public static void init() {
		fob_watch = new ItemBuilder(Regeneration.MOD_ID)
			.setIcon("regenerated:item/fob_watch")
			.setStackSize(1)
			.build(new FobWatchItem("fob_watch", Regeneration.regenConfig.getFobWatchID()));

	}
}
