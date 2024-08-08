package frc.libzodiac;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.libzodiac.util.Vec2D;

/**
 * A highly implemented class for hopefully all types of swerve control.
 */
public abstract class Zwerve extends SubsystemBase implements ZmartDash {

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
     * @param shape   Shape of the robot, <code>x</code> for length and <code>y</code> for width.
     */
    public Zwerve(Module[] modules, Zensor gyro, Vec2D shape) {
        this.module = modules;
        this.gyro = gyro;
        this.shape = shape;
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
        return this.opt_init();
    }

    /**
     * Optional initializations you would like to automatically invoke.
     */
    protected abstract Zwerve opt_init();

    /**
     * Kinematics part from 6941.
     */
    @Deprecated
    public Zwerve go_previous(Vec2D velocity, double omega) {
        var x = velocity.x;
        var y = velocity.y;
        var l = this.shape.x;
        var w = this.shape.y;
        var r = this.radius();
        var a = y - omega * l / r;
        var b = y + omega * l / r;
        var c = x - omega * w / r;
        var d = x + omega * w / r;
        var v1 = new Vec2D.Polar(Math.hypot(b, c), Math.atan2(b, c));
        var v2 = new Vec2D.Polar(Math.hypot(b, d), Math.atan2(b, d));
        var v3 = new Vec2D.Polar(Math.hypot(a, d), Math.atan2(a, d));
        var v4 = new Vec2D.Polar(Math.hypot(a, c), Math.atan2(a, c));
        var max = Math.max(Math.max(v1.r, v2.r), Math.max(v3.r, v4.r));
        if (max > 1) {
            v1 = v1.div(max);
            v2 = v2.div(max);
            v3 = v3.div(max);
            v4 = v4.div(max);
        }
        this.module[0].go(v2.into());
        this.module[1].go(v3.into());
        this.module[2].go(v4.into());
        this.module[3].go(v1.into());
        return this;
    }

    /**
     * Kinematics part rewritten using vector calculations.
     */
    public Zwerve go(Vec2D velocity, double omega) {
        final var l = this.shape.x / 2;
        final var w = this.shape.y / 2;
        final var vt = omega;
        final var vel = velocity.rot(this.dir_fix());
        Vec2D[] v = {
                new Vec2D(-l, -w).with_r(vt).add(vel),
                new Vec2D(l, -w).with_r(vt).add(vel),
                new Vec2D(l, w).with_r(vt).add(vel),
                new Vec2D(-l, w).with_r(vt).add(vel),
        };
        final var max = v[0].max(v[1]).max(v[2]).max(v[3]).r();
        if (max > 1) for (int i = 0; i < 4; i++) v[i] = v[i].div(max);
        this.module[0].go(v[0].mul(output));
        this.module[1].go(v[1].mul(output));
        this.module[2].go(v[2].mul(output));
        this.module[3].go(v[3].mul(output));
        return this;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        this.debug("headless", this.headless);
        this.debug("dir_fix", this.dir_fix());
        this.debug("yaw", this.gyro.get("yaw"));
    }

    /**
     * Enable/disable headless mode.
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
        return new Zambda<>((x) -> x.go(new Vec2D(0.1, 0), 0), this);
    }

    public ZCommand drive(Zoystick zoystick) {
        return new Zambda<>((x) -> {
            final var vel = new Vec2D(-zoystick.ly(), -zoystick.lx());
            this.debug("vel", vel + "");
            this.debug("rot", zoystick.rx());
            x.go(vel, zoystick.rx());
        }, this);
    }

    public ZCommand check_headless(Zoystick zoystick) {
        return new Zambda<>((x) -> {
            if (zoystick.pressed("X")) {
                this.headless();
            }
            this.debug("x", zoystick.button("X"));
        }, this);
    }

    public ZCommand check_wheel_reset(Zoystick zoystick) {
        return new Zambda<>((x) -> {
            if (zoystick.pressed("A")) {
                this.reset();
            }
            if (zoystick.pressed("Y")) {
                this.module[0].clear();
                this.module[1].clear();
                this.module[2].clear();
                this.module[3].clear();
            }
        }, this);
    }

    public Zwerve reset() {
        this.module[0].reset();
        this.module[1].reset();
        this.module[2].reset();
        this.module[3].reset();
        this.gyro.reset();
        return this;
    }

    public Zwerve clear() {
        this.module[0].clear();
        this.module[1].clear();
        this.module[2].clear();
        this.module[3].clear();
        this.gyro.reset();
        return this;
    }

    @Override
    public String key() {
        return "Zwerve";
    }

    /**
     * Defines one swerve module.
     */
    public interface Module {

        Module init();

        Module go(Vec2D velocity);

        Module reset();

        Module clear();
    }
}
