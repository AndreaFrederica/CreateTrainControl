package cc.sirrus.create_train_control.mixin;

import cc.sirrus.create_train_control.utility.CarriageSoundsReflector;
import cc.sirrus.create_train_control.utility.MovingState;
import cc.sirrus.create_train_control.utility.SoundInvokerUtils;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageSounds;
import cc.sirrus.create_train_control.utility.Sounds.LoopingSound;
import com.simibubi.create.content.trains.entity.Carriage.DimensionalCarriageEntity;
import cc.sirrus.create_train_control.init.ModSoundEvents;
import com.simibubi.create.content.trains.entity.Train;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static cc.sirrus.create_train_control.utility.Sounds.playIfMissing;

@Mixin(value = CarriageSounds.class, remap = false)
public class CarriageSoundsMixin {
    @Shadow(remap = false)
	LerpedFloat distanceFactor;
    @Shadow(remap = false)
	LerpedFloat speedFactor;
    @Shadow(remap = false)
	LerpedFloat approachFactor;
    @Shadow(remap = false)
	LerpedFloat seatCrossfade;
    @Shadow(remap = false)
    CarriageContraptionEntity entity;
    @Shadow(remap = false)
    	int tick;
    @Shadow(remap = false)
	int prevSharedTick;
	// 新增声音字段
    @Unique private LoopingSound vvvf1Sound;
    @Unique private LoopingSound vvvf2Sound;
    @Unique private LoopingSound vvvf3Sound;
    @Unique private LoopingSound vvvf4Sound;
    @Unique private LoopingSound vvvf5Sound;
    @Unique private LoopingSound vvvf6Sound;
    @Unique private LoopingSound vvvf7Sound;
    @Unique private LoopingSound vvvf8Sound;
    @Unique private LoopingSound motor1Sound;
    @Unique private LoopingSound motor2Sound;
    @Unique private float[] vvvfVolumes;
    @Unique private float speed;
    @Unique private float speedLerped;
    @Unique private float speedLerpedLast;
    @Unique private float accelAvg;
    @Unique private MovingState state;
//    @Shadow(remap = false)
//    Object minecartEsqueSound;
//    @Shadow(remap = false)
//    Object sharedHonkSound;
//	@Shadow(remap = false)
//	Object sharedWheelSoundSeated;
//	@Shadow(remap = false)
//	Object sharedWheelSound;

	public void finalizeSharedVolume(float volume) {
        float crossfade = seatCrossfade.getValue();

        Object minecartEsqueSound = null;
        Object sharedWheelSoundSeated = null;
        Object sharedWheelSound = null;
        try {
            Object self = this;

            // 1. 读取四个 LoopingSound 实例
            minecartEsqueSound = CarriageSoundsReflector.minecartField.get(self);
            sharedWheelSound = CarriageSoundsReflector.sharedWheelField.get(self);
            sharedWheelSoundSeated = CarriageSoundsReflector.sharedWheelSeatedField.get(self);
            Object sharedHonkSound = CarriageSoundsReflector.sharedHonkField.get(self);

            // 2. 计算音量
            float v0 = (1 - crossfade * .65f) * volume / 2;
            float v1 = Math.min(volume, Math.max((speedFactor.getValue() - .25f) / 4 + .01f, 0));

        //((LoopsoundInvoker) minecartEsqueSound).invokeSetVolume((1 - crossfade * .65f) * volume / 2);
		SoundInvokerUtils.safeInvokeSetVolume(minecartEsqueSound, ((1 - crossfade * .65f) * volume / 2));
        volume = Math.min(volume, Math.max((speedFactor.getValue() - .25f) / 4 + 0.01f, 0));
        //((LoopsoundInvoker) sharedWheelSoundSeated).invokeSetVolume(volume * crossfade);
		SoundInvokerUtils.safeInvokeSetVolume(sharedWheelSoundSeated, (volume * crossfade));
        //((LoopsoundInvoker) sharedWheelSound).invokeSetVolume(volume * (1 - crossfade) * 1.5f);
		SoundInvokerUtils.safeInvokeSetVolume(sharedWheelSound,(volume * (1 - crossfade) * 1.5f));
        } catch (ReflectiveOperationException e) {
            Create.LOGGER.error("反射执行 finalizeSharedVolume 失败", e);
        }
    }

    // 构造后初始化
    @Inject(method = "<init>", at = @At("RETURN"),remap = false)
    public void onCtor(CarriageContraptionEntity entity, CallbackInfo ci) {
        vvvfVolumes = new float[8];
        state = MovingState.Stopped;
        speedLerpedLast = 0;
        accelAvg = 0;
    }

    // 在 tick 方法尾部注入新逻辑
//    @Inject(method = "tick", at = @At("TAIL"))
//    private void afterTick(DimensionalCarriageEntity dce, CallbackInfo ci) {
//        Minecraft mc = Minecraft.getInstance();
//        // 初始化循环声音
//        motor1Sound = playIfMissing(mc, motor1Sound, ModSoundEvents.MOTOR_1.get());
//        motor2Sound = playIfMissing(mc, motor2Sound, ModSoundEvents.MOTOR_2.get());
//        vvvf1Sound  = playIfMissing(mc, vvvf1Sound, ModSoundEvents.VVVF_1.get());
//        vvvf2Sound  = playIfMissing(mc, vvvf2Sound, ModSoundEvents.VVVF_2.get());
//        vvvf3Sound  = playIfMissing(mc, vvvf3Sound, ModSoundEvents.VVVF_3.get());
//        vvvf4Sound  = playIfMissing(mc, vvvf4Sound, ModSoundEvents.VVVF_4.get());
//        vvvf5Sound  = playIfMissing(mc, vvvf5Sound, ModSoundEvents.VVVF_5.get());
//        vvvf6Sound  = playIfMissing(mc, vvvf6Sound, ModSoundEvents.VVVF_6.get());
//        vvvf7Sound  = playIfMissing(mc, vvvf7Sound, ModSoundEvents.VVVF_7.get());
//        vvvf8Sound  = playIfMissing(mc, vvvf8Sound, ModSoundEvents.VVVF_8.get());
//
//        // 计算基础音量
//        float baseVol = Math.min(
//            distanceFactor.getValue() / 100,
//            approachFactor.getValue() / 300 + .0125f
//        );
//
//        // 获取速度因子
//        speedLerped = speedFactor.getValue();
//        float acc = speedLerped - speedLerpedLast;
//        accelAvg = (accelAvg * 0.5f) + (acc * 0.5f);
//        speedLerpedLast = speedLerped;
//        float tacc = entity.getCarriage().train.acceleration();
//        // 简化的加速状态机示例…（可依据原逻辑填充）
//
//        float sf = speedLerped * 20f * 3.6f;
//        if (sf > 0.1f) {
//            sf *= 1.2f;
//            float atten = Math.min(1.0f, Math.max(sf / 50f * -1f + 1.8f, 0.1f));
//
//            // Motor 音量与音高
//            float motorVol = baseVol * Math.min(1.0f, Math.max((sf - 8f) / 8f, 0f)) * atten * 0.8f;
//            if (sf < 32) {
//                motor1Sound.setVolume(motorVol);
//                motor2Sound.setVolume(0);
//                motor1Sound.setPitch(sf / 16f);
//            } else {
//                motor1Sound.setVolume(0);
//                motor2Sound.setVolume(motorVol);
//                motor2Sound.setPitch(sf / 64f);
//            }
//
//            // VVVF 音量分段
//            float vvvfVol = baseVol * Math.min(1.0f, sf / 6f) * atten * 0.7f;
//            for (int i = 0; i < vvvfVolumes.length; i++) vvvfVolumes[i] = 0;
//            int idx = Math.min(7, (int)(sf / 10));
//            vvvfVolumes[idx] = vvvfVol;
//
//            // 应用到各 VVVF 声音
//            vvvf1Sound.setVolume(vvvfVolumes[0]);
//            vvvf2Sound.setVolume(vvvfVolumes[1]);
//            vvvf3Sound.setVolume(vvvfVolumes[2]);
//            vvvf4Sound.setVolume(vvvfVolumes[3]);
//            vvvf5Sound.setVolume(vvvfVolumes[4]);
//            vvvf6Sound.setVolume(vvvfVolumes[5]);
//            vvvf7Sound.setVolume(vvvfVolumes[6]);
//            vvvf8Sound.setVolume(vvvfVolumes[7]);
//        } else {
//            motor1Sound.setVolume(0);
//            motor2Sound.setVolume(0);
//            for (LoopingSound vvvf : new LoopingSound[]{vvvf1Sound,vvvf2Sound,vvvf3Sound,vvvf4Sound,vvvf5Sound,vvvf6Sound,vvvf7Sound,vvvf8Sound}) {
//                vvvf.setVolume(0);
//            }
//        }
//    }

       @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lcom/simibubi/create/content/trains/entity/CarriageSounds;playIfMissing(Lnet/minecraft/client/Minecraft;Lcom/simibubi/create/content/trains/entity/CarriageSounds$LoopingSound;Lnet/minecraft/sounds/SoundEvent;)Lcom/simibubi/create/content/trains/entity/CarriageSounds$LoopingSound;",
            ordinal = 2
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
			   remap = false
    )
    private void afterSharedWheelSoundSeated(
        DimensionalCarriageEntity dce,
        CallbackInfo ci
    ) {
		try {
            // 1. 获取当前 Mixin 实例（即 CarriageSounds 对象）
            Object self = this;

            // 2. 读取四个 LoopingSound 实例
            Object minecartEsqueSound          = CarriageSoundsReflector.minecartField.get(self);
            Object sharedWheelSound       = CarriageSoundsReflector.sharedWheelField.get(self);
            Object sharedWheelSoundSeated = CarriageSoundsReflector.sharedWheelSeatedField.get(self);
            Object sharedHonkSound        = CarriageSoundsReflector.sharedHonkField.get(self);

			Minecraft mc = Minecraft.getInstance();
        motor1Sound = playIfMissing(mc, motor1Sound, ModSoundEvents.MOTOR_1.get(),true);
        motor2Sound = playIfMissing(mc, motor2Sound, ModSoundEvents.MOTOR_2.get(),true);
        vvvf1Sound  = playIfMissing(mc, vvvf1Sound, ModSoundEvents.VVVF_1.get(),true);
        vvvf2Sound  = playIfMissing(mc, vvvf2Sound, ModSoundEvents.VVVF_2.get(),true);
        vvvf3Sound  = playIfMissing(mc, vvvf3Sound, ModSoundEvents.VVVF_3.get(),true);
        vvvf4Sound  = playIfMissing(mc, vvvf4Sound, ModSoundEvents.VVVF_4.get(),true);
        vvvf5Sound  = playIfMissing(mc, vvvf5Sound, ModSoundEvents.VVVF_5.get(),true);
        vvvf6Sound  = playIfMissing(mc, vvvf6Sound, ModSoundEvents.VVVF_6.get(),true);
        vvvf7Sound  = playIfMissing(mc, vvvf7Sound, ModSoundEvents.VVVF_7.get(),true);
        vvvf8Sound  = playIfMissing(mc, vvvf8Sound, ModSoundEvents.VVVF_8.get(),true);
Entity camEntity = mc.cameraEntity;
        Vec3 cam = camEntity.getEyePosition();
        Vec3 leadingAnchor = dce.leadingAnchor();
		Vec3 trailingAnchor = dce.trailingAnchor();
        Vec3 toBogey1 = leadingAnchor.subtract(cam);
		Vec3 toBogey2 = trailingAnchor.subtract(cam);
		double distance1 = toBogey1.length();
		double distance2 = toBogey2.length();
        Vec3 toCarriage = distance1 > distance2 ? toBogey2 : toBogey1;
        Vec3 soundLocation = cam.add(toCarriage);
		//float volume = Math.min(Math.min(speedFactor.getValue(), distanceFactor.getValue() / 100),
		//	approachFactor.getValue() / 300 + .0125f);
		// VVVF sound is not attenuated by speed factor
		float volume = Math.min(distanceFactor.getValue() / 100, approachFactor.getValue() / 300 + .0125f);

		// if (entity.carriageIndex == 0) {
		// 	float v = volume * (1 - seatCrossfade.getValue() * .35f) * .75f;
		// 	if ((3 + tick) % 4 == 0)
		// 		AllSoundEvents.STEAM.playAt(entity.level(), soundLocation, v * ((tick + 7) % 8 == 0 ? 0.75f : .45f),
		// 			1.17f, false);
		// 	if (tick % 16 == 0)
		// 		AllSoundEvents.STEAM.playAt(entity.level(), soundLocation, v * 1.5f, .8f, false);
		// }

		// if (!arrived && speedFactor.getValue() < .002f && train.accumulatedSteamRelease > 1) {
		// 	arrived = true;
		// 	float releaseVolume = train.accumulatedSteamRelease / 10f;
		// 	entity.level().playLocalSound(soundLocation.x, soundLocation.y, soundLocation.z, SoundEvents.LAVA_EXTINGUISH,
		// 		SoundSource.NEUTRAL, .25f * releaseVolume, .78f, false);
		// 	entity.level().playLocalSound(soundLocation.x, soundLocation.y, soundLocation.z,
		// 		SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.NEUTRAL, .2f * releaseVolume, 1.5f, false);
		// 	AllSoundEvents.STEAM.playAt(entity.level(), soundLocation, .75f * releaseVolume, .5f, false);
		// }

		float pitchModifier = ((entity.getId() * 10) % 13) / 36f;

		volume = Math.min(volume, distanceFactor.getValue() / 800);

		float pitch = Mth.clamp(speedFactor.getValue() * 2 + .25f, .75f, 1.95f) - pitchModifier;
		float pitch2 = Mth.clamp(speedFactor.getValue() * 2, 0.75f, 1.25f) - pitchModifier;
        Train train = entity.getCarriage().train;
           //((LoopsoundInvoker)minecartEsqueSound).invokeSetPitch(pitch * 1.5f);
		   SoundInvokerUtils.safeInvokeSetPitch(((LoopsoundInvoker)minecartEsqueSound), pitch * 1.5f);
		speedLerped = speedFactor.getValue();
		float acc = speedLerped - speedLerpedLast;
		accelAvg = accelAvg * 0.5f + acc * 0.5f;
		speedLerpedLast = speedLerped;
		float tacc = train.acceleration();
		if (Math.abs(accelAvg) < (tacc / 3)) {
			if ((speedLerped < 0.01) && (state != MovingState.Stopped)) {
				speed = 0;
				state = MovingState.Stopped;
			}
			else if (state == MovingState.Accelerating) {
				speed = speedLerped; // This is the final speed
				state = MovingState.Cruising;
			}
		}
		else {
			if (state != MovingState.Accelerating) {
				speed = speedLerped;
				state = MovingState.Accelerating;
			}
			else {
				if (accelAvg < 0)
					speed -= tacc;
				else
					speed += tacc;
				if (speed < 0) {
					speed = 0;
					state = MovingState.Stopped;
				}
			}
		}

		if (tick % 32 == 0) {
			Create.LOGGER.info("[CARRIAGE] Current speed {}, accel avg {}, tacc {}, state {}", speed, accelAvg, tacc, state);
		}

		float sf = speed * 20.f * 3.6f; // speed in km/h
		if (sf > 0.1f) {
			sf = sf * 1.2f; // convert from km/h to driving Hz
			// Attenuate volume at low speed and high speed
			float attenFactor = Math.min(1.0f, Math.max(sf / 50.0f * -1.0f + 1.8f, 0.1f));
			float motorVolume = volume * Math.min(1.0f, Math.max((sf - 8.0f) / 8.0f, 0.0f)) * attenFactor * 0.8f;
			// Select speed region
			if (sf < 32) {
				motor1Sound.setVolume(motorVolume);
				motor2Sound.setVolume(0.0f);
				motor1Sound.setPitch(sf / 16.0f);
			}
			else {
				motor1Sound.setVolume(0.0f);
				motor2Sound.setVolume(motorVolume);
				motor2Sound.setPitch(sf / 64.0f);
			}

			float vvvfVolume = volume * Math.min(1.0f, sf / 6.0f) * attenFactor * 0.7f;
			for (int i = 0; i < vvvfVolumes.length; i++) {
				vvvfVolumes[i] = 0.f;
			}
			if (sf < 11) {
				// Async 200Hz
				vvvfVolumes[0] = vvvfVolume;
			}
			else if (sf < 27) {
				// Sync 21
				vvvfVolumes[1] = vvvfVolume;
				vvvf2Sound.setPitch(sf / 20.0f);
			}
			else if (sf < 36) {
				// Sync 15
				vvvfVolumes[2] = vvvfVolume;
				vvvf3Sound.setPitch(sf / 30.0f);
			}
			else if (sf < 52) {
				// SHE 11
				vvvfVolumes[3] = vvvfVolume;
				vvvf4Sound.setPitch(sf / 46.0f);
			}
			else if (sf < 57) {
				// SHE 7
				vvvfVolumes[4] = vvvfVolume;
				vvvf5Sound.setPitch(sf / 54.0f);
			}
			else if (sf < 64) {
				// SHE 5
				vvvfVolumes[5] = vvvfVolume;
				vvvf6Sound.setPitch(sf / 60.0f);
			}
			else if (sf < 70) {
				// SHE 3
				vvvfVolumes[6] = vvvfVolume;
				vvvf7Sound.setPitch(sf / 67.0f);
			}
			else {
				// Sync 1
				vvvfVolumes[7] = vvvfVolume;
				vvvf8Sound.setPitch(sf / 80.0f);
			}

			vvvf1Sound.setVolume(vvvfVolumes[0]);
			vvvf2Sound.setVolume(vvvfVolumes[1]);
			vvvf3Sound.setVolume(vvvfVolumes[2]);
			vvvf4Sound.setVolume(vvvfVolumes[3]);
			vvvf5Sound.setVolume(vvvfVolumes[4]);
			vvvf6Sound.setVolume(vvvfVolumes[5]);
			vvvf7Sound.setVolume(vvvfVolumes[6]);
			vvvf8Sound.setVolume(vvvfVolumes[7]);
		}
		else {
			motor1Sound.setVolume(0);
			motor2Sound.setVolume(0);
			vvvf1Sound.setVolume(0);
			vvvf2Sound.setVolume(0);
			vvvf3Sound.setVolume(0);
			vvvf4Sound.setVolume(0);
			vvvf5Sound.setVolume(0);
			vvvf6Sound.setVolume(0);
			vvvf7Sound.setVolume(0);
			vvvf8Sound.setVolume(0);
		}


		volume = Math.min(volume, distanceFactor.getValue() / 1000);

		for (Carriage carriage : train.carriages) {
			DimensionalCarriageEntity mainDCE = carriage.getDimensionalIfPresent(entity.level().dimension());
			if (mainDCE == null)
				continue;
			CarriageContraptionEntity mainEntity = mainDCE.entity.get();
			if (mainEntity == null)
				continue;
			if (mainEntity.sounds == null)
				mainEntity.sounds = new CarriageSounds(mainEntity);
			mainEntity.sounds.submitSharedSoundVolume(soundLocation, volume);
			if (carriage != entity.getCarriage()) {
				finalizeSharedVolume(0);
				return;
			}
			break;
		}

//		finalizeSharedVolume(volume);
//		minecartEsqueSound.setLocation(soundLocation);
		//((LoopsoundInvoker)sharedWheelSound).invokeSetPitch(pitch2);
		SoundInvokerUtils.safeInvokeSetPitch(sharedWheelSound, pitch2);
//		sharedWheelSound.setLocation(soundLocation);
		//((LoopsoundInvoker)sharedWheelSoundSeated).invokeSetPitch(pitch2);
		SoundInvokerUtils.safeInvokeSetPitch(sharedWheelSoundSeated,pitch2);
//		sharedWheelSoundSeated.setLocation(soundLocation);

		if (train.honkTicks == 0) {
			if (sharedHonkSound != null) {
				//((LoopsoundInvoker)sharedHonkSound).invokeStopSound();
				SoundInvokerUtils.safeInvokeStopSound(sharedHonkSound);
				sharedHonkSound = null;
			}
			return;
		}

		train.honkTicks--;
		train.determineHonk(entity.level());

		if (train.lowHonk == null)
			return;

		boolean low = train.lowHonk;
		float honkPitch = (float) Math.pow(2, train.honkPitch / 12.0);

		AllSoundEvents.SoundEntry endSound =
			!low ? AllSoundEvents.WHISTLE_TRAIN_MANUAL_END : AllSoundEvents.WHISTLE_TRAIN_MANUAL_LOW_END;
		AllSoundEvents.SoundEntry continuousSound =
			!low ? AllSoundEvents.WHISTLE_TRAIN_MANUAL : AllSoundEvents.WHISTLE_TRAIN_MANUAL_LOW;

		if (train.honkTicks == 5)
			endSound.playAt(mc.level, soundLocation, 1, honkPitch, false);
		if (train.honkTicks == 19)
			endSound.playAt(mc.level, soundLocation, .5f, honkPitch, false);

		sharedHonkSound = playIfMissing(mc, (LoopingSound) sharedHonkSound, continuousSound.getMainEvent());
		//((LoopsoundInvoker)sharedHonkSound).invokeSetLocation(soundLocation);
		SoundInvokerUtils.safeInvokeSetLocation(sharedHonkSound,soundLocation);
		float fadeout = Mth.clamp((3 - train.honkTicks) / 3f, 0, 1);
		float fadein = Mth.clamp((train.honkTicks - 17) / 3f, 0, 1);
		//((LoopsoundInvoker)sharedHonkSound).invokeSetVolume(1 - fadeout - fadein);
		SoundInvokerUtils.safeInvokeSetVolume(sharedHonkSound,1 - fadeout - fadein);
		//((LoopsoundInvoker)sharedHonkSound).invokeSetPitch(honkPitch);
		SoundInvokerUtils.safeInvokeSetPitch(sharedHonkSound,honkPitch);

        } catch (ReflectiveOperationException e) {
            Create.LOGGER.error("反射调用 LoopingSound 失败", e);
        }



    }

    @Inject(method = "stop",at = @At("TAIL"),remap = false)
    public void afterStop(CallbackInfo ci){
        if (vvvf1Sound != null)
			vvvf1Sound.stopSound();
		if (vvvf2Sound != null)
			vvvf2Sound.stopSound();
		if (vvvf3Sound != null)
			vvvf3Sound.stopSound();
		if (vvvf4Sound != null)
			vvvf4Sound.stopSound();
		if (vvvf5Sound != null)
			vvvf5Sound.stopSound();
		if (vvvf6Sound != null)
			vvvf6Sound.stopSound();
		if (vvvf7Sound != null)
			vvvf7Sound.stopSound();
		if (vvvf8Sound != null)
			vvvf8Sound.stopSound();
		if (motor1Sound != null)
			motor1Sound.stopSound();
		if (motor2Sound != null)
			motor2Sound.stopSound();
    }
}
