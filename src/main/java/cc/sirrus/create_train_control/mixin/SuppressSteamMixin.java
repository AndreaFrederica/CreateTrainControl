package cc.sirrus.create_train_control.mixin;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.trains.entity.CarriageSounds;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.world.level.Level;

@Mixin(value = CarriageSounds.class, remap = false)
public abstract class SuppressSteamMixin {

    /**
     * 将原版对 AllSoundEvents.STEAM.playAt(...) 的调用
     * 全部重定向到一个空实现，从而彻底不播放蒸汽声音。
     */
    @Redirect(
        method = "tick(Lcom/simibubi/create/content/trains/entity/Carriage$DimensionalCarriageEntity;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/AllSoundEvents$SoundEntry;playAt"
        )
    )
    private void onPlaySteam(
        AllSoundEvents.SoundEntry self,
        Level world,
        Vec3 pos,
        float volume,
        float pitch,
        boolean continuous
    ) {
        // 不调用原方法：什么也不做
    }
}
