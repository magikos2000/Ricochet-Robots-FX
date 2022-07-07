package ricochetrobotsfx.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private final BorderPane pane = new BorderPane();
    private final Pane boardPane = new Pane();
    private final Scene scene = new Scene(pane, 1280, 960);

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
        // TODO
        matchBoardImage.put((byte)9, getImageResource("/wall/blank.png"));
        matchBoardImage.put((byte)10, getImageResource("/wall/blank.png"));

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

    public FeelingLucky() {
        buildMatchTable();

        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                boardTile[i][j] = new ImageView();
                boardTile[i][j].relocate(boardOffsetX + j * tileSize, boardOffsetY + i * tileSize);
                boardPane.getChildren().add(boardTile[i][j]);

                specTile[i][j] = new ImageView();
                specTile[i][j].relocate(boardOffsetX + j * tileSize, boardOffsetY + i * tileSize);
                boardPane.getChildren().add(specTile[i][j]);
            }
        pane.setCenter(boardPane);

        Button btnNewGame = new Button("New Game");
        btnNewGame.setOnAction(event -> newGame());
        VBox rightBar = new VBox(btnNewGame);
        pane.setRight(rightBar);

        newGame();
    }

    public void newGame() {
        currentBoard = new Board();
        updateBoard();
    }

    public void updateBoard() {
        byte[][] board = currentBoard.getBoard();
        byte[][] spec = currentBoard.getSpec();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }

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
