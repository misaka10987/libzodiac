package frc.libzodiac;

import edu.wpi.first.wpilibj.Timer;
import frc.libzodiac.util.Vec2D;

public class InertialNavigation extends Zubsystem {
    private final Gyro gyro;

    private final Timer timer = new Timer();

    private double zero = 0;
    private Vec2D pos = new Vec2D(0, 0);
    private Vec2D speed = new Vec2D(0, 0);

    public ZCommand run = new Zambda(this, this::update);

    public InertialNavigation(Gyro gyro) {
        this.gyro = gyro;
    }

    public InertialNavigation init() {
        this.timer.start();
        return this;
    }

    public InertialNavigation setZero() {
        this.zero = this.gyro.getYaw();
        return this;
    }

    public InertialNavigation setZero(double zero) {
        this.zero = zero;
        return this;
    }

    private Vec2D getAcceleration() {
        return this.gyro.getAcceleration().rot(-this.zero);
    }

    public InertialNavigation update() {
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

    public interface Gyro {
        double getYaw();

        Vec2D getAcceleration();
    }
}
