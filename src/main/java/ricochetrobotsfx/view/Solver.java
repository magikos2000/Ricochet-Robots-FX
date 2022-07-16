package ricochetrobotsfx.view;

import ricochetrobotsfx.Pair;
import ricochetrobotsfx.model.Board;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    Board game;

    public Solver(Board game) {
        this.game = game;
    }

    List<Pair<Byte, Integer>> ans;
    // Return: List<Pair<colour, direction>> - <(blue, yellow, green, red), (left, up, right, down)>
    public List<Pair<Byte, Integer>> findSolution() {
        byte[][] board = game.getBoard(); // walls
        byte[][] spec = game.getSpec(); // prism + goals
        int[][] robotPos = game.getRobotPos().clone(); // blue, yellow, green, red
        byte goal = game.getGoal();

        ans = null;
        int steps = 0;
        while (ans == null)
            backIDS(board, spec, robotPos, goal, steps++,
                new Pair<Byte, Pair<Integer, Integer>>((byte)0, new Pair<Integer, Integer>(0, 0)));
        return ans;
    }

    private void backIDS(byte[][] board, byte[][] spec, int[][] robotPos, byte goal, int steps, Pair<Byte, Pair<Integer, Integer>> lastPos) {
        ans = new ArrayList<Pair<Byte, Integer>>();
        ans.add(new Pair<Byte, Integer>((byte)1, 2));
    }
}
