package net.halfbolt.platformer.world.tilemap;

import net.halfbolt.platformer.world.World;

import java.util.HashMap;

public class Tilemap {
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private HashMap<Integer, Layer> layers;
    public Tilemap(World w, String filename) {
        Tilemap tilemap = Loader.load(w, filename);
        if (tilemap == null) {
            throw(new RuntimeException("Tilemap is null"));
        }
        this.width = tilemap.width;
        this.height = tilemap.height;
        this.tileWidth = tilemap.tileWidth;
        this.tileHeight = tilemap.tileHeight;
        this.layers = tilemap.layers;
    }

    Tilemap(int width, int height, int tileWidth, int tileHeight, HashMap<Integer, Layer> layers) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.layers = layers;
    }

    public Layer getLayer(int i) {
        return layers.get(i);
    }

    @Override
    public String toString() {
        return "Tilemap{" +
                "width=" + width +
                ", height=" + height +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                ", layers=" + layers +
                '}';
    }
}
