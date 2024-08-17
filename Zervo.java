package frc.libzodiac;

/**
 * Note: the method Zmotor.go **MUST** be overrided.
 */
public interface Zervo {

    /**
     * Configures the zero position of the motor.
     */
    Zervo set_zero(double zero);

    /**
     * Gets the current position.
     */
    double get();

    /**
     * Convaries the servo motor and returns a generic motor.
     */
    ZMotor motor();
}
