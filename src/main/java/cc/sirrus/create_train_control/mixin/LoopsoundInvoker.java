package cc.sirrus.create_train_control.mixin;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "com.simibubi.create.content.trains.entity.CarriageSounds$LoopingSound",remap = false)
public interface LoopsoundInvoker {
    /**
     * 调用目标类的私有 setPitch(float) 方法
     */
    @Invoker("setPitch")
    void invokeSetPitch(float pitch);

    @Invoker("setLocation")
    void invokeSetLocation(Vec3 location);

    @Invoker("setVolume")
    void invokeSetVolume(float volume);

    @Invoker("stopSound")
    void invokeStopSound();
}
