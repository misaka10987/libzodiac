package frc.libzodiac.hardware.group;

import frc.libzodiac.Util;
import frc.libzodiac.ZmartDash;
import frc.libzodiac.Zwerve.Module;
import frc.libzodiac.hardware.Falcon;
import frc.libzodiac.util.Vec2D;

public final class FalconSwerve implements Module, ZmartDash {
    public final Falcon speed_motor;
    public final Falcon.Servo angle_motor;

    private double zero_pos = 0;

    public FalconSwerve(Falcon speed_motor, Falcon.Servo angle_motor) {
        this.speed_motor = speed_motor;
        this.angle_motor = angle_motor;
    }

    private static final double SWERVE_RATIO = 150.0 / 7.0;

    @Override
    public FalconSwerve init() {
        this.speed_motor.init();
        this.angle_motor.init();
        this.reset();
        return this;
    }

    @Override
    public FalconSwerve reset() {
        this.zero_pos = this.angle_motor.get() / SWERVE_RATIO;
        return this;
    }

    @Override
    public FalconSwerve go(Vec2D vel) {
        // 轮子转一圈 falcon 转25圈
        if (vel.r() == 0) {
            this.speed_motor.shutdown();
            return this;
        }
        this.debug("go", "" + vel);
        final var curr = this.angle_motor.get() / SWERVE_RATIO - this.zero_pos;
        final var angle = vel.theta();
        final var best = Util.solve(curr, angle);

        final var pos = best.x0;
        final var inverted = best.x1;

        final var speed = inverted ? -vel.r() : vel.r();
        this.angle_motor.debug("curr", curr);
        this.angle_motor.debug("go", pos);
        this.speed_motor.debug("go", speed);
        this.angle_motor.debug("dst", pos * SWERVE_RATIO);
        this.speed_motor.go_v(speed);
        this.angle_motor.go((pos + this.zero_pos) * SWERVE_RATIO);
        return this;
    }

    @Override
    public String key() {
        return "FalconSwerve(" + this.speed_motor.key() + ",...)";
    }
}
