package frc.libzodiac;

/**
 * Common utilities.
 */
public class Util {
    /**
     * Convert degrees to radians.
     */
    public static double rad(double deg) {
        return deg / 180 * Math.PI;
    }

    /**
     * Convert radians to degrees.
     */
    public static double deg(double rad) {
        return rad / Math.PI * 180;
    }

    /**
     * Extends the modulo operation to R.
     * <br/>
     * The definition here:
     * <br/>
     * a and b are congruent modulo c, if and only if (a-b)/c is integer.
     *
     * @return In (-mod,mod), and NaN for NaN and Infinite parameters.
     */
    public static double mod(Double num, double mod) {
        // I believe there are still some bugs.
        if (num.isInfinite() || num.isNaN())
            return Double.NaN;
        if (num < 0) {
            return -mod(-num, mod);
        }
        final var ans = (int) (num / 2 / mod);
        final var res = num - ans * 2 * mod;
        return res > mod ? res - 2 * mod : res;
    }

    /**
     * Convert a radian into (-pi,pi).
     */
    public static double mod_pi(double rad) {
        return mod(rad, Math.PI);
    }

}
