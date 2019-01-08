package net.halfbolt.platformer.enemy.pathfind;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.collision.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pathfind {

    public static Node findPath(Point sPos, Point ePos, World w, int offsetAmount) {
        int minX, minY, maxX, maxY;
        if (ePos.getX() > sPos.getX()) {
            minX = (sPos.getX() - offsetAmount);
            maxX = (ePos.getX() + offsetAmount);
        } else {
            minX = (ePos.getX() - offsetAmount);
            maxX = (sPos.getX() + offsetAmount);
        }
        if (ePos.getY() > sPos.getY()) {
            minY = (sPos.getY() - offsetAmount);
            maxY = (ePos.getY() + offsetAmount);
        } else {
            minY = (ePos.getY() - offsetAmount);
            maxY = (sPos.getY() + offsetAmount);
        }
        //System.out.println("minX: " + minX + ", minY: " + minY + ", maxX: " + maxX + ", maxY: " + maxY);
        return findPath(sPos, ePos, new MapSegment(new Point(minX, minY), new Point(maxX, maxY), w.getTileManager().getMap()));
    }

    private static Node findPath(Point sPos, Point ePos, MapSegment map) {
        // Completely new pathfinding. Works much better, and there's no recursion!
        // It looks outward from the end position, until it finds the start position.
        // This is because out nodes store the child, not the parent.
        ArrayList<Node> tipNodes = new ArrayList<>();
        // Set the list of nodes going outward to be the end (we are looking backward here)
        tipNodes.add(new Node(ePos.clone()));
        while (tipNodes.size() > 0) {
            ArrayList<Node> newTipNodes = new ArrayList<>();
            for (Node node : tipNodes) {
                // Check if it's the start node (still looking backward)
                if (node.getPos().equals(sPos)) {
                    return node;
                }
                ArrayList<Node> extentions = new ArrayList<>();
                extentions.add(new Node(new Point(node.getPos().getX() - 1, node.getPos().getY()), node));
                extentions.add(new Node(new Point(node.getPos().getX(), node.getPos().getY() - 1), node));
                extentions.add(new Node(new Point(node.getPos().getX(), node.getPos().getY() + 1), node));
                extentions.add(new Node(new Point(node.getPos().getX() + 1, node.getPos().getY()), node));
                Collections.shuffle(extentions);
                for (Node extension : extentions) {
                    if (map.inBounds(extension.getPos()) &&
                            map.get(extension.getPos()) != Type.WALL &&
                            map.get(extension.getPos()) != Type.EXPLORED) {
                        newTipNodes.add(extension);
                        map.set(extension.getPos(), Type.EXPLORED);
                    }
                }

            }
            tipNodes = newTipNodes;
        }
        return null;
    }

    private static class MapSegment {
        private Point min;
        private Point max;
        private HashMap<Point, Type> map = new HashMap<>();
        private MapSegment(Point min, Point max, HashMap<Point, Tile> bigMap) {
            this.min = min;
            this.max = max;
            if (min.getX() >= max.getX() || min.getY() >= max.getY()) {
                throw new RuntimeException("Min is greater than max");
            }
            for (int y = min.getY(); y < max.getY(); y++) {
                for (int x = min.getX(); x < max.getX(); x++) {
                    Point pos = new Point(x, y);
                    if (bigMap.get(pos) == null) {
                        map.put(pos, Type.EMPTY);
                    } else {
                        map.put(pos, Type.WALL);
                    }
                }
            }
        }

        private boolean inBounds(Point pos) {
            return pos.getX() >= min.getX() && pos.getX() < max.getX() && pos.getY() >= min.getY() && pos.getY() < max.getY();
        }

        private void set(Point pos, Type type) {
            if (!inBounds(pos)) {
                throw new RuntimeException("Position " + pos + " is outside of bounds");
            }
            map.put(pos, type);
        }

        private Type get(Point pos) {
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
