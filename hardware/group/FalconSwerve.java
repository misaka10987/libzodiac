package frc.libzodiac.hardware.group;

import frc.libzodiac.Util;
import frc.libzodiac.ZmartDash;
import frc.libzodiac.Zwerve.Module;
import frc.libzodiac.hardware.Falcon;
import frc.libzodiac.util.Vec2D;

public final class FalconSwerve implements Module, ZmartDash {
    public final Falcon speed_motor;
    public final Falcon.Servo angle_motor;

    public FalconSwerve(Falcon speed_motor, Falcon.Servo angle_motor) {
        this.speed_motor = speed_motor;
        this.angle_motor = angle_motor;
    }

    @Override
    public Module init() {
        this.speed_motor.init();
        this.angle_motor.init();
        return this;
    }

    @Override
    public Module go(Vec2D vel) {
        this.debug("go", "" + vel);
        final var curr = this.angle_motor.get();
        this.debug("angle", curr);
        var angle = Util.mod_pi(vel.theta());
        var speed = vel.r();
        final var delta = angle - curr;
        if (Math.abs(delta) > Math.PI / 2) {
            angle += Math.PI;
            speed = -speed;
        }
        // this.angle_motor.go(angle * 4);
        // this.speed_motor.go(speed);
        this.angle_motor.debug("go", angle * 4);
        this.speed_motor.debug("go", speed);
        return this;
    }

    @Override
    public String key() {
        return "FalconSwerve(" + this.speed_motor.key() + ",...)";
    }
}
