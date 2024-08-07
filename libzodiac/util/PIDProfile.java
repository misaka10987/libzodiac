package frc.libzodiac.util;

public class PIDProfile {
    public final double k_p, k_i, k_d;

    public PIDProfile(double k_p, double k_i, double k_d) {
        this.k_p = k_p;
        this.k_i = k_i;
        this.k_d = k_d;
    }
}