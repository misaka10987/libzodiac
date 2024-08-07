package frc.libzodiac;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public interface ZmartDash {
    String key();

    default ZmartDash debug(String key, String value) {
        SmartDashboard.putString(this.key() + "->" + key + ": ", value);
        return this;
    }

    default ZmartDash debug(String key, int value) {
        return this.debug(key, "" + value);
    }

    default ZmartDash debug(String key, double value) {
        SmartDashboard.putNumber(this.key() + "->" + key + ": ", value);
        return this;
    }

    default ZmartDash debug(String key, boolean value) {
        SmartDashboard.putBoolean(this.key() + "->" + key + ": ", value);
        return this;
    }
}
