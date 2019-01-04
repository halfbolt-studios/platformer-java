package net.halfbolt.platformer.enemy.pathfind;

import com.badlogic.gdx.math.Vector2;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.collision.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pathfind {
    public static Node findPath(Point sPos, Point ePos, World w) {
        int minX, minY, maxX, maxY;
        if (ePos.getX() > sPos.getX()) {
            minX = (int) sPos.getX() - 5;
            maxX = (int) ePos.getX() + 5;
        } else {
            minX = (int) ePos.getX() - 5;
            maxX = (int) sPos.getX() + 5;
        }
        if (ePos.getY() > sPos.getY()) {
            minY = (int) sPos.getY() - 5;
            maxY = (int) ePos.getY() + 5;
        } else {
            minY = (int) ePos.getY() - 5;
            maxY = (int) sPos.getY() + 5;
        }
        return findPath(sPos, ePos, new MapSegment(new Vector2(minX, minY), new Vector2(maxX, maxY), w.getTileManager().getMap()));
    }

    private static Node findPath(Point sPos, Point ePos, MapSegment map) {
        if (!map.inBounds(sPos)) {
            return null;
        }
        if (!map.inBounds(ePos)) {
            return null;
        }
        if (map.get(sPos).equals(Type.EXPLORED) || map.get(sPos).equals(Type.WALL)) {
            return null;
        }
        map.set(sPos, Type.EXPLORED);
        if (sPos.equals(ePos)) {
            return new Node(sPos.clone());
        }
        ArrayList<Point> offsets = new ArrayList<>();
        offsets.add(new Point(1, 0));
        offsets.add(new Point(-1, 0));
        offsets.add(new Point(0, 1));
        offsets.add(new Point(0, -1));
        Collections.shuffle(offsets);

        int minLength = Integer.MAX_VALUE;
        Node newNode = null;
        for (int i = 0; i < offsets.size(); i++) {
            Node node = findPath(sPos.add(offsets.get(i)), ePos, map);
            if (node != null) {
                int length = node.getLength();
                if (length < minLength) {
                    minLength = length;
                    newNode = new Node(sPos.clone(), node);
                }
            }
        }
        return newNode;
    }

    private static class MapSegment {
        private Vector2 min;
        private Vector2 max;
        private HashMap<Point, Type> map = new HashMap<>();
        public MapSegment(Vector2 min, Vector2 max, HashMap<Point, Tile> bigMap) {
            this.min = min;
            this.max = max;
            if (min.x >= max.x || min.y >= max.y) {
                throw new RuntimeException("Min is greater than max");
            }
            for (int y = (int) min.y; y < max.y; y++) {
                for (int x = (int) min.x; x < max.x; x++) {
                    Point pos = new Point(x, y);
                    if (bigMap.get(pos) == null) {
                        map.put(pos, Type.EMPTY);
                    } else {
                        map.put(pos, Type.WALL);
                    }
                }
            }
        }

        public boolean inBounds(Point pos) {
            return pos.getX() >= min.x && pos.getX() < max.x && pos.getY() >= min.y && pos.getY() < max.y;
        }

        public void set(Point pos, Type type) {
            if (!inBounds(pos)) {
                throw new RuntimeException("Position " + pos + " is outside of bounds");
            }
            map.put(pos, type);
        }

        public Type get(Point pos) {
            if (!inBounds(pos)) {
                throw new RuntimeException("Position " + pos + " is outside of bounds");
            }
            return map.get(pos);
        }
    }

    private enum Type {
        EMPTY, EXPLORED, WALL
    }
}
