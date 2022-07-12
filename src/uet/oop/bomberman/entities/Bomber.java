package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static uet.oop.bomberman.BombermanGame.*;
import static uet.oop.bomberman.Sound.*;
public class Bomber extends Character {
    private int numberofBomb;
    private KeyCode keyCode = null; //keyCode = huong left right top down
    private List<Bomb> bombList = new ArrayList<>();
    private int radius;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
        setSpeed(2);
        setLayer(1);
        setNumberofBomb(1);
        setRadius(1);
        setNumberofBomb(1);
    }

    public void setNumberofBomb(int numberofBomb) {
        this.numberofBomb = numberofBomb;
    }

    public int getNumberofBomb() {
        return numberofBomb;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    @Override
    public void update() {
        if (this.keyCode == KeyCode.LEFT) {
            Left();
        }
        if (this.keyCode == KeyCode.RIGHT) {
            Right();
        }
        if (this.keyCode == KeyCode.UP) {
            Top();
        }
        if (this.keyCode == KeyCode.DOWN) {
            Bottom();
        }
        if (this.keyCode == KeyCode.SPACE) {
            placeBomb();
        }
        for (int i = 0; i < bombList.size(); i++) {
            Bomb bomb = bombList.get(i);
            if (!bomb.isAlive()) {
                bombList.remove(bomb);
                numberofBomb++;
            }
        }
        if (!alive) {
            animated++;
            die();
        }

    }

    private void placeBomb() {
        if (numberofBomb > 0) {
            int xB = (int) Math.round((x) / (double) Sprite.SCALED_SIZE);
            int yB = (int) Math.round((y) / (double) Sprite.SCALED_SIZE);
            for (Bomb bomb : bombList) {
                if (xB * Sprite.SCALED_SIZE == bomb.getX() && yB * Sprite.SCALED_SIZE == bomb.getY()) return;
            }
            Bomb abomb = new Bomb(xB, yB, Sprite.bomb.getFxImage(), radius);
            bombList.add(abomb);
            numberofBomb--;
            dat_bom.play();
            dat_bom.seek(dat_bom.getStartTime());
        }
    }

    public void pressKeyCode(KeyCode keyCode) {
        if (keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT || keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
            this.keyCode = keyCode;
        } else if (keyCode == KeyCode.SPACE) {
            this.keyCode = keyCode;
        }
    }

    public void releaseKeyCode(KeyCode keyCode) {
        if (this.keyCode == keyCode) {
            if (this.keyCode == KeyCode.LEFT) {
                img = Sprite.player_left.getFxImage();
            }
            if (this.keyCode == KeyCode.RIGHT) {
                img = Sprite.player_right.getFxImage();
            }
            if (this.keyCode == KeyCode.UP) {
                img = Sprite.player_up.getFxImage();
            }
            if (this.keyCode == KeyCode.DOWN) {
                img = Sprite.player_down.getFxImage();
            }
            this.keyCode = null;
        }
    }

    @Override
    public void Left() {
        super.Left();
        img = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, left++, 45).getFxImage();
    }

    public void Right() {
        super.Right();
        img = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, right++, 45).getFxImage();
    }

    public void Top() {
        super.Top();
        img = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, up++, 45).getFxImage();
    }

    public void Bottom() {
        super.Bottom();
        img = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, down++, 45).getFxImage();
    }

    public void die() {
        if (animated == 45) life--;
        if (animated <= 60) img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animated, 45).getFxImage();
    }

    public Rectangle getBounds() {
        return new Rectangle(new_x + 2, new_y + 5, Sprite.SCALED_SIZE - 10, Sprite.SCALED_SIZE * 3 / 4);
    }

    public List<Bomb> getBombList() {
        return bombList;
    }

    public void collision() {
        Rectangle r1 = getBounds();

        //Bomberman vs stillObjects
        for (Entity stillObject : stillObjects) {
            Rectangle r2 = stillObject.getBounds();
            if (r1.intersects(r2)) {
                if (bomberman.getLayer() == stillObject.getLayer() && stillObject instanceof Item) {
                    an_item.play();
                    an_item.seek(an_item.getStartTime());
                    if(stillObject instanceof BombItem) {
                        numberofBomb++;
                        stillObjects.remove(stillObject);
                    } else if(stillObject instanceof SpeedItem) {
                        speed++;
                        stillObjects.remove(stillObject);
                    } else if(stillObject instanceof FlameItem) {
                        radius++;
                        stillObjects.remove(stillObject);
                    }
                    bomberman.stop();
                } else if(bomberman.getLayer() >= stillObject.getLayer()) {
                    bomberman.move();
                    if(stillObject instanceof Portal) {
                        if(enemyList.size() == 0) {
                            win = true;
                            you_win.play();
                            you_win.seek(you_win.getStartTime());
                        }
                    }
                }
                else {
                    bomberman.stop();
                }
                break;
            }
        }

        //Enemies vs StillObjects
        for (Enemy enemy : enemyList) {
            Rectangle r2 = enemy.getBounds();
            for (Entity stillObject : stillObjects) {
                Rectangle r3 = stillObject.getBounds();
                if (r2.intersects(r3)) {
                    if (enemy.getLayer() >= stillObject.getLayer()) {
                        enemy.move();
                    } else {
                        enemy.stop();
                    }
                    break;
                }
            }
        }

        //Bomber vs Enemies
        for (Enemy enemy : enemyList) {
            Rectangle r2 = enemy.getBounds();
            if (r1.intersects(r2)) {
                alive = false;
                nv_chet.play();
                nv_chet.seek(nv_chet.getStartTime());
                numberofBomb = 1;
                radius = 1;
                speed = 2;
                if (life >= 0) {
                    Timer count = new Timer();
                    count.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
                            count.cancel();
                        }
                    }, 500, 1);
                }
            }
        }

        //Enemies vs Bombs
        for (Enemy enemy : enemyList) {
            Rectangle r2 = enemy.getBounds();
            for (Bomb bomb : bombList) {
                Rectangle r3 = bomb.getBounds();
                if (r2.intersects(r3)) {
                    enemy.stop();
                    break;
                }
            }
        }
    }

    public void CollisionWithFlame() {
        for (int i = 0; i < flames.size(); i++) {
            Rectangle r1 = flames.get(i).getBounds();
            for (int j = 0; j < stillObjects.size(); j++) {
                Rectangle r2 = stillObjects.get(j).getBounds();
                if (r1.intersects(r2) && !(stillObjects.get(j) instanceof Item))
                    stillObjects.get(j).setAlive(false);
            }
            for (int j = 0; j < enemyList.size(); j++) {
                Rectangle r2 = enemyList.get(j).getBounds();
                if (r1.intersects(r2)) {
                    enemyList.get(j).setAlive(false);
                    quai_chet.play();
                    quai_chet.seek(quai_chet.getStartTime());
                }
            }
            Rectangle r2 = bomberman.getBounds();
            if (r1.intersects(r2)) {
                alive = false;
                nv_chet.play();
                nv_chet.seek(nv_chet.getStartTime());
                numberofBomb = 1;
                radius = 1;
                speed = 2;
                if (life >= 0) {
                    Timer count = new Timer();
                    count.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
                            count.cancel();
                        }
                    }, 800, 1);
                }
            }
        }
    }
}
