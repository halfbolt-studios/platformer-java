package net.halfbolt.platformer.world.tile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.halfbolt.platformer.world.World;

public class Tile {
    private Body body;

    public Tile(World w, Vector2 pos) {
        BodyDef groundBodyDef = new BodyDef();

        body = w.getWorld().createBody(groundBodyDef);
        body.setTransform(new Vector2(pos.x + 0.5f, pos.y + 0.5f), 0);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(0.5f, 0.5f);

        body.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }
}
