package ricochetrobotsfx.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ricochetrobotsfx.model.Board;

import java.util.HashMap;
import java.util.Objects;

public class FeelingLucky {
    Board currentBoard;
    ImageView[][] boardTile = new ImageView[16][16];
    ImageView[][] specTile = new ImageView[16][16];
    ImageView[] robotTile = new ImageView[4];

    private final BorderPane pane = new BorderPane();
    private final Pane boardPane = new Pane();
    private final Scene scene = new Scene(pane, 1200, 840);

    private final int boardOffsetX = 20;
    private final int boardOffsetY = 30;
    private final int tileSize = 50;

    private final HashMap<Byte, Image> matchBoardImage = new HashMap<>();
    private final HashMap<Byte, Image> matchSpecImage = new HashMap<>();

    private Image getImageResource(String imageURL) {
        return new Image(
                Objects.requireNonNull(getClass().getResource(imageURL)).toExternalForm(),
                tileSize, tileSize, true, false
        );
    }

    private void buildMatchTable() {
        matchBoardImage.put((byte)0, getImageResource("/wall/blank.png"));
        matchBoardImage.put((byte)1, getImageResource("/wall/wallLeft.png"));
        matchBoardImage.put((byte)2, getImageResource("/wall/wallUp.png"));
        matchBoardImage.put((byte)3, getImageResource("/wall/wallRight.png"));
        matchBoardImage.put((byte)4, getImageResource("/wall/wallDown.png"));
        matchBoardImage.put((byte)5, getImageResource("/wall/wallLeftUp.png"));
        matchBoardImage.put((byte)6, getImageResource("/wall/wallUpRight.png"));
        matchBoardImage.put((byte)7, getImageResource("/wall/wallRightDown.png"));
        matchBoardImage.put((byte)8, getImageResource("/wall/wallDownLeft.png"));
        matchBoardImage.put((byte)9, getImageResource("/wall/wallLeftRight.png"));
        matchBoardImage.put((byte)10, getImageResource("/wall/wallUpDown.png"));

        matchSpecImage.put((byte)1, getImageResource("/star/starBlue.png"));
        matchSpecImage.put((byte)2, getImageResource("/gear/gearBlue.png"));
        matchSpecImage.put((byte)3, getImageResource("/ball/ballBlue.png"));
        matchSpecImage.put((byte)4, getImageResource("/cross/crossBlue.png"));
        matchSpecImage.put((byte)5, getImageResource("/star/starYellow.png"));
        matchSpecImage.put((byte)6, getImageResource("/gear/gearYellow.png"));
        matchSpecImage.put((byte)7, getImageResource("/ball/ballYellow.png"));
        matchSpecImage.put((byte)8, getImageResource("/cross/crossYellow.png"));
        matchSpecImage.put((byte)9, getImageResource("/star/starGreen.png"));
        matchSpecImage.put((byte)10, getImageResource("/gear/gearGreen.png"));
        matchSpecImage.put((byte)11, getImageResource("/ball/ballGreen.png"));
        matchSpecImage.put((byte)12, getImageResource("/cross/crossGreen.png"));
        matchSpecImage.put((byte)13, getImageResource("/star/starRed.png"));
        matchSpecImage.put((byte)14, getImageResource("/gear/gearRed.png"));
        matchSpecImage.put((byte)15, getImageResource("/ball/ballRed.png"));
        matchSpecImage.put((byte)16, getImageResource("/cross/crossRed.png"));
        matchSpecImage.put((byte)25, getImageResource("/universal/universal.png"));

        matchSpecImage.put((byte)17, getImageResource("/prism/prismLBlue.png"));
        matchSpecImage.put((byte)18, getImageResource("/prism/prismRBlue.png"));
        matchSpecImage.put((byte)19, getImageResource("/prism/prismLYellow.png"));
        matchSpecImage.put((byte)20, getImageResource("/prism/prismRYellow.png"));
        matchSpecImage.put((byte)21, getImageResource("/prism/prismLGreen.png"));
        matchSpecImage.put((byte)22, getImageResource("/prism/prismRGreen.png"));
        matchSpecImage.put((byte)23, getImageResource("/prism/prismLRed.png"));
        matchSpecImage.put((byte)24, getImageResource("/prism/prismRRed.png"));
    }

    private void setPos(ImageView obj, int x, int y) {
        obj.relocate(boardOffsetY + y * tileSize, boardOffsetX + x * tileSize);
    }

    public FeelingLucky() {
        buildMatchTable();
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                boardTile[i][j] = new ImageView();
                setPos(boardTile[i][j], i, j);
                boardPane.getChildren().add(boardTile[i][j]);

                specTile[i][j] = new ImageView();
                setPos(specTile[i][j], i, j);
                boardPane.getChildren().add(specTile[i][j]);
            }

        robotTile[0] = new ImageView();
        robotTile[0].setImage(getImageResource("/robot/robotBlue.png"));
        robotTile[1] = new ImageView();
        robotTile[1].setImage(getImageResource("/robot/robotYellow.png"));
        robotTile[2] = new ImageView();
        robotTile[2].setImage(getImageResource("/robot/robotGreen.png"));
        robotTile[3] = new ImageView();
        robotTile[3].setImage(getImageResource("/robot/robotRed.png"));
        boardPane.getChildren().addAll(robotTile[0], robotTile[1], robotTile[2], robotTile[3]);

        pane.setCenter(boardPane);

        Button btnNewGame = new Button("New Game");
        btnNewGame.setOnAction(event -> newGame());
        VBox rightBar = new VBox(btnNewGame);

        pane.setRight(rightBar);

        setupInputHandler();
        currentBoard = new Board();
        newGame();
    }

    private void setupInputHandler() {
        robotTile[0].setOnMouseClicked(event -> {System.out.println("hey!");});

        scene.addEventHandler(KeyEvent.KEY_PRESSED, FeelingLucky::handleKeyboardInput);
    }

    static void handleKeyboardInput(KeyEvent keyEvent) {
        KeyCode pressed = keyEvent.getCode();

        if (pressed.equals(KeyCode.UP) || pressed.equals(KeyCode.W)) {

        }

        if (pressed.equals(KeyCode.DOWN) || pressed.equals(KeyCode.S)) {

        }

        if (pressed.equals(KeyCode.LEFT) || pressed.equals(KeyCode.A)) {

        }

        if (pressed.equals(KeyCode.RIGHT) || pressed.equals(KeyCode.D)) {

        }
    }

    public void newGame() {
        currentBoard.newRandomBoard();
        updateBoard();
    }

    public void updateBoard() {
        int[][] robotPos = currentBoard.getRobotPos();
        setPos(robotTile[0], robotPos[0][0], robotPos[0][1]);
        setPos(robotTile[1], robotPos[1][0], robotPos[1][1]);
        setPos(robotTile[2], robotPos[2][0], robotPos[2][1]);
        setPos(robotTile[3], robotPos[3][0], robotPos[3][1]);

        byte[][] board = currentBoard.getBoard();
        byte[][] spec = currentBoard.getSpec();
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                boardTile[i][j].setImage(matchBoardImage.get(board[i][j]));
                specTile[i][j].setImage(matchSpecImage.get(spec[i][j]));
            }
    }

    public Scene getScene() {
        return scene;
    }
}
