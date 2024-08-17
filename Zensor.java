package frc.libzodiac;

import java.security.InvalidParameterException;

public interface Zensor {
    double get();

    Zensor reset(double zero);

    default Zensor reset() {
        return this.reset(this.get());
    }

    default double get(String value) throws InvalidParameterException {
        return this.get();
    }

    default Zensor reset(String value, double zero) throws InvalidParameterException {
        return this.reset(zero);
    }

    default Zensor reset(String value) throws InvalidParameterException {
        return this.reset(value, this.get(value));
    }
}
