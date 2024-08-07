package frc.libzodiac.hardware;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.libzodiac.Constant;
import frc.libzodiac.ZEncoder;

public class TalonSRXEncoder extends ZEncoder {

    protected TalonSRX encoder;

    public TalonSRXEncoder(int can_id) {
        super(can_id);
    }

    @Override
    public ZEncoder init() {
        this.encoder = new TalonSRX(this.can_id);
        this.set_zero();
        return this;
    }

    @Override
    public double get_raw() {
        return this.encoder.getSelectedSensorPosition();
    }

    @Override
    public double get_rad() {
        return this.get_raw() / Constant.TALONSRX_ENCODER_UNIT;
    }

    @Override
    public ZEncoder clear() {
        this.encoder.setSelectedSensorPosition(0);
        this.zero = 0;
        return this;
    }
}
