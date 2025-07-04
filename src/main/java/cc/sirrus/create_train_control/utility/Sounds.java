package cc.sirrus.create_train_control.utility;

import com.simibubi.create.content.trains.entity.CarriageSounds;
import com.simibubi.create.foundation.mixin.accessor.GuiAccessor;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class Sounds {
    public static LoopingSound playIfMissing(Minecraft mc, LoopingSound loopingSound, SoundEvent sound) {
        return playIfMissing(mc, loopingSound, sound, false);
    }

    public static LoopingSound playIfMissing(Minecraft mc, LoopingSound loopingSound, SoundEvent sound, boolean continuouslyShowSubtitle) {
        if (loopingSound == null) {
            loopingSound = new LoopingSound(sound, SoundSource.NEUTRAL, continuouslyShowSubtitle);
            mc.getSoundManager()
                    .play(loopingSound);
        }
        return loopingSound;
    }

    public static class LoopingSound extends AbstractTickableSoundInstance {
        private static final SubtitleOverlay OVERLAY = ((GuiAccessor) Minecraft.getInstance().gui).create$getSubtitleOverlay();

        private final boolean repeatSubtitle;
        private final WeighedSoundEvents weighedSoundEvents = this.resolve(Minecraft.getInstance().getSoundManager());
        private byte subtitleTimer = 0;

        public LoopingSound(SoundEvent soundEvent, SoundSource source) {
            this(soundEvent, source, false);
        }

        public LoopingSound(SoundEvent soundEvent, SoundSource source, boolean repeatSubtitle) {
            super(soundEvent, source, SoundInstance.createUnseededRandom());
            this.attenuation = Attenuation.LINEAR;
            this.looping = true;
            this.delay = 0;
            this.volume = 0.0001f;
            this.repeatSubtitle = repeatSubtitle;
        }

        @Override
        public void tick() {
            if (repeatSubtitle) {
                subtitleTimer++;

                if (subtitleTimer == 20) {
                    OVERLAY.onPlaySound(this, weighedSoundEvents, sound.getAttenuationDistance());
                    subtitleTimer = 0;
                }
            }
        }

        @Override
        public float getVolume() {
            return volume;
        }

        public void setVolume(float volume) {
            this.volume = volume;
        }

        @Override
        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public void setLocation(Vec3 location) {
            x = location.x;
            y = location.y;
            z = location.z;
        }

        public void stopSound() {
            Minecraft.getInstance()
                    .getSoundManager()
                    .stop(this);
        }
    }
}
