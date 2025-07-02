package cc.sirrus.create_train_control.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import cc.sirrus.create_train_control.Create_train_control;

/**
 * 用 NeoForge 的 DeferredRegister 来管理 SoundEvent
 */
public class ModSoundEvents {

    /** 为 SOUND_EVENT 创建 DeferredRegister */
    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(Registries.SOUND_EVENT, Create_train_control.MODID);

    // motor
    public static final DeferredHolder<SoundEvent, SoundEvent> MOTOR_1 = register("motor_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOTOR_2 = register("motor_2");

    // vvvf 系列
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_1  = register("vvvf_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_2  = register("vvvf_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_3  = register("vvvf_3");
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_4  = register("vvvf_4");
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_5  = register("vvvf_5");
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_6  = register("vvvf_6");
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_7  = register("vvvf_7");
    public static final DeferredHolder<SoundEvent, SoundEvent> VVVF_8  = register("vvvf_8");

private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        // 静态工厂 tryBuild
        ResourceLocation id = ResourceLocation.tryBuild(Create_train_control.MODID, name);
        if (id == null) throw new IllegalArgumentException("Invalid sounds name: " + name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }


    /** 在主类构造里调用：把所有 SOUNDS 注册进 modEventBus */
    public static void registerAll(IEventBus modBus) {
        SOUNDS.register(modBus);
    }
}
