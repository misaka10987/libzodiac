package frc.libzodiac.hardware;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.StaticBrake;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.libzodiac.Constant;
import frc.libzodiac.ZMotor;
import frc.libzodiac.Zervo;
import frc.libzodiac.ZmartDash;

public class Falcon extends ZMotor implements ZmartDash {

    public final int can_id;

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
        this.motor.setControl(new VelocityDutyCycle(this.profile.get(profile)));
        return this;
    }

    @Override
    public Falcon go(double raw_unit) {
        this.motor.set(raw_unit);
        return this;
    }

    @Override
    public String key() {
        return "Falcon(" + this.can_id + ")";
    }

    public static class Servo extends Falcon implements Zervo {

        /**
         * Zero position applied to adjust output value of Falcon's builtin encoder.
         */
        public double zero = 0;

        public Servo(int can_id) {
            super(can_id);
        }

        @Override
        public Servo go(String profile) {
            this.motor.setControl(new PositionDutyCycle(this.profile.get(profile)));
            return this;
        }

        @Override
        public Servo go(double angle) {
            this.motor.setControl(new PositionDutyCycle(angle));
            return this;
        }

        @Override
        public Zervo set_zero(double zero) {
            this.zero = zero;
            return this;
        }

        @Override
        public double get_zero() {
            return this.zero;
        }

        @Override
        public double get() {
            return this.motor.getPosition().refresh().getValue() * Constant.FALCON_POSITION_UNIT - this.zero;
        }

        @Override
        public Falcon motor() {
            return this;
        }

        @Override
        public String key() {
            return "Falcon.Servo(" + this.can_id + ")";
        }

        public StatusSignal<Double> getPosition() {
            return this.motor.getPosition();
        }

        public Falcon setPosition(int i) {
            this.motor.setPosition(i);
            return this;
        }
    }
}
