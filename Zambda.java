package frc.libzodiac;

import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.function.Consumer;

/**
 * Allows you to construct wpilib's <code>Command</code> with a lambda.
 */
public class Zambda<T extends Subsystem> extends ZCommand {
    public final T src;
    public final Consumer<T> exec;

    public Zambda(Consumer<T> exec, T src) {
        this.exec = exec;
        this.src = this.require(src);
    }

    @Override
    protected ZCommand exec() {
        this.exec.accept(this.src);
        return this;
    }
}