package net.halfbolt.platformer.player.lantern;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.World;

public class Lantern {
    public Lantern(World w, Player player) {
        PointLight light = new PointLight(w.getRender().getLights(), 300, Color.ORANGE, 15, 10, 10);
        light.attachToBody(player.getBody());
        light.setSoft(false);
    }

    public void render() {
    }
}
