package frc.libzodiac.hardware;

import com.ctre.phoenix6.hardware.Pigeon2;
import frc.libzodiac.Zensor;
import frc.libzodiac.ZmartDash;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class Pigeon implements Zensor, ZmartDash {
    public final int can_id;
    protected Pigeon2 pigeon = null;
    protected HashMap<String, Double> zero = new HashMap<>();

    public Pigeon(int can_id) {
        this.can_id = can_id;
        this.zero.put("yaw", 0.0);
        this.zero.put("pitch", 0.0);
        this.zero.put("roll", 0.0);
    }

    @Override
    public Pigeon init() {
        this.pigeon = new Pigeon2(this.can_id);
        return this;
    }

    @Override
    public double get() {
        return this.get("yaw");
    }

    @Override
    public Pigeon reset(double zero) {
        return this.reset("yaw", zero);
    }

    @Override
    public double get(String value) {
        return switch (value.trim().toLowerCase()) {
            case "yaw" -> this.pigeon.getYaw().getValue();
            case "pitch" -> this.pigeon.getPitch().getValue();
            case "roll" -> this.pigeon.getRoll().getValue();
            default -> throw new InvalidParameterException(value);
        };
    }

    @Override
    public Pigeon reset(String value, double zero) {
        this.zero.put(value, zero);
        return this;
    }

    @Override
    public String key() {
        return "Pigeon(" + this.can_id + ")";
    }
}