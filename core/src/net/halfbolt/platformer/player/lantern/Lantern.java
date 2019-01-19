package net.halfbolt.platformer.player.lantern;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.tilemap.tile.Tile;

public class Lantern {
    Player player;
    World w;
    private Body body;

    public Lantern(World w, Player player) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getPos());

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(8f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = Player.playerBits;
        fixtureDef.filter.maskBits = Tile.tileBits;

        body.createFixture(fixtureDef);

        circle.dispose();

        PointLight light = new PointLight(w.getRender().getLights(), 300, Color.ORANGE, 15, 10, 10);
        light.attachToBody(body);
        light.setSoft(false);
        this.player = player;
        this.w = w;

    }

    public void update() {
        Vector2 delta = w.getRender().getGui().getControl().getLanternDelta(body.getPosition());
        body.applyForceToCenter(delta, true);
    }

    public void render() {
    }

    public void dispose() {
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
