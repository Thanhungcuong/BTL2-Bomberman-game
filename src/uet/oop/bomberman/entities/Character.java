package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;

public abstract class Character extends Entity {
    protected int new_x = x;
    protected int new_y = y;
    protected int speed;
    protected int left = 0;
    protected int right = 0;
    protected int up = 0;
    protected int down = 0;

    public Character(int x, int y, Image image) {
        super(x, y, image);
        alive = true;
    }

    // tra ve gia tri ve toc do
    public int getSpeed() {
        return speed;
    }

    //dat gia tri cho toc do
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void Left() {
        new_x = x - speed;
    }

    public void Right() {
        new_x = x + speed;
    }

    public void Top() {
        new_y = y - speed;
    }

    public void Bottom() {
        new_y = y + speed;
    }

    //
    public void move() {
        y = new_y;
        x = new_x;
    }

    public void stop() {
        new_y = y;
        new_x = x;
    }

    public Rectangle getBounds() {
        return new Rectangle(new_x, new_y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }
}
