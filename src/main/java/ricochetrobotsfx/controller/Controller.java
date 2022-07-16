package ricochetrobotsfx.controller;

import ricochetrobotsfx.model.Board;
import ricochetrobotsfx.view.FeelingLucky;

public class Controller {
    Board board;
    FeelingLucky feelingLucky;

    public Controller(Board board) {
        this.board = board;
    }

    public void setFeelingLucky(FeelingLucky feelingLucky) {
        this.feelingLucky = feelingLucky;
    }

    public void newGame() {
        board.newRandomBoard();
        feelingLucky.updateBoard();
        feelingLucky.updateGoal();
    }
}
