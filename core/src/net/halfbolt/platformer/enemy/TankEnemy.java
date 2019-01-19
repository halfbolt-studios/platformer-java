package net.halfbolt.platformer.enemy;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;

public class TankEnemy extends Enemy {
    public TankEnemy(Point pos, World w) {
        size = 1f;
        speed = 60;
        this.w = w;
        createBody(pos);
    }
}
