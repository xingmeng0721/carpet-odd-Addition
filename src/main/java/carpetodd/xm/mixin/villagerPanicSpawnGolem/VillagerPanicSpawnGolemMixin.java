package carpetodd.xm.mixin.villagerPanicSpawnGolem;

import carpetodd.xm.CarpetOddSettings;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagerPanicSpawnGolemMixin {

    // 恐慌状态下跳过"近期睡眠"检查（原版数量要求保持不变）
    @Inject(method = "golemSpawnConditionsMet", at = @At("HEAD"), cancellable = true)
    private void carpetOdd$panicSpawnGolemWithoutSleep(long gameTime, CallbackInfoReturnable<Boolean> cir) {
        if (!CarpetOddSettings.villagerPanicSpawnGolem) return;
        Villager self = (Villager) (Object) this;
        if (self.getBrain().isActive(Activity.PANIC)) {
            cir.setReturnValue(true);
        }
    }
}
