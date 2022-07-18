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

    List<Pair<Integer, Integer>> ans;
    // Return: List<Pair<colour, direction>> - <(blue, yellow, green, red), (left, up, right, down)>
    public List<Pair<Integer, Integer>> findSolution() {
        int[][] board = game.getBoard(); // walls
        int[][] spec = game.getSpec(); // prism + goals
        int[][] robotPos = game.getRobotPos().clone(); // blue, yellow, green, red
        int goal = game.getGoal();

        ans = new ArrayList<Pair<Integer, Integer>>();
        int steps = 0;
        while (ans.size() == 0)
            backIDS(board, spec, robotPos, goal, steps++, new Pair<>(0, new Pair<>(0, 0)));
        return ans;
    }

    private void backIDS(int[][] board, int[][] spec, int[][] robotPos, int goal, int steps, Pair<Integer, Pair<Integer, Integer>> lastPos) {

        ans.add(new Pair<>(1, 2));
    }
}
