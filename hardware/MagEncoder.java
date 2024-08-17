package frc.libzodiac.hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.libzodiac.ZmartDash;

public final class MagEncoder implements ZmartDash {
    public static final double POSITION_RAW_UNIT = 2 * Math.PI / 4096;

    public final int can_id;
    public double zero = 0;
    private TalonSRX encoder;

    public MagEncoder(int can_id) {
        this.can_id = can_id;
    }

    public MagEncoder init() {
        this.encoder = new TalonSRX(this.can_id);
        this.encoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        return this;
    }

    public MagEncoder set_zero(double zero) {
        this.zero = zero;
        this.debug("get", this.get());
        return this;
    }

    public double get() {
        return (this.encoder.getSelectedSensorPosition() - this.zero) * POSITION_RAW_UNIT;
    }

    @Override
    public String key() {
        return "MagEncoder(" + this.can_id + ")";
    }
}
