package net.halfbolt.platformer.enemy;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.Level;

public class TankEnemy extends Enemy {

    public TankEnemy(Level l, Point pos) {
        size = 1f;
        speed = 200;
        maxHealth = 20;
        init(l, pos);
    }
}
