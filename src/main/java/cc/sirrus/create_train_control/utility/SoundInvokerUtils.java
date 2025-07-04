package cc.sirrus.create_train_control.utility;

import cc.sirrus.create_train_control.mixin.LoopsoundInvoker;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.phys.Vec3;

/**
 * 提供对 LoopingSound 实例的安全调用方法，避免 NPE 和反射异常。
 */
public class SoundInvokerUtils {

    /** 安全调用 setPitch(float) **/
    public static void safeInvokeSetPitch(Object soundInstance, float pitch) {
        if (soundInstance == null) return;  // Null check to avoid NPE :contentReference[oaicite:0]{index=0}
        try {
            ((LoopsoundInvoker) soundInstance).invokeSetPitch(pitch);
        } catch (Exception e) {
            // Log or ignore
            e.printStackTrace();            // Reflection invocation pattern :contentReference[oaicite:1]{index=1}
        }
    }

    /** 安全调用 setVolume(float) **/
    public static void safeInvokeSetVolume(Object soundInstance, float volume) {
        if (soundInstance == null) return;  // Prevent calling on null :contentReference[oaicite:2]{index=2}
        try {
            ((LoopsoundInvoker) soundInstance).invokeSetVolume(volume);
        } catch (Exception e) {
            e.printStackTrace();            // Reflective method invocation :contentReference[oaicite:3]{index=3}
        }
    }

    /** 安全调用 setLocation(Vec3) **/
    public static void safeInvokeSetLocation(Object soundInstance, Vec3 location) {
        if (soundInstance == null || location == null) return;  // Null-safety for arguments :contentReference[oaicite:4]{index=4}
        try {
            ((LoopsoundInvoker) soundInstance).invokeSetLocation(location);
        } catch (Exception e) {
            e.printStackTrace();            // Standard reflection error handling :contentReference[oaicite:5]{index=5}
        }
    }

    /** 安全调用 stopSound() **/
    public static void safeInvokeStopSound(Object soundInstance) {
        if (soundInstance == null) return;  // Null check to avoid NPE :contentReference[oaicite:6]{index=6}
        try {
            ((LoopsoundInvoker) soundInstance).invokeStopSound();
        } catch (Exception e) {
            e.printStackTrace();            // Reflective stop invocation :contentReference[oaicite:7]{index=7}
        }
    }
}
