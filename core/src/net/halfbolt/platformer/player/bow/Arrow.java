package net.halfbolt.platformer.player.bow;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.tilemap.tile.Tile;

public class Arrow {
    private Body body;
    private World w;

    public Arrow(World w, Player p, float chargeAmount) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(p.getPos());

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(1f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = Player.playerBits;
        fixtureDef.filter.maskBits = Tile.tileBits | Enemy.enemyBits;

        body.createFixture(fixtureDef);

        Vector2 delta = w.getRender().getGui().getTileFromCursor().sub(p.getPos());
        float speed = 3;
        delta = new Vector2(delta.x * chargeAmount * speed, delta.y * chargeAmount * speed);
        body.applyForceToCenter(delta, true);
        this.w = w;
    }

    public boolean update() { // returns true if needs to be destroyed
        if (!body.isAwake()) {
            w.getWorld().destroyBody(body);
            return true;
        }
        return false;
    }

    public void render() {
    }

    public void dispose() {
    }
}
