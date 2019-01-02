package net.halfbolt.platformer.enemy;

import com.badlogic.gdx.utils.Array;

public class DepthFirst {

    //TODO: Delete this file
    public static boolean searchPath(Array<Array<Node>> maze, int x, int y, Array<Integer> path) {

        if (maze.get(y).get(x).type == 3) {
            path.add(x);
            path.add(y);
            return true;
        }

        int dx = -1;
        int dy = 0;
        if (searchPath(maze, x + dx, y + dy, path)) {
            path.add(x);
            path.add(y);
            return true;
        }

        dx = 1;
        dy = 0;
        if (searchPath(maze, x + dx, y + dy, path)) {
            path.add(x);
            path.add(y);
            return true;
        }

        dx = 0;
        dy = -1;
        if (searchPath(maze, x + dx, y + dy, path)) {
            path.add(x);
            path.add(y);
            return true;
        }

        dx = 0;
        dy = 1;
        if (searchPath(maze, x + dx, y + dy, path)) {
            path.add(x);
            path.add(y);
            return true;
        }

        return false;
    }

}
