package frc.libzodiac.ui;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Button {
    private final BooleanSupplier status;
    private boolean prev = false;

    public Button(BooleanSupplier status) {
        this.status = status;
    }

    public boolean status() {
        return this.status.getAsBoolean();
    }

    public boolean pressed() {
        final var curr = this.status();
        final var res = !this.prev && curr;
        this.prev = curr;
        return res;
    }

    public boolean released() {
        final var curr = this.status();
        final var res = this.prev && !curr;
        this.prev = curr;
        return res;
    }

    public Trigger trigger() {
        return new Trigger(this.status);
    }

    public Button on_press(Command cmd) {
        this.trigger().toggleOnTrue(cmd);
        return this;
    }

    public Button on_release(Command cmd) {
        this.trigger().toggleOnFalse(cmd);
        return this;
    }

    public Button on_down(Command cmd) {
        this.trigger().onTrue(cmd);
        return this;
    }

    public Button on_up(Command cmd) {
        this.trigger().onFalse(cmd);
        return this;
    }
}
