package net.halfbolt.platformer.enemy;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.Level;

public class SmallEnemy extends Enemy {

    public SmallEnemy(Level l, Point pos) {
        super();
        size = 0.3f;
        speed = 350;
        maxHealth = 5;
        init(l, pos);
    }
}
