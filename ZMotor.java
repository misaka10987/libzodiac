package frc.libzodiac;

import frc.libzodiac.util.PIDProfile;

import java.util.HashMap;

/**
 * Defines a large collection of APIs to operate various motors so that motors
 * can be controled under a unified generic way.
 */
public abstract class ZMotor {

    /**
     * The PID configuration of this motor.
     */
    public PIDProfile pid;

    /**
     * Motions profiles pre-defined for future use.
     */
    public HashMap<String, Double> profile = new HashMap<>();

    public boolean inverted = false;

    /**
     * Initialize this motor, e.g. binds to the CAN bus.
     */
    public final ZMotor init() {
        this.bind_can();
        this.apply_pid();
        this.opt_init();
        return this;
    }

    /**
     * Binds the motor to specified CAN-bus id(s).
     */
    protected abstract ZMotor bind_can();

    /**
     * Override this method to set the motor's PID to <code>this.pid</code>.
     */
    protected abstract ZMotor apply_pid();

    protected abstract ZMotor opt_init();

    /**
     * Set PID parameters.
     */
    public ZMotor set_pid(PIDProfile pid) {
        this.pid = pid;
        return this;
    }

    /**
     * Set PID parameters.
     */
    public ZMotor set_pid(double k_p, double k_i, double k_d) {
        return this.set_pid(new PIDProfile(k_p, k_i, k_d));
    }

    /**
     * Stop any output behaviour of this motor.
     */
    public abstract ZMotor shutdown();

    /**
     * Stop the motor.
     * Using brake mode if it is available.
     */
    public abstract ZMotor stop();

    /**
     * Perform actions with the specified motion profile.
     */
    public abstract ZMotor go(String profile);

    /**
     * Set the output.
     *
     * @param rads rad/s for general motors and rad for servos.
     */
    public abstract ZMotor go(double rads);

    public abstract ZMotor raw(double output);
}