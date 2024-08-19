package frc.libzodiac.hardware;

import com.ctre.phoenix6.hardware.Pigeon2;
import frc.libzodiac.Util;
import frc.libzodiac.Zensor;
import frc.libzodiac.ZmartDash;
import frc.libzodiac.ui.Axis;

import java.security.InvalidParameterException;

public class Pigeon implements Zensor, ZmartDash {
    public final int can_id;
    protected Pigeon2 pigeon = null;

    public Pigeon(int can_id) {
        this.can_id = can_id;
        this.init();
    }

    public Pigeon init() {
        this.pigeon = new Pigeon2(this.can_id);
        return this;
    }

    public Axis yaw() {
        return new Axis(() -> Util.rad(this.pigeon.getYaw().refresh().getValue()));
    }

    public Axis pitch() {
        return new Axis(() -> Util.rad(this.pigeon.getPitch().refresh().getValue()));
    }

    public Axis roll() {
        return new Axis(() -> Util.rad(this.pigeon.getRoll().refresh().getValue()));
    }

    @Override
    public double get() {
        return this.get("yaw");
    }

    @Override
    public double get(String value) {
        return switch (value.trim().toLowerCase()) {
            case "yaw" -> Util.rad(this.pigeon.getYaw().getValue());
            case "pitch" -> Util.rad(this.pigeon.getPitch().getValue());
            case "roll" -> Util.rad(this.pigeon.getRoll().getValue());
            default -> throw new InvalidParameterException(value);
        };
    }

    @Override
    public String key() {
        return "Pigeon(" + this.can_id + ")";
    }
}