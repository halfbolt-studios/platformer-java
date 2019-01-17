package net.halfbolt.platformer.player.lantern;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.World;

public class Lantern {
    Player player;
    World w;
    private Body body;

    public Lantern(World w, Player player) {
        PointLight light = new PointLight(w.getRender().getLights(), 300, Color.ORANGE, 15, 10, 10);
        light.attachToBody(player.getBody());
        light.setSoft(false);
        this.player = player;
        this.w = w;

    }

    public void update() {
        Vector2 delta = w.getRender().getControl().getLanternDelta(body.getPosition());
    }

    public void render() {
    }

    public void dispose() {
    }
}
