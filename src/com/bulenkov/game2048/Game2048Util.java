package com.bulenkov.game2048;

import java.util.*;

public class Game2048Util {

    public static CheckNewState right(Tile[] tiles) {
        tiles = rotate(tiles, 180);
        boolean canMoveRight = left(tiles).isCanMove();
        return new CheckNewState(canMoveRight, rotate(tiles, 180));
    }

    public static CheckNewState up(Tile[] tiles) {
        tiles = rotate(tiles, 270);
        boolean canMoveUp = left(tiles).isCanMove();
        return new CheckNewState(canMoveUp, rotate(tiles, 90));
    }

    public static CheckNewState down(Tile[] tiles) {
        tiles = rotate(tiles, 90);
        boolean canMoveDown = left(tiles).isCanMove();
        return new CheckNewState(canMoveDown, rotate(tiles, 270));
    }

    public static CheckNewState left(Tile[] tiles) {
        boolean needAddTile = false;
        for (int i = 0; i < 4; i++) {
            Tile[] line = getLine(i, tiles);
            Tile[] merged = mergeLine(moveLine(line));
            setLine(i, merged, tiles);
            if (!needAddTile && !compare(line, merged)) {
                needAddTile = true;
            }
        }

        return new CheckNewState(needAddTile, tiles);
    }

    private static Tile[] rotate(Tile[] tiles, int angle) {
        Tile[] newTiles = new Tile[4 * 4];
        int offsetX = 3, offsetY = 3;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * 4] = tileAt(tiles, x, y);
            }
        }
        return newTiles;
    }

    private static Tile[] getLine(int index, Tile[] tiles) {
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++) {
            result[i] = tileAt(tiles, i, index);
        }
        return result;
    }

    private static void setLine(int index, Tile[] re, Tile[] tiles) {
        System.arraycopy(re, 0, tiles, index * 4, 4);
    }

    private static Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<Tile>();
        for (int i = 0; i < 4; i++) {
            if (!oldLine[i].isEmpty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[4];
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private static Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].value;
            if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
                num *= 2;
                i++;
            }
            list.add(new Tile(num));
        }
        if (list.size() == 0) {
            return oldLine;
        } else {
            ensureSize(list, 4);
            return list.toArray(new Tile[4]);
        }
    }

    private static boolean compare(Tile[] line1, Tile[] line2) {
        if (line1 == line2) {
            return true;
        } else if (line1.length != line2.length) {
            return false;
        }

        for (int i = 0; i < line1.length; i++) {
            if (line1[i].value != line2[i].value) {
                return false;
            }
        }
        return true;
    }

    private static void ensureSize(java.util.List<Tile> l, int s) {
        while (l.size() != s) {
            l.add(new Tile());
        }
    }

    public static boolean addTile(Tile[] tiles) {
        List<Tile> list = availableSpace(tiles);
        if (!list.isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile emptyTime = list.get(index);
            emptyTime.value = Math.random() < 0.9 ? 2 : 4;
            return (emptyTime.value == 2) ? true : false;
        }
        return false;
    }


    public static List<Tile> availableSpace(Tile[] tiles) {
        final List<Tile> list = new ArrayList<Tile>(16);
        for (Tile t : tiles) {
            if (t.isEmpty()) {
                list.add(t);
            }
        }
        return list;
    }

    private static Tile tileAt(Tile[] tiles, int x, int y) {
        return tiles[x + y * 4];
    }


}