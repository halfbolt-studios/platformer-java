package net.halfbolt.platformer.world.tilemap;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.tilemap.tile.Tile;

import java.util.HashMap;

public class Layer {
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private boolean visible;
    private HashMap<Point, Tile> map;
    public Layer(int width, int height, int tileWidth, int tileHeight, HashMap<Point, Tile> map, boolean visible) {
        this.map = map;
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public Tile get(int x, int y) {
        return get(new Point(x, y));
    }

    public Tile get(Point pos) {
        return map.get(pos);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "width=" + width +
                ", height=" + height +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                ", visible=" + visible +
                ", map=" + map +
                '}';
    }

    public int tileWidth() {
        return tileWidth;
    }

    public int tileHeight() {
        return tileHeight;
    }
}
