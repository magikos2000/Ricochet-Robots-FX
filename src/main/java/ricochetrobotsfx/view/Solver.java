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

        ans = new ArrayList<>();
        int steps = 0;
        while (true) {
            int[][] robot = robotPos.clone();
            if (backDFS(board, spec, robot, goal, steps)) break;
            steps++;
        }
        return ans;
    }

    private boolean backDFS(int[][] board, int[][] spec, int[][] robot, int goal, int steps) {
        // test if the goal is reached


        // move up

        // move down

        // move left

        // move right

        ans.add(new Pair<>(1, 2));
        return false;
    }
}
