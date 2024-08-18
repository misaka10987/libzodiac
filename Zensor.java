package frc.libzodiac;

import java.security.InvalidParameterException;

public interface Zensor {
    double get();

    default double get(String value) throws InvalidParameterException {
        return this.get();
    }
}
