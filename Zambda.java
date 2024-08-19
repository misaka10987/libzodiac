package frc.libzodiac;

import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Allows you to construct wpilib <code>Command</code> with a lambda.
 */
public final class Zambda extends ZCommand {
    public final Runnable exec;

    public Zambda(Subsystem req, Runnable exec) {
        this.exec = exec;
        this.require(req);
    }

    public Zambda(Subsystem[] req, Runnable exec) {
        this.exec = exec;
        for (final var i : req)
            this.require(i);
    }

    public static Zambda indep(Runnable exec) {
        return new Zambda(new Subsystem[]{}, exec);
    }

    @Override
    protected ZCommand exec() {
        this.exec.run();
        return this;
    }
}