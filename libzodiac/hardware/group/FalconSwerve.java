package frc.libzodiac.hardware.group;

import frc.libzodiac.Constant;
import frc.libzodiac.Util;
import frc.libzodiac.ZmartDash;
import frc.libzodiac.Zwerve.Module;
import frc.libzodiac.hardware.Falcon;
import frc.libzodiac.util.Vec2D;

public final class FalconSwerve implements Module, ZmartDash {
    public final Falcon speed_motor;
    public final FalconWithEncoder angle_motor;

    public final Vec2D speed;

    // Control the direction of speed motor.
    private boolean speed_inverted = false;

    public FalconSwerve(Falcon speed_motor, FalconWithEncoder angle_motor, Vec2D speed) {
        this.speed_motor = speed_motor;
        this.angle_motor = angle_motor;
        this.speed = speed;
    }

    @Override
    public Module init() {
        this.speed_motor.init();
        this.angle_motor.init();
        return this;
    }

    @Override
    public Module go(Vec2D vel) {
        final var v = vel.mul(this.speed);
        this.debug("go", "" + v);
        var angle = v.theta();
        var speed = v.r();
        if (this.speed_inverted) {
            angle = Util.mod_pi(angle + Math.PI);
            speed = -speed;
        }
        final var curr = this.angle_motor.getPosition() / Constant.SWERVE_MOTOR_WHEEL_RATIO;
        var delta = Util.mod_pi(angle - curr);
        if (Math.abs(delta) > Math.PI / 2) {
            angle = Util.mod_pi(angle + Math.PI);
            delta = Util.mod_pi(angle - curr);
            this.speed_inverted = !this.speed_inverted;
            speed = -speed;
        }
        this.debug("curr", curr);
        this.debug("delta", delta);
        final var target = curr + delta;
        this.debug("target", target);
        this.angle_motor.go(target * Constant.SWERVE_MOTOR_WHEEL_RATIO);
        this.speed_motor.go(speed);
        return this;
    }

    @Override
    public Module reset() {
        this.speed_inverted = this.angle_motor.reset(this.speed_inverted);
        return this;
    }

    @Override
    public Module clear() {
        this.angle_motor.clear();
        return this;
    }

    @Override
    public String key() {
        return "FalconSwerve(" + this.speed_motor.key() + ",...)";
    }
}
