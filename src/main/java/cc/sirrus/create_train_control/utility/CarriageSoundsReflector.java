package cc.sirrus.create_train_control.utility;

import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CarriageSoundsReflector {
    // 目标类及内部类全名
    private static final String CS_CLASS = "com.simibubi.create.content.trains.entity.CarriageSounds";
    private static final String LOOP_CLASS = CS_CLASS + "$LoopingSound";

    // 缓存 Class 对象
    public static final Class<?> carriageSoundsClass;
    public static final Class<?> loopingSoundClass;

    // 缓存 Field 对象
    public static final Field minecartField;
    public static final Field sharedWheelField;
    public static final Field sharedWheelSeatedField;
    public static final Field sharedHonkField;

    // 缓存 Method 对象，用于调用 setPitch, setVolume, setLocation, stopSound
    public static final Method setPitchMethod;
    public static final Method setVolumeMethod;
    public static final Method setLocationMethod;
    public static final Method stopSoundMethod;

    static {
        try {
            // 1. 加载 Class
            carriageSoundsClass = Class.forName(CS_CLASS);                                   // :contentReference[oaicite:0]{index=0}
            loopingSoundClass   = Class.forName(LOOP_CLASS);                                // :contentReference[oaicite:1]{index=1}

            // 2. 获取并打开字段访问
            minecartField           = carriageSoundsClass.getDeclaredField("minecartEsqueSound"); // :contentReference[oaicite:2]{index=2}
            sharedWheelField        = carriageSoundsClass.getDeclaredField("sharedWheelSound");
            sharedWheelSeatedField  = carriageSoundsClass.getDeclaredField("sharedWheelSoundSeated");
            sharedHonkField         = carriageSoundsClass.getDeclaredField("sharedHonkSound");
            for (Field f : new Field[]{ minecartField, sharedWheelField, sharedWheelSeatedField, sharedHonkField }) {
                f.setAccessible(true);                                                      // :contentReference[oaicite:3]{index=3}
            }

            // 3. 获取并打开方法访问
            setPitchMethod    = loopingSoundClass.getDeclaredMethod("setPitch", float.class);   // :contentReference[oaicite:4]{index=4}
            setVolumeMethod   = loopingSoundClass.getDeclaredMethod("setVolume", float.class);
            setLocationMethod = loopingSoundClass.getDeclaredMethod("setLocation", Vec3.class);
            stopSoundMethod   = loopingSoundClass.getDeclaredMethod("stopSound");
            for (Method m : new Method[]{ setPitchMethod, setVolumeMethod, setLocationMethod, stopSoundMethod }) {
                m.setAccessible(true);                                                       // :contentReference[oaicite:5]{index=5}
            }
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError("初始化 CarriageSoundsReflector 失败: " + e);
        }
    }
}
