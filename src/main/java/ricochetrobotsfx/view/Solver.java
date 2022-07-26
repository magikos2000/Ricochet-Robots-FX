package ricochetrobotsfx.view;

import ricochetrobotsfx.Pair;
import ricochetrobotsfx.model.Board;

import java.util.*;

public class Solver {
    Board game;
    byte[][] board;
    byte[][] spec;

    public Solver(Board game) {
        this.game = game;
    }

    private boolean touchWall(byte x, byte y, Pair<Byte, Byte> dir) {
        byte t = board[x][y];

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
    
    private Pair<Byte, Byte> lensReflect(byte x, byte y, Pair<Byte, Byte> dir){
        if (spec[x][y] % 2 == 1)
            return new Pair<>((byte)-dir.value(), (byte)-dir.key());
        return new Pair<>(dir.value(), dir.key());
    }

    private void traverse(Pair<Byte, Byte> pos, Pair<Byte, Byte> dir, byte initial,
                          Stack<Pair<Byte, Byte>> expandList, byte[][] heuristic) {
        byte x = pos.key();
        byte y = pos.value();
        while (!touchWall(x, y, dir)) {
            x += dir.key();
            y += dir.value();
            if (initial + 2 < heuristic[x][y]) {
                heuristic[x][y] = (byte)(initial + 2);
                if (!expandList.contains(new Pair<>(x, y)) && heuristic[x][y] < 19)
                    expandList.push(new Pair<>(x, y));
            }
            if (spec[x][y] >= 17 && spec[x][y] <= 24)
                traverse(new Pair<>(x, y), lensReflect(x, y, dir), initial, expandList, heuristic);
        }

        if (touchWall(x, y, dir) && initial + 1 < heuristic[x][y]) {
            heuristic[x][y] = (byte)(initial + 1);
            if (!expandList.contains(new Pair<>(x, y)) && heuristic[x][y] < 19)
                expandList.push(new Pair<>(x, y));
        }
    }

    private final HashMap<Pair<Byte, Byte>, byte[][]> heuristics = new HashMap<>();

    private final List<Pair<Byte, Byte>> dirConverter = new ArrayList<>(Arrays.asList(
            new Pair<>((byte)0, (byte)-1),
            new Pair<>((byte)-1, (byte)0),
            new Pair<>((byte)0, (byte)1),
            new Pair<>((byte)1, (byte)0)));

    private void expand(Stack<Pair<Byte, Byte>> expandList, byte[][] heuristic) {
        while (!expandList.isEmpty()) {
            Pair<Byte, Byte> pos = expandList.pop();
            for (byte direction = 0; direction < 4; direction++) {
                Pair<Byte, Byte> dir = dirConverter.get(direction);
                traverse(pos, dir, heuristic[pos.key()][pos.value()], expandList, heuristic);
            }
        }
    }

    public void preprocess() {
        heuristics.clear();
        memorization.clear();

        board = game.getBoard(); // walls
        spec = game.getSpec(); // prism + goals
        Stack<Pair<Byte, Byte>> expandList = new Stack<>();

        for (byte i = 0; i < 16; i++)
            for (byte j = 0; j < 16; j++) {
                byte[][] heuristic = new byte[16][16];
                for (byte[] row: heuristic)
                    Arrays.fill(row, (byte)127);
                expandList.clone();

                heuristic[i][j] = 0;
                expandList.push(new Pair<>(i, j));
                expand(expandList, heuristic);

                heuristics.put(new Pair<>(i, j), heuristic);
            }
    }

    // Return: List<Pair<colour, direction>> - <(blue, yellow, green, red, black), (left, up, right, down)>
    public List<Pair<Byte, Byte>> findSolution() {
        byte goal = game.getGoal();
        Pair<Byte, Byte> goalPos = null;
        byte goalColour = (byte)Math.floor((goal - 1.0) / 4);
        if (goalColour > 3) goalColour = 4;
        for (byte i = 0; i < 16; i++) {
            for (byte j = 0; j < 16; j++)
                if (spec[i][j] == goal) {
                    goalPos = new Pair<>(i, j);
                    break;
                }
            if (goalPos != null) break;
        }

        assert goalPos != null;
        return AStar(game.getRobotPos().clone(), goalPos, goalColour);
    }

    static class State implements Comparable<State>{
        double weight;
        byte[][] robotPos;
        List<Pair<Byte, Byte>> steps;

        State(double weight, byte[][] robotPos, List<Pair<Byte, Byte>> steps) {
            this.weight = weight;
            this.robotPos = robotPos;
            this.steps = steps;
        }

        @Override
        public int compareTo(State o) {
            if (this.weight != o.weight)
                return Double.compare(this.weight, o.weight);
            return Byte.compare((byte)this.steps.size(), (byte)o.steps.size());
        }
    }

    private void move(byte[][] robotPos, byte colour, byte direction) {
        Pair<Byte, Byte> dir = dirConverter.get(direction);

        while (true) {
            // test wall collision
            if (touchWall(robotPos[colour][0], robotPos[colour][1], dir))
                return;

            robotPos[colour][0] += dir.key();
            robotPos[colour][1] += dir.value();

            // test robot collision
            for (byte i = 0; i <= 4; i++)
                if (i != colour)
                    if (robotPos[colour][0] == robotPos[i][0] && robotPos[colour][1] == robotPos[i][1]) {
                        robotPos[colour][0] -= dir.key();
                        robotPos[colour][1] -= dir.value();
                        return;
                    }

            if (spec[robotPos[colour][0]][robotPos[colour][1]] >= 17 &&
                    spec[robotPos[colour][0]][robotPos[colour][1]] <= 24)
                if (colour != Math.floor((spec[robotPos[colour][0]][robotPos[colour][1]] - 17) / 2.0))
                    dir = lensReflect(robotPos[colour][0], robotPos[colour][1], dir);
        }
    }

    private final Map<byte[][], Byte> memorization = new HashMap<>();

    // robotPos: blue, yellow, green, red, black
    // <(0 blue, 1 yellow, 2 green, 3 red, 4 black), (0 left, 1 up, 2 right, 3 down)>
    private List<Pair<Byte, Byte>> AStar(byte[][] robotPos, Pair<Byte, Byte> goalPos, byte goalColour) {
        Queue<State> pq = new PriorityQueue<>();
        pq.add(new State(
                heuristics.get(new Pair<>(robotPos[goalColour][0], robotPos[goalColour][1]))
                        [goalPos.key()][goalPos.value()],
                robotPos, new ArrayList<>()));
        memorization.put(robotPos, (byte)0);

        while (!pq.isEmpty()) {
            State state = pq.poll();

            if (state.robotPos[goalColour][0] == goalPos.key() && state.robotPos[goalColour][1] == goalPos.value()) {
                pq.clear();
                return state.steps;
            }

            for (byte colour = 0; colour <= 4; colour++)
                for (byte direction = 0; direction < 4; direction++) {
                    if (!state.steps.isEmpty())
                        if (state.steps.get(state.steps.size()-1).key() == colour &&
                                state.steps.get(state.steps.size()-1).value() == (direction + 2) % 4)
                            continue;
                    if (touchWall(state.robotPos[colour][0], state.robotPos[colour][1], dirConverter.get(direction)))
                        continue;

                    byte[][] robot = new byte[5][2];
                    for (byte i = 0 ;i <= 4; i++) {
                        robot[i][0] = state.robotPos[i][0];
                        robot[i][1] = state.robotPos[i][1];
                    }

                    move(robot, colour, direction);

                    List<Pair<Byte, Byte>> steps = new ArrayList<>(state.steps);
                    steps.add(new Pair<>(colour, direction));

                    if (!memorization.containsKey(robot) || memorization.get(robot) > steps.size()) {
                        if (colour != goalColour)
                            pq.add(new State(state.weight + 1, robot, steps));
                        else
                            pq.add(new State(
                                    steps.size() - 0.1 + heuristics.get(new Pair<>(robot[colour][0], robot[colour][1]))
                                            [goalPos.key()][goalPos.value()],
                                    robot, steps));
                        memorization.put(robot, (byte)steps.size());
                    }
                }
        }

        return null;
    }
}
