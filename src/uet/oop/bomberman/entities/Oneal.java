package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.BombermanGame.*;

public class Oneal extends Enemy {
    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setLayer(1);
        setSpeed(1);
        direction = calculateDirection();
    }

    int n = 0;

    @Override
    public void update() {
        n++;
        if (alive) {
            if (direction == 3) Left();
            if (direction == 1) Right();
            if (direction == 0) Top();
            if (direction == 2) Bottom();
            if (n == 32) {
                direction = calculateDirection();
                n = 0;
            }
            if (!bomberman.isAlive()) bomberman.update();
        } else if (animated < 30) {
            animated++;
            img = Sprite.oneal_dead.getFxImage();
        } else {
            enemyList.remove(this);
        }
    }

    public void Left() {
        super.Left();
        img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, left++, 45).getFxImage();
    }

    public void Right() {
        super.Right();
        img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, right++, 45).getFxImage();
    }

    public void Top() {
        super.Top();
        img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, up++, 45).getFxImage();
    }

    public void Bottom() {
        super.Bottom();
        img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, down++, 45).getFxImage();
    }

    @Override
    public void stop() {
        super.stop();
        direction = random.nextInt(4);
    }

    protected int calculateColDirection() {
        if (bomberman.x < this.x)
            return 3;
        else if (bomberman.x > this.x)
            return 1;
        return -1;
    }

    protected int calculateRowDirection() {
        if (bomberman.y < this.y)
            return 0;
        else if (bomberman.y > this.y)
            return 2;
        return -1;
    }

    public int calculateDirection() {
        int vertical = random.nextInt(2);
        if (vertical == 1) {
            if (calculateColDirection() != -1)
                return calculateColDirection();
            else
                return calculateRowDirection();
        } else {
            if (calculateRowDirection() != -1)
                return calculateRowDirection();
            else
                return calculateColDirection();
        }
    }
}
