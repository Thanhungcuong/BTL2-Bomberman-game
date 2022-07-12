package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

import java.util.Random;

public abstract class Enemy extends Character {
    protected Random random = new Random();
    protected int direction;

    public Enemy(int x, int y, Image image) {
        super(x, y, image);
    }
}
