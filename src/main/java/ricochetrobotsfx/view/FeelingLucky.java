package ricochetrobotsfx.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ricochetrobotsfx.Pair;
import ricochetrobotsfx.controller.Controller;
import ricochetrobotsfx.model.Board;
import ricochetrobotsfx.view.alerts.Popup;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FeelingLucky {
    Board board;
    Controller controller;

    ImageView[][] boardTile = new ImageView[16][16];
    ImageView[][] specTile = new ImageView[16][16];
    ImageView[] robotTile = new ImageView[5];

    private final BorderPane pane = new BorderPane();
    private final Pane boardPane = new Pane();
    private final Scene scene = new Scene(pane, 950, 666);

    private final int boardOffsetX = 10;
    private final int boardOffsetY = 15;
    private final int tileSize = 40;

    private final HashMap<Integer, Image> matchBoardImage = new HashMap<>();
    private final HashMap<Integer, Image> matchSpecImage = new HashMap<>();

    ImageView imgNextGoal = new ImageView();
    TextArea txtSolution = new TextArea();

    private Image getImageResource(String imageURL) {
        return new Image(
                Objects.requireNonNull(getClass().getResource(imageURL)).toExternalForm(),
                tileSize, tileSize, true, true
        );
    }

    private void buildMatchTable() {
        matchBoardImage.put(0, getImageResource("/wall/blank.png"));
        matchBoardImage.put(1, getImageResource("/wall/wallLeft.png"));
        matchBoardImage.put(2, getImageResource("/wall/wallUp.png"));
        matchBoardImage.put(3, getImageResource("/wall/wallRight.png"));
        matchBoardImage.put(4, getImageResource("/wall/wallDown.png"));
        matchBoardImage.put(5, getImageResource("/wall/wallLeftUp.png"));
        matchBoardImage.put(6, getImageResource("/wall/wallUpRight.png"));
        matchBoardImage.put(7, getImageResource("/wall/wallRightDown.png"));
        matchBoardImage.put(8, getImageResource("/wall/wallDownLeft.png"));
        matchBoardImage.put(9, getImageResource("/wall/wallLeftRight.png"));
        matchBoardImage.put(10, getImageResource("/wall/wallUpDown.png"));

        matchSpecImage.put(1, getImageResource("/star/starBlue.png"));
        matchSpecImage.put(2, getImageResource("/gear/gearBlue.png"));
        matchSpecImage.put(3, getImageResource("/ball/ballBlue.png"));
        matchSpecImage.put(4, getImageResource("/cross/crossBlue.png"));
        matchSpecImage.put(5, getImageResource("/star/starYellow.png"));
        matchSpecImage.put(6, getImageResource("/gear/gearYellow.png"));
        matchSpecImage.put(7, getImageResource("/ball/ballYellow.png"));
        matchSpecImage.put(8, getImageResource("/cross/crossYellow.png"));
        matchSpecImage.put(9, getImageResource("/star/starGreen.png"));
        matchSpecImage.put(10, getImageResource("/gear/gearGreen.png"));
        matchSpecImage.put(11, getImageResource("/ball/ballGreen.png"));
        matchSpecImage.put(12, getImageResource("/cross/crossGreen.png"));
        matchSpecImage.put(13, getImageResource("/star/starRed.png"));
        matchSpecImage.put(14, getImageResource("/gear/gearRed.png"));
        matchSpecImage.put(15, getImageResource("/ball/ballRed.png"));
        matchSpecImage.put(16, getImageResource("/cross/crossRed.png"));
        //matchSpecImage.put(25, getImageResource("/universal/universal.png"));

        matchSpecImage.put(17, getImageResource("/prism/prismLBlue.png"));
        matchSpecImage.put(18, getImageResource("/prism/prismRBlue.png"));
        matchSpecImage.put(19, getImageResource("/prism/prismLYellow.png"));
        matchSpecImage.put(20, getImageResource("/prism/prismRYellow.png"));
        matchSpecImage.put(21, getImageResource("/prism/prismLGreen.png"));
        matchSpecImage.put(22, getImageResource("/prism/prismRGreen.png"));
        matchSpecImage.put(23, getImageResource("/prism/prismLRed.png"));
        matchSpecImage.put(24, getImageResource("/prism/prismRRed.png"));
    }

    private void setPos(ImageView obj, int x, int y) {
        obj.relocate(boardOffsetY + y * tileSize, boardOffsetX + x * tileSize);
    }

    public FeelingLucky(Board board, Controller controller) {
        this.board = board;
        this.controller = controller;
        controller.setFeelingLucky(this);

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
        robotTile[4] = new ImageView();
        robotTile[4].setImage(getImageResource("/robot/robotSilver.png"));
        boardPane.getChildren().addAll(robotTile[0], robotTile[1], robotTile[2], robotTile[3], robotTile[4]);

        pane.setCenter(boardPane);

        Button btnNewGame = new Button("New Game");
        btnNewGame.setOnAction(event -> controller.newGame());
        Label lblNextGoal = new Label("Next Goal:");
        HBox hBoxNextGoal = new HBox(lblNextGoal, imgNextGoal);
        Button btnFindSolution = new Button("Find solution");
        btnFindSolution.setOnAction(event -> controller.findSolution());
        Label lblSolution = new Label("Solution:");
        txtSolution.setPrefWidth(200);
        HBox hBoxSolution = new HBox(lblSolution, txtSolution);

        VBox rightBar = new VBox(btnNewGame, hBoxNextGoal, btnFindSolution, hBoxSolution);
        rightBar.setSpacing(30);
        pane.setRight(rightBar);

        setupInputHandler();
        controller.newGame();
    }

    public void updateSolution() {
        List<Pair<Integer, Integer>> solution = board.getSolution();
        if (solution == null) {
            txtSolution.setText("Solution does not exist.");
            return;
        }

        StringBuilder sol = new StringBuilder();
        sol.append("Number of steps: ").append(solution.size()).append("\n");
        sol.append("----------\n");
        for (Pair<Integer, Integer> step: solution) {
            // blue, yellow, green, red
            if (step.key() == 0)
                sol.append("Blue:   ");
            else if (step.key() == 1)
                sol.append("Yellow: ");
            else if (step.key() == 2)
                sol.append("Green:  ");
            else if (step.key() == 3)
                sol.append("Red:    ");
            else if (step.key() == 4)
                sol.append("Black:  ");

            if (step.value() == 0)
                sol.append("left\n");
            else if (step.value() == 1)
                sol.append("up\n");
            else if (step.value() == 2)
                sol.append("right\n");
            else if (step.value() == 3)
                sol.append("down\n");
        }

        txtSolution.setText(sol.toString());
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

    public void updateBoard() {
        int[][] robotPos = board.getRobotPos();
        setPos(robotTile[0], robotPos[0][0], robotPos[0][1]);
        setPos(robotTile[1], robotPos[1][0], robotPos[1][1]);
        setPos(robotTile[2], robotPos[2][0], robotPos[2][1]);
        setPos(robotTile[3], robotPos[3][0], robotPos[3][1]);
        setPos(robotTile[4], robotPos[4][0], robotPos[4][1]);

        int[][] board = this.board.getBoard();
        int[][] spec = this.board.getSpec();
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                boardTile[i][j].setImage(matchBoardImage.get(board[i][j]));
                specTile[i][j].setImage(matchSpecImage.get(spec[i][j]));
            }
    }

    public void updateGoal() {
        int nextGoal = board.getNextGoal();
        if (nextGoal < 0)
            new Popup("Warning", "No more valid goals remaining.").show();
        else
            imgNextGoal.setImage(matchSpecImage.get(nextGoal));

        txtSolution.setText("");
    }

    public Scene getScene() {
        return scene;
    }
}
