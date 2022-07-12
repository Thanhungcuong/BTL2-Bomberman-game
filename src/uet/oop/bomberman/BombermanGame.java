package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static uet.oop.bomberman.Sound.*;

public class BombermanGame extends Application {

    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    private GraphicsContext gc;
    private Canvas canvas;
    public static List<Enemy> enemyList = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Flame> flames = new ArrayList<>();
    public static int life = 3;
    public static boolean win = false;
    public static Bomber bomberman;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, Color.BLACK);

        Image icon = new Image("icon.png");

        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setTitle("Bomberman Game Nhóm 4");
        stage.setScene(scene);
        stage.show();
        nhac_nen.play();
        nhac_nen.seek(nhac_nen.getStartTime());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (life == 0) {
                    this.stop();
                    root.getChildren().remove(canvas);
                    Text text = new Text("GAME OVER!");
                    text.setX(WIDTH * Sprite.SCALED_SIZE / 2 - 150);
                    text.setY(HEIGHT * Sprite.SCALED_SIZE / 2);
                    text.setFont(Font.font(50));
                    text.setFill(Color.WHITE);
                    root.getChildren().add(text);
                    nhac_nen.stop();
                    game_over.play();
                    game_over.seek(game_over.getStartTime());
                }
                if (win) {
                    root.getChildren().remove(canvas);
                    Text text = new Text("YOU WIN!");
                    text.setX(WIDTH * Sprite.SCALED_SIZE / 2 - 100);
                    text.setY(HEIGHT * Sprite.SCALED_SIZE / 2);
                    text.setFont(Font.font(50));
                    text.setFill(Color.CYAN);
                    root.getChildren().add(text);
                    nhac_nen.stop();
                }
                else  {
                    render();
                    update();
                }
            }
        };
        createMap();

        timer.start();

        //cho bomberman di chuyen
        scene.setOnKeyPressed(event -> (bomberman).pressKeyCode(event.getCode()));
        scene.setOnKeyReleased(event -> (bomberman).releaseKeyCode(event.getCode()));

    }

    public void createMap() {
        File file = new File("res\\levels\\Level1.txt");
        try (FileReader fileReader = new FileReader(file)) {
            Scanner sc = new Scanner(fileReader);
            sc.nextLine();
            for (int i = 0; i < HEIGHT; i++) {
                String line = sc.nextLine();
                for (int j = 0; j < WIDTH; j++) {
                    switch (line.charAt(j)) {
                        case '#':
                            stillObjects.add(new Wall(j, i, Sprite.wall.getFxImage()));
                            break;
                        case '*':
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        case '1':
                            enemyList.add(new Balloom(j, i, Sprite.balloom_left1.getFxImage()));
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            break;
                        case '2':
                            enemyList.add(new Oneal(j, i, Sprite.oneal_left1.getFxImage()));
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            break;
                        case 'b':
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            stillObjects.add(new BombItem(j, i, Sprite.powerup_bombs.getFxImage()));
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        case 'f':
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            stillObjects.add(new FlameItem(j, i, Sprite.powerup_flames.getFxImage()));
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        case 's':
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            stillObjects.add(new SpeedItem(j, i, Sprite.powerup_speed.getFxImage()));
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        case 'x':
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            stillObjects.add(new Portal(j, i, Sprite.portal.getFxImage()));
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        case 'p':
                            bomberman = new Bomber(j, i, Sprite.player_right.getFxImage());
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            break;
                        case ' ':
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            break;
                    }
                }
            }
            stillObjects.sort(new Layer());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        for (int i = 0; i < enemyList.size(); i++)
            enemyList.get(i).update();
        for (int i = 0; i < flames.size(); i++)
            flames.get(i).update();
        bomberman.update();
        List<Bomb> bombs = bomberman.getBombList();
        bombs.forEach(Bomb::update);
        for (int i = 0; i < stillObjects.size(); i++)
            stillObjects.get(i).update();
        bomberman.collision();
        bomberman.CollisionWithFlame();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = stillObjects.size() - 1; i >= 0; i--) {
            stillObjects.get(i).render(gc);
        }
        enemyList.forEach(g -> g.render(gc));
        List<Bomb> bombs = bomberman.getBombList();
        for (Bomb bomb : bombs) {
            bomb.render(gc);
        }
        flames.forEach(g -> g.render(gc));
        bomberman.render(gc);
    }
}
