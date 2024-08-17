package frc.libzodiac.util;

import java.util.function.Function;

import frc.libzodiac.Util;

public class Vec2D {

    public final double x;
    public final double y;

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vec2D add(Vec2D a, Vec2D b) {
        return new Vec2D(a.x + b.x, a.y + b.y);
    }

    public static Polar add(Polar a, Polar b) {
        return Vec2D.add(a.into(), b.into()).into();
    }

    public static double mul(Vec2D a, Vec2D b) {
        return a.r() * b.r() * Math.cos(a.theta() - b.theta());
    }

    public static double mul(Polar a, Polar b) {
        return Vec2D.mul(a.into(), b.into());
    }

    public static Vec2D from(Polar v) {
        return v.into();
    }

    @Override
    public String toString() {
        // final var x = String.format("%.3e", this.x);
        // final var y = String.format("%.3e", this.y);
        return "(" + Util.printed(this.x) + "," + Util.printed(this.y) + ")";
    }

    public Vec2D with_x(double x) {
        return new Vec2D(x, this.y);
    }

    public Vec2D with_y(double y) {
        return new Vec2D(this.x, y);
    }

    public Vec2D with_r(double r) {
        return this.into().with_r(r).into();
    }

    public Vec2D with_theta(double theta) {
        return this.into().with_theta(theta).into();
    }

    public double r() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double theta() {
        return Math.atan2(this.y, this.x);
    }

    public Vec2D inv() {
        return new Vec2D(-this.x, -this.y);
    }

    public Vec2D add(Vec2D rhs) {
        return Vec2D.add(this, rhs);
    }

    public Vec2D add(Polar rhs) {
        return this.add(rhs.into());
    }

    public Vec2D sub(Vec2D rhs) {
        return Vec2D.add(this, rhs.inv());
    }

    public Vec2D sub(Polar rhs) {
        return this.sub(rhs.into());
    }

    public Vec2D mul(double k) {
        return new Vec2D(this.x * k, this.y * k);
    }

    public Vec2D mul(Vec2D v) {
        return new Vec2D(this.x * v.x, this.y * v.y);
    }

    public Vec2D div(double k) {
        return this.mul(1 / k);
    }

    public Vec2D rot(double phi) {
        return this.with_theta(this.theta() + phi);
    }

    public Vec2D max(Vec2D other) {
        if (this.r() > other.r()) {
            return this;
        }
        return other;
    }

    public Vec2D map_x(Function<Double, Double> mapping) {
        return this.with_x(mapping.apply(this.x));
    }

    public Vec2D map_y(Function<Double, Double> mapping) {
        return this.with_y(mapping.apply(this.y));
    }

    public Polar into() {
        return Polar.from(this);
    }

    public static class Polar {
        public final double r;
        public final double theta;

        public Polar(double r, double theta) {
            this.r = r;
            this.theta = theta;
        }

        public static Polar from(Vec2D v) {
            return new Polar(v.r(), v.theta());
        }

        public Polar with_r(double r) {
            return new Polar(r, this.theta);
        }

        public Polar with_theta(double theta) {
            return new Polar(this.r, theta);
        }

        public Vec2D into() {
            return new Vec2D(this.r * Math.cos(this.theta), this.r * Math.sin(this.theta));
        }

        public double x() {
            return this.into().x;
        }

        public double y() {
            return this.into().y;
        }

        public Polar add(Polar rhs) {
            return this.into().add(rhs).into();
        }

        public Polar add(Vec2D rhs) {
            return this.into().add(rhs).into();
        }

        public Polar sub(Polar rhs) {
            return this.into().sub(rhs).into();
        }

        public Polar sub(Vec2D rhs) {
            return this.into().sub(rhs).into();
        }

        public Polar mul(double k) {
            return this.into().mul(k).into();
        }

        public Polar div(double k) {
            return this.into().div(k).into();
        }

        public Polar rot(double phi) {
            return this.into().rot(phi).into();
        }
    }
}