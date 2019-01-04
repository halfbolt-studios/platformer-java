package net.halfbolt.platformer.enemy.pathfind;

import net.halfbolt.platformer.helper.Point;

import java.util.Objects;

public class Node {

    private Point p;
    private Node child;

    public Node(Point p) {
        this.p = p;
        child = null;
    }
    public Node(Point p, Node child) {
        this.p = p;
        this.child = child;
    }

    public Point getPos() {
        return p;
    }

    public int getLength() {
        if (child == null) {
            return 0;
        }
        return child.getLength() + 1;
    }

    public Node getChild() {
        return child;
    }

    public Node clone() {
        return new Node(p, child);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(p, node.p) &&
                Objects.equals(child, node.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, child);
    }
}
