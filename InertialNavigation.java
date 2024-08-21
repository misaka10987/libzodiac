package frc.libzodiac;

import edu.wpi.first.wpilibj.Timer;
import frc.libzodiac.util.Vec2D;

public class InertialNavigation extends Zubsystem implements ZmartDash {
    private final Gyro gyro;

    private final Timer timer = new Timer();

    private double zero = 0;
    private Vec2D gravity = new Vec2D(0,0);
    private Vec2D pos = new Vec2D(0, 0);
    private Vec2D speed = new Vec2D(0, 0);

    public ZCommand run = new Zambda(this, this::update);

    public InertialNavigation(Gyro gyro) {
        this.gyro = gyro;
    }

    public InertialNavigation init() {
        this.timer.start();
        this.setZero();
        return this;
    }

    public InertialNavigation setZero() {
        this.zero = this.gyro.getYaw();
//        this.gravity = this.gyro.getAcceleration();
        return this;
    }

    public InertialNavigation setZero(double zero) {
        this.zero = zero;
        return this;
    }

    private Vec2D getAcceleration() {
        return this.gyro.getAcceleration().sub(gravity).mul(10);//.rot(-this.zero);
    }

    public InertialNavigation update() {
        this.debug("posinav", "" + this.getPosition());
        this.debug("yawinav", this.getYaw());
        this.debug("acc", "" + this.getAcceleration());
        final var loopTime = timer.get();
        timer.reset();
        this.speed = this.speed.add(this.getAcceleration().mul(loopTime));
        Vec2D dis = this.speed.mul(loopTime);
        this.pos = this.pos.add(dis);
        return this;
    }

    public Vec2D getPosition() {
        return pos;
    }

    public Vec2D getSpeed() {
        return this.speed;
    }

    public double getYaw() {
        return this.gyro.getYaw() - this.zero;
    }

    @Override
    public String key() {
        return "inav";
    }

    public interface Gyro {
        double getYaw();

        Vec2D getAcceleration();
    }
}
