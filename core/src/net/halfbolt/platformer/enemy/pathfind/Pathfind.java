package net.halfbolt.platformer.enemy.pathfind;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.tilemap.Layer;

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
        Layer map = w.getMap().getLayer(1);
        if (map.get(sPos) == null || (map.get(sPos).getID() != 0 && map.get(sPos).getID() != 1)) {
            return findPath(sPos, ePos, new MapSegment(new Point(minX, minY), new Point(maxX, maxY), map));
        }
        //System.out.println("minX: " + minX + ", minY: " + minY + ", maxX: " + maxX + ", maxY: " + maxY);
        ArrayList<Point> extensions = new ArrayList<>();
        extensions.add(new Point(sPos.getX() - 1, sPos.getY() + 1));
        extensions.add(new Point(sPos.getX() - 1, sPos.getY()));
        extensions.add(new Point(sPos.getX() - 1, sPos.getY() - 1));
        extensions.add(new Point(sPos.getX(), sPos.getY() - 1));
        extensions.add(new Point(sPos.getX(), sPos.getY() + 1));
        extensions.add(new Point(sPos.getX() + 1, sPos.getY() + 1));
        extensions.add(new Point(sPos.getX() + 1, sPos.getY()));
        extensions.add(new Point(sPos.getX() + 1, sPos.getY() - 1));
        Collections.shuffle(extensions);
        for (Point pos : extensions) {
            if (map.get(pos) == null || (map.get(pos).getID() != 0 && map.get(pos).getID() != 1)) {
                return findPath(pos, ePos, new MapSegment(new Point(minX, minY), new Point(maxX, maxY), map));
            }
        }
        return null;
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
            float minLength = Float.MAX_VALUE;
            Node returnNode = null;
            for (Node node : tipNodes) {
                ArrayList<Node> extensions = new ArrayList<>();
                extensions.add(new Node(new Point(node.getPos().getX() - 1, node.getPos().getY() + 1), node));
                extensions.add(new Node(new Point(node.getPos().getX() - 1, node.getPos().getY()), node));
                extensions.add(new Node(new Point(node.getPos().getX() - 1, node.getPos().getY() - 1), node));
                extensions.add(new Node(new Point(node.getPos().getX(), node.getPos().getY() - 1), node));
                extensions.add(new Node(new Point(node.getPos().getX(), node.getPos().getY() + 1), node));
                extensions.add(new Node(new Point(node.getPos().getX() + 1, node.getPos().getY() + 1), node));
                extensions.add(new Node(new Point(node.getPos().getX() + 1, node.getPos().getY()), node));
                extensions.add(new Node(new Point(node.getPos().getX() + 1, node.getPos().getY() - 1), node));
                Collections.shuffle(extensions);
                for (Node extension : extensions) {
                    if (map.inBounds(extension.getPos()) &&
                            map.get(extension.getPos()) != Type.WALL &&
                            map.get(extension.getPos()) != Type.EXPLORED) {
                        // Check if it's the start node (still looking backward)
                        if (extension.getPos().equals(sPos) && extension.getLength() < minLength) {
                            minLength = extension.getLength();
                            returnNode = extension;
                        }
                        newTipNodes.add(extension);
                        map.set(extension.getPos(), Type.EXPLORED);
                    }
                }
            }
            if (returnNode != null) {
                return returnNode;
            }
            tipNodes = newTipNodes;
        }
        return null;
    }

    private static class MapSegment {
        private Point min;
        private Point max;
        private HashMap<Point, Type> map = new HashMap<>();
        private MapSegment(Point min, Point max, Layer bigMap) {
            this.min = min;
            this.max = max;
            if (min.getX() >= max.getX() || min.getY() >= max.getY()) {
                throw new RuntimeException("Min is greater than max");
            }
            for (int y = min.getY(); y < max.getY(); y++) {
                for (int x = min.getX(); x < max.getX(); x++) {
                    Point pos = new Point(x, y);
                    if (bigMap.get(pos) != null && (bigMap.get(pos).getID() == 0 || bigMap.get(pos).getID() == 1)) {
                        map.put(pos, Type.WALL);
                    } else {
                        map.put(pos, Type.EMPTY);
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
