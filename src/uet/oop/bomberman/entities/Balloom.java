package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.BombermanGame.enemyList;


public class Balloom extends Enemy {
    public Balloom(int x, int y, Image image) {
        super(x, y, image);
        setLayer(1);
        setSpeed(1);
        direction = random.nextInt(4);
    }

    @Override
    public void update() {
        if (alive) {
            if (direction == 0) Left();
            if (direction == 1) Right();
            if (direction == 2) Top();
            if (direction == 3) Bottom();
        } else if(animated < 30){
            animated ++;
            img = Sprite.balloom_dead.getFxImage();
        } else {
            enemyList.remove(this);
        }
    }

    public void Left() {
        super.Left();
        img = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, left++, 45).getFxImage();
    }

    public void Right() {
        super.Right();
        img = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, right++, 45).getFxImage();
    }

    public void Top() {
        super.Top();
        img = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, up++, 45).getFxImage();

    }

    public void Bottom() {
        super.Bottom();
        img = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, down++, 45).getFxImage();
    }

    @Override
    public void stop() {
        super.stop();
        direction = random.nextInt(4);
    }
}
