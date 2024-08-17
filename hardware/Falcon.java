package frc.libzodiac.hardware;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.StaticBrake;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.libzodiac.ZMotor;
import frc.libzodiac.Zervo;
import frc.libzodiac.ZmartDash;

public class Falcon extends ZMotor implements ZmartDash {
    public static final double VELOCITY_RAW_UNIT = 2 * Math.PI;

    public final int can_id;

    public boolean inverted = false;

    protected TalonFX motor;

    public Falcon(int can_id) {
        this.can_id = can_id;
    }

    @Override
    protected Falcon bind_can() {
        this.motor = new TalonFX(this.can_id);
        return this;
    }

    @Override
    public Falcon apply_pid() {
        this.motor
                .getConfigurator()
                .apply(new Slot0Configs()
                        .withKP(this.pid.k_p)
                        .withKI(this.pid.k_i)
                        .withKD(this.pid.k_d));
        return this;
    }

    @Override
    protected Falcon opt_init() {
        return this;
    }

    @Override
    public Falcon shutdown() {
        this.motor.stopMotor();
        return this;
    }

    @Override
    public Falcon stop() {
        this.motor.setControl(new StaticBrake());
        return this;
    }

    @Override
    public Falcon go(String profile) {
        final var v = this.profile.get(profile);
        return this.go(v);
    }

    @Override
    public Falcon go(double rad_s) {
        final var vel = this.inverted ? -rad_s : rad_s;
        final var v = new VelocityDutyCycle(vel / Falcon.VELOCITY_RAW_UNIT);
        this.motor.setControl(v);
        return this;
    }

    public Falcon set(double x) {
        this.motor.set(x);
        return this;
    }

    @Override
    public String key() {
        return "Falcon(" + this.can_id + ")";
    }

    public Falcon invert(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public Falcon invert() {
        return this.invert(true);
    }

    public Falcon go_v(double rad_s) {
        final var vel = this.inverted ? -rad_s : rad_s;
        final var v = new VelocityVoltage(vel / Falcon.VELOCITY_RAW_UNIT);
        this.motor.setControl(v);
        return this;
    }

    public static final class Servo extends Falcon implements Zervo {

        public static final double POSITION_RAW_UNIT = 2 * Math.PI;

        // private double zero = 0;

        public Servo(int can_id) {
            super(can_id);
        }

        @Override
        public Servo go(String profile) {
            final var v = this.profile.get(profile);
            return this.go(v);
        }

        @Override
        public Servo go(double rad) {
            final var v = new PositionDutyCycle((this.inverted ? -rad : rad) / Servo.POSITION_RAW_UNIT);
            this.motor.setControl(v);
            return this;
        }

        @Override
        public Servo set_zero(double zero) {
            // this.zero = zero;
            final var v = zero / POSITION_RAW_UNIT;
            this.motor.setPosition(this.inverted ? -v : v);
            return this;
        }

        @Override
        public double get_zero() {
            // return this.zero;
            return 0;
        }

        @Override
        public double get() {
            final var v = this.motor.getPosition().refresh().getValue() * POSITION_RAW_UNIT;
            return this.inverted ? -v : v;
        }

        @Override
        public Falcon motor() {
            return this;
        }

        @Override
        public Servo invert(boolean inverted) {
            this.inverted = inverted;
            return this;
        }

        @Override
        public Servo invert() {
            return this.invert(true);
        }

        @Override
        public String key() {
            return "Falcon.Servo(" + this.can_id + ")";
        }
    }
}
