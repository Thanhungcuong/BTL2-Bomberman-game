package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Bomb extends Character {
    private int radius; //pham vi bom no
    private int timeCounter = 0;

    public Bomb(int x, int y, Image image) {
        super(x, y, image);
        setLayer(2);
        this.radius = 1;
    }

    public Bomb(int xUnit, int yUnit, Image img, int radius) {
        super(xUnit, yUnit, img);
        setLayer(2);
        this.radius = radius;
    }

    @Override
    public void update() {
        if (timeCounter++ == 100) {
            exploded();
        }
        img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, timeCounter, 60).getFxImage();

    }

    private void exploded() {
        Flame flame = new Flame(x, y);
        flame.setRadius(radius);
        flame.render_explosion();
        alive = false;
    }
}
