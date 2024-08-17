package frc.libzodiac;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;

public class Zamera {
    private UsbCamera camera;

    public Zamera start() {
        camera = CameraServer.startAutomaticCapture();
        return this;
    }

    public void close() {
        camera.close();
    }
}
