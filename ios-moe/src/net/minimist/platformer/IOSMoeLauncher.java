package net.minimist.platformer;

import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import org.moe.natj.general.Pointer;
import net.minimist.platformer.Platformer;

import apple.uikit.c.UIKit;

public class IOSMoeLauncher extends IOSApplication.Delegate {

  protected IOSMoeLauncher(Pointer peer) {
    super(peer);
  }

  @Override
  protected IOSApplication createApplication() {
    IOSApplicationConfiguration config = new IOSApplicationConfiguration();
    config.useAccelerometer = false;
    return new IOSApplication(new Platformer(), config);
  }

  public static void main(String[] argv) {
    UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
  }
}
