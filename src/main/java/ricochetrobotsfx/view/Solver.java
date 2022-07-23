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

    private final List<Pair<Integer, Integer>> dirConverter = new ArrayList<>(Arrays.asList(
            new Pair<>(0, -1), new Pair<>(-1, 0), new Pair<>(0, 1), new Pair<>(1, 0)));

    private void expand(Stack<Pair<Integer, Integer>> expandList, int[][] heuristic) {
        while (!expandList.isEmpty()) {
            Pair<Integer, Integer> pos = expandList.pop();
            for (int direction = 0; direction < 4; direction++) {
                Pair<Integer, Integer> dir = dirConverter.get(direction);
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

    // Return: List<Pair<colour, direction>> - <(blue, yellow, green, red, black), (left, up, right, down)>
    public List<Pair<Integer, Integer>> findSolution() {
        int goal = game.getGoal();
        Pair<Integer, Integer> goalPos = null;
        int goalColour = (int)Math.floor((goal - 1.0) / 4);
        if (goalColour > 3) goalColour = 4;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++)
                if (spec[i][j] == goal) {
                    goalPos = new Pair<>(i, j);
                    break;
                }
            if (goalPos != null) break;
        }

        return AStar(game.getRobotPos(), goalPos, goalColour);
    }

    static class State implements Comparable<State>{
        int weight;
        int[][] robotPos;
        List<Pair<Integer, Integer>> steps;

        State(int weight, int[][] robotPos, List<Pair<Integer, Integer>> steps) {
            this.weight = weight;
            this.robotPos = robotPos;
            this.steps = steps;
        }

        @Override
        public int compareTo(State o) {
            return Integer.compare(this.weight, o.weight);
        }
    }

    private void move(int[][] robotPos, int colour, int direction) {
        Pair<Integer, Integer> dir = dirConverter.get(direction);

        while (true) {
            // test wall collision
            if (touchWall(robotPos[colour][0], robotPos[colour][1], dir))
                return;

            robotPos[colour][0] += dir.key();
            robotPos[colour][1] += dir.value();

            // test robot collision
            for (int i = 0; i <= 4; i++)
                if (i != colour)
                    if (robotPos[colour][0] == robotPos[i][0] && robotPos[colour][1] == robotPos[i][1]) {
                        robotPos[colour][0] -= dir.key();
                        robotPos[colour][1] -= dir.value();
                        return;
                    }

            if (spec[robotPos[colour][0]][robotPos[colour][1]] >= 17 &&
                    spec[robotPos[colour][0]][robotPos[colour][1]] <= 24)
                dir = lensReflect(robotPos[colour][0], robotPos[colour][1], dir);
        }
    }

    // robotPos: blue, yellow, green, red, black
    // <(0 blue, 1 yellow, 2 green, 3 red, 4 black), (0 left, 1 up, 2 right, 3 down)>
    private List<Pair<Integer, Integer>> AStar(int[][] robotPos, Pair<Integer, Integer> goalPos, int goalColour) {
        Queue<State> pq = new PriorityQueue<>();
        pq.add(new State(
                heuristics.get(new Pair<>(robotPos[goalColour][0], robotPos[goalColour][1]))
                        [goalPos.key()][goalPos.value()],
                robotPos, new ArrayList<>()));

        while (!pq.isEmpty()) {
            State state = pq.poll();

            if (state.robotPos[goalColour][0] == goalPos.key() && state.robotPos[goalColour][1] == goalPos.value()) {
                pq.clear();
                return state.steps;
            }

            for (int colour = 0; colour <= 4; colour++)
                for (int direction = 0; direction < 4; direction++) {
                    if (!state.steps.isEmpty())
                        if (state.steps.get(state.steps.size()-1).key() == colour &&
                                state.steps.get(state.steps.size()-1).value() == (direction + 2) % 4)
                            continue;

                    int[][] robot = robotPos.clone();
                    move(robot, colour, direction);
                    List<Pair<Integer, Integer>> steps = new ArrayList<>(state.steps);
                    steps.add(new Pair<>(colour, direction));

                    if (colour != goalColour)
                        pq.add(new State(state.weight + 1, robot, steps));
                    else
                        pq.add(new State(
                                state.steps.size() + heuristics.get(new Pair<>(robot[colour][0], robot[colour][1]))
                                        [goalPos.key()][goalPos.value()],
                                robot, steps));
                }
        }

        return null;
    }
}
