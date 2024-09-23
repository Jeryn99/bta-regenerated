package mc.jeryn.dev.regen.bta.mixin;

import mc.jeryn.dev.regen.bta.Regeneration;
import mc.jeryn.dev.regen.bta.item.ModItems;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(
	value = WorldFeatureLabyrinth.class,
	remap = false
)
public class WorldGenLabyrinthMixin {

	@Shadow
	public WeightedRandomBag<WeightedRandomLootObject> chestLoot;

	@Inject(method = "generate",at = @At("TAIL"))
	private void init(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir){
		this.chestLoot.addEntry(new WeightedRandomLootObject(ModItems.fob_watch.getDefaultStack()), Regeneration.regenConfig.getFobWatchWeight());
	}

}
