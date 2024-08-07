package frc.libzodiac.hardware.group;

import frc.libzodiac.*;
import frc.libzodiac.hardware.Falcon;

/**
 * Falcon's built-in encoder resets its zero position to the position when
 * power-on.
 * Therefore, an <code>ZEncoder</code> is used so that it is able to control the
 * servo with absolute positions.
 */
public class FalconWithEncoder extends ZMotor implements Zervo, ZmartDash {
    public final Falcon.Servo motor;
    public final ZEncoder encoder;

    public FalconWithEncoder(Falcon.Servo motor, ZEncoder encoder) {
        this.motor = motor;
        this.encoder = encoder;
    }

    public final FalconWithEncoder calibrate() {
        var expected = this.encoder.get();
        var actual = this.motor.get();
        // Falcon thinks it is `actual-expected` more than we want. Take that from its
        // zero position.
        this.motor.zero -= actual - expected;
        this.debug("motor.zero", this.motor.zero);
        return this;
    }

    @Override
    protected FalconWithEncoder bind_can() {
        return this;
    }

    @Override
    protected FalconWithEncoder apply_pid() {
        this.motor.set_pid(this.pid);
        return this;
    }

    @Override
    protected FalconWithEncoder opt_init() {
        this.motor.init();
        this.encoder.init();
        this.calibrate();
        return this;
    }

    @Override
    public Falcon shutdown() {
        return this.motor.shutdown();
    }

    @Override
    public Falcon stop() {
        return this.motor.stop();
    }

    @Override
    public Falcon.Servo go(String profile) {
        return this.motor.go(profile);
    }

    @Override
    public Falcon.Servo go(double rads) {
        return this.motor.go(rads);
    }

    @Override
    public double get_zero() {
        return this.encoder.zero;
    }

    @Override
    public FalconWithEncoder set_zero(double zero) {
        this.encoder.zero = zero;
        return this;
    }

    @Override
    public double get() {
        return this.encoder.get();
    }

    @Override
    public Falcon motor() {
        return this.motor;
    }

    @Override
    public String key() {
        return "FalconWithEncoder(" + this.motor.key() + "," + this.encoder.key() + ")";
    }

    public boolean reset(boolean reverse) {
        var curr = this.getPosition() / Constant.SWERVE_MOTOR_WHEEL_RATIO;
        double delta = -Util.mod_pi(curr);
        if (Math.abs(delta) > Math.PI / 2) {
            delta = Util.mod_pi(delta + Math.PI);
            reverse = !reverse;
        }
        double target = curr + delta;
        this.go(target * Constant.SWERVE_MOTOR_WHEEL_RATIO);
        return reverse;
    }

    public double getPosition() {
        // Why not this.zero? Because idk what this.zero actually is. It just doesn't work.
        return this.motor.getPosition().refresh().getValue();
        //return this.encoder.get();
    }

    public Zervo clear() {
        this.motor.setPosition(0);
        this.encoder.clear();
        return this;
    }
}