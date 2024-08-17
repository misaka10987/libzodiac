package frc.libzodiac;

import java.util.ArrayDeque;

import edu.wpi.first.wpilibj.Timer;
import frc.libzodiac.ui.Axis;
import frc.libzodiac.util.Vec2D;

/**
 * A highly implemented class for hopefully all types of swerve control.
 */
public abstract class Zwerve extends Zubsystem implements ZmartDash {

    public final Vec2D shape;
    /**
     * Gyro.
     */
    public final Zensor gyro;
    /**
     * Swerve modules of a rectangular chassis.
     * <p>
     * Suppose the robot heads the positive x direction,
     * relationship between indice and positions of modules are as follows:
     * <table>
     * <thead>
     * <tr>
     * <th>Galaxy</th>
     * <th>Index</th>
     * </tr>
     * </thead><tbody>
     * <tr>
     * <td>I</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>II</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>III</td>
     * <td>2</td>
     * </tr>
     * <tr>
     * <td>IV</td>
     * <td>3</td>
     * </tr>
     * </tbody>
     * </table>
     */
    public final Module[] module;
    public boolean headless = false;
    /**
     * Modifier timed at the output speed of the chassis.
     */
    public double output = 1;

    /**
     * Creates a new Zwerve.
     *
     * @param modules See <code>Zwerve.module</code>.
     * @param gyro    The gyro.
     * @param shape   Shape of the robot, <code>x</code> for length and
     *                <code>y</code> for width.
     */
    public Zwerve(Module[] modules, Zensor gyro, Vec2D shape) {
        this.module = modules;
        this.gyro = gyro;
        this.shape = shape;
        final var v = new Vec2D(0, 0);
        this.prev.add(v);
        this.prev.add(v);
        this.prev.add(v);
        this.prev.add(v);
    }

    /**
     * Method to calculate the radius of the rectangular robot.
     */
    private double radius() {
        return this.shape.div(2).r();
    }

    /**
     * Get the absolute current direction of the robot.j
     */
    public double dir_curr() {
        return this.gyro.get("yaw");
    }

    /**
     * Get the direction adjustment applied under headless mode.
     */
    private double dir_fix() {
        if (this.gyro == null)
            return 0;
        return this.headless ? -this.dir_curr() : 0;
    }

    /**
     * Initialize all modules.
     */
    public Zwerve init() {
        for (Module i : this.module) {
            i.init();
        }
        this.opt_init();
        this.desired_yaw = this.gyro.get("yaw");
        return this;
    }

    /**
     * Optional initializations you would like to automatically invoke.
     */
    protected abstract Zwerve opt_init();

    private ArrayDeque<Vec2D> prev = new ArrayDeque<>();

    private double desired_yaw = 0;

    private static double POS_FIX_KP = 1;

    private Timer last_rot = new Timer();

    /**
     * Kinematics part rewritten using vector calculations.
     * 
     * @param vel translational velocity, with +x as the head of the bot
     * @param rot rotal velocity, CCW positive
     */
    public Zwerve go(Vec2D vel, double rot) {

        final var curr_yaw = this.gyro.get("yaw");
        if (rot != 0) {
            this.last_rot.reset();
            this.desired_yaw = curr_yaw;
        } else if (!Util.approx(this.desired_yaw, curr_yaw, 0.1) && this.last_rot.get() >= 1) {
            final var error = this.desired_yaw - curr_yaw;
            this.debug("yaw_error", error);
            rot += error * POS_FIX_KP;
        }

        this.prev.add(vel);
        var sum = new Vec2D(0, 0);
        for (final var i : this.prev)
            sum = sum.add(i);
        final var vt = sum.div(this.prev.size());
        this.prev.pop();
        this.debug("translation", "" + vt);
        this.debug("rotation", rot);
        final var l = this.shape.x / 2;
        final var w = this.shape.y / 2;
        Vec2D[] v = {
                new Vec2D(l, w),
                new Vec2D(-l, w),
                new Vec2D(-l, -w),
                new Vec2D(l, -w),
        };
        for (var i = 0; i < 4; i++)
            v[i] = v[i].rot(Math.PI / 2).with_r(rot).add(vt);
        for (int i = 0; i < 4; i++)
            this.module[i].go(v[i].mul(this.output));
        return this;
    }

    @Override
    public Zwerve update() {
        this.debug("headless", this.headless);
        this.debug("dir_fix", this.dir_fix());
        this.debug("yaw", this.gyro.get("yaw"));
        return this;
    }

    /**
     * Enable/disable headless mode.
     * 
     * @param status whether to enable headless mode
     */
    public Zwerve headless(boolean status) {
        this.headless = status;
        return this;
    }

    /**
     * Enable headless mode.
     */
    public Zwerve headless() {
        return this.headless(true);
    }

    public Zwerve toggle_headless() {
        this.headless = !this.headless;
        return this;
    }

    public ZCommand drive_forward() {
        return new Zambda(this, () -> this.go(new Vec2D(0.1, 0), 0));
    }

    public ZCommand drive(Axis x, Axis y, Axis rot) {
        return new Zambda(this, () -> {
            final var vel = new Vec2D(x.get(), y.get());
            this.go(vel, rot.get());
        });
    }

    @Override
    public String key() {
        return "Zwerve";
    }

    public Zwerve mod_reset() {
        for (final var i : this.module)
            i.reset();
        return this;
    }

    /**
     * Defines one swerve module.
     */
    public interface Module {

        Module init();

        Module go(Vec2D velocity);

        Module reset();

        Module shutdown();
    }
}
