package ricochetrobotsfx.view;

import ricochetrobotsfx.Pair;
import ricochetrobotsfx.model.Board;

import java.util.*;

public class Solver {
    Board game;
    int[][] board;
    int[][] spec;

    public Solver(Board game) {
        this.game = game;
    }

    private boolean touchWall(int x, int y, Pair<Integer, Integer> dir) {
        int t = board[x][y];

        if (dir.key() == 0 && dir.value() == -1)
            return t == 1 || t == 5 || t == 8 || t == 9;

        if (dir.key() == -1 && dir.value() == 0)
            return t == 2 || t == 5 || t == 6 || t == 10;

        if (dir.key() == 0 && dir.value() == 1)
            return t == 3 || t == 6 || t == 7 || t == 9;

        if (dir.key() == 1 && dir.value() == 0)
            return t == 4 || t == 7 || t == 8 || t == 10;

        return true;
    }
    
    private Pair<Integer, Integer> lensReflect(int x, int y, Pair<Integer, Integer> dir){
        if (spec[x][y] % 2 == 1)
            return new Pair<>(-dir.value(), -dir.key());
        return new Pair<>(dir.value(), dir.key());
    }

    private void traverse(Pair<Integer, Integer> pos, Pair<Integer, Integer> dir, int initial,
                          Stack<Pair<Integer, Integer>> expandList, int[][] heuristic) {
        int x = pos.key();
        int y = pos.value();
        while (!touchWall(x, y, dir)) {
            x += dir.key();
            y += dir.value();
            if (initial + 2 < heuristic[x][y]) {
                heuristic[x][y] = initial + 2;
                if (!expandList.contains(new Pair<>(x, y)) && heuristic[x][y] < 19)
                    expandList.push(new Pair<>(x, y));
            }
            if (spec[x][y] >= 17 && spec[x][y] <= 24)
                traverse(new Pair<>(x, y), lensReflect(x, y, dir), initial, expandList, heuristic);
        }

        if (touchWall(x, y, dir) && initial + 1 < heuristic[x][y]) {
            heuristic[x][y] = initial + 1;
            if (!expandList.contains(new Pair<>(x, y)) && heuristic[x][y] < 19)
                expandList.push(new Pair<>(x, y));
        }
    }

    private final HashMap<Pair<Integer, Integer>, int[][]> heuristics = new HashMap<>();

    private void expand(Stack<Pair<Integer, Integer>> expandList, int[][] heuristic) {
        while (!expandList.isEmpty()) {
            Pair<Integer, Integer> pos = expandList.pop();
            for (int direction = 1; direction <= 4; direction++) {
                Pair<Integer, Integer> dir;
                if (direction == 1) dir = new Pair<>(0, -1);
                else if (direction == 2) dir = new Pair<>(-1, 0);
                else if (direction == 3) dir = new Pair<>(0, 1);
                else dir = new Pair<>(1, 0);

                traverse(pos, dir, heuristic[pos.key()][pos.value()], expandList, heuristic);
            }
        }
    }

    public void preprocess() {
        board = game.getBoard(); // walls
        spec = game.getSpec(); // prism + goals
        Stack<Pair<Integer, Integer>> expandList = new Stack<>();

        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                int[][] heuristic = new int[16][16];
                for (int[] row: heuristic)
                    Arrays.fill(row, 1000000);
                expandList.clone();

                heuristic[i][j] = 0;
                expandList.push(new Pair<>(i, j));
                expand(expandList, heuristic);

                heuristics.put(new Pair<>(i, j), heuristic);
            }
    }

    List<Pair<Integer, Integer>> ans;
    // Return: List<Pair<colour, direction>> - <(blue, yellow, green, red), (left, up, right, down)>
    public List<Pair<Integer, Integer>> findSolution() {
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
