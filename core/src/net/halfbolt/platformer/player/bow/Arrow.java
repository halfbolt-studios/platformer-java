package net.halfbolt.platformer.player.bow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.LevelManager;
import net.halfbolt.platformer.world.tilemap.tile.Tile;

public class Arrow {
    private Body body;
    private LevelManager manager;
    protected int damage = 0;
    private boolean destroyed = false;
    private boolean needDestroy = false;

    public Arrow(LevelManager manager, Vector2 shooterPos, boolean playerShot, float chargeAmount, Vector2 target) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(shooterPos);

        body = manager.getWorld().createBody(bodyDef);
        body.setLinearDamping(1f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 30f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = playerShot ? Player.playerBits : Enemy.enemyBits;
        if (playerShot) {
            fixtureDef.filter.maskBits = Tile.tileBits | Enemy.enemyBits;
        } else {
            fixtureDef.filter.maskBits = Tile.tileBits | Player.playerBits;
        }

        body.createFixture(fixtureDef);

        //get velocity of aim controller
            //Vector2 target = manager.getRender().getGui().getControl().getBowTarget(p);
            //Gdx.app.log(Arrow.class.getName(),target + "");
            float speed = 3;
            Vector2 delta = new Vector2(-1 * target.x * chargeAmount * speed, -1 * target.y * chargeAmount * speed);
            body.setLinearVelocity(body.getLinearVelocity().add(delta));
        this.manager = manager;
    }

    public boolean update() { // returns true if needs to be destroyed
        if (!body.isAwake() || needDestroy) {
            manager.getWorld().destroyBody(body);
            destroyed = true;
            return true;
        }
        return false;
    }

    public void render() {
    }

    public void dispose() {
    }

    public Body getBody() {
        return body;
    }

    public int getDamage() {
        if (destroyed) {
            throw new RuntimeException("Calling getDamage when destroyed!");
        }
        return (int) (damage + body.getLinearVelocity().len());
    }

    public void destroy() {
        needDestroy = true;
    }
}
