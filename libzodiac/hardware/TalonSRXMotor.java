package frc.libzodiac.hardware;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.libzodiac.ZMotor;
import frc.libzodiac.Zervo;

public class TalonSRXMotor extends ZMotor {

    public final int can_id;

    protected TalonSRX motor;

    public TalonSRXMotor(int can_id) {
        this.can_id = can_id;
    }

    @Override
    public TalonSRXMotor bind_can() {
        this.motor = new TalonSRX(this.can_id);
        this.motor.configFactoryDefault();
        return this;
    }

    @Override
    public TalonSRXMotor apply_pid() {
        this.motor.config_kP(0, this.pid.k_p);
        this.motor.config_kI(0, this.pid.k_i);
        this.motor.config_kD(0, this.pid.k_d);
        return this;
    }

    @Override
    protected TalonSRXMotor opt_init() {
        return this;
    }

    @Override
    public TalonSRXMotor shutdown() {
        this.motor.set(ControlMode.Disabled, 0);
        return this;
    }

    @Override
    public TalonSRXMotor stop() {
        return this.shutdown();
    }

    @Override
    public TalonSRXMotor go(String profile) {
        this.motor.set(ControlMode.Velocity, this.profile.get(profile));
        return this;
    }

    @Override
    public TalonSRXMotor go(double raw_unit) {
        this.motor.set(ControlMode.Velocity, raw_unit);
        return this;
    }

    public static class Servo extends TalonSRXMotor implements Zervo {

        public double zero = 0;

        public Servo(int can_id) {
            super(can_id);
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
            return this.motor.getSelectedSensorPosition();
        }

        @Override
        public Servo go(String profile) {
            this.motor.set(ControlMode.Position, this.profile.get(profile));
            return this;
        }

        @Override
        public Servo go(double raw_unit) {
            this.motor.set(ControlMode.Position, raw_unit);
            return this;
        }

        @Override
        public TalonSRXMotor motor() {
            return this;
        }

    }
}
