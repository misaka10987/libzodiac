package frc.libzodiac.hardware.group;

import frc.libzodiac.Util;
import frc.libzodiac.ZmartDash;
import frc.libzodiac.Zwerve.Module;
import frc.libzodiac.hardware.Falcon;
import frc.libzodiac.hardware.MagEncoder;
import frc.libzodiac.util.Vec2D;

public final class FalconSwerve implements Module, ZmartDash {
    public final Falcon speed_motor;
    public final Falcon.Servo angle_motor;

    private MagEncoder encoder;

    public FalconSwerve(Falcon speed_motor, Falcon.Servo angle_motor, MagEncoder encoder) {
        this.speed_motor = speed_motor;
        this.angle_motor = angle_motor;
        this.encoder = encoder;
    }

    private static final double SWERVE_RATIO = 150.0 / 7.0;

    @Override
    public FalconSwerve init() {
        this.speed_motor.init();
        this.angle_motor.init();
        this.encoder.init();
        this.debug("encoder_zero", this.encoder.zero);
        return this;
    }

    @Override
    public FalconSwerve reset() {
        final var curr = this.encoder.get();
        final var target = curr * SWERVE_RATIO;
        this.angle_motor.set_zero(target);
        return this;
    }

    @Override
    public FalconSwerve go(Vec2D vel) {
        if (vel.r() == 0) {
            this.speed_motor.shutdown();
            this.angle_motor.shutdown();
            return this;
        }
        final var curr = this.angle_motor.get() / SWERVE_RATIO;
        final var angle = vel.theta();
        final var best = Util.solve(curr, angle);

        final var pos = best.x0;
        final var inverted = best.x1;

        final var speed = inverted ? -vel.r() : vel.r();
        this.speed_motor.go_v(speed);
        this.angle_motor.go(pos * SWERVE_RATIO);
        return this;
    }

    @Override
    public FalconSwerve shutdown() {
        this.angle_motor.shutdown();
        this.speed_motor.shutdown();
        return this;
    }

    @Override
    public String key() {
        return "FalconSwerve(" + this.speed_motor.key() + ",...)";
    }
}
