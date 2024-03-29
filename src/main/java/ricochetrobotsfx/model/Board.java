package ricochetrobotsfx.model;

import ricochetrobotsfx.Pair;
import ricochetrobotsfx.view.Solver;

import java.util.List;
import java.util.Random;

public class Board {
    private final byte[][] board = new byte[16][16]; // walls
    private final byte[][] spec = new byte[16][16]; // prism + goals
    private final byte[][] robotPos = new byte[5][2]; // blue, yellow, green, red
    // 1~16 - goals: (blue, yellow, green, red) * (star, gear, ball, cross)
    private byte goalNo = 0;
    private final byte[] goalsRemained = new byte[16];

    Solver solver = new Solver(this);

    /*
    0 - blank;
    1~4 - one-side wall: left, up, right, down;
    5~8 - two-side wall: left+up, up+right, right+down, down+left;
    9~10 - opposite wall: left+right, up+down;
     */
    private final static byte[][][] boardTemplates = { // 16 temp * 8 col * 8 row
            // 1A
            {{5,6,5,10,2,2,2,2},{1,0,3,5,0,0,0,0},{1,0,0,0,0,0,0,0},{1,0,0,0,0,0,7,1},
                    {9,8,0,0,0,0,2,0},{1,2,0,0,4,0,0,0},{8,0,0,0,6,1,0,4},{5,0,0,0,0,0,3,5}},
            // 1B
            {{5,2,2,2,2,6,5,2},{1,0,0,0,0,0,0,0},{1,4,0,0,0,0,0,0},{9,5,0,0,0,0,0,0},
                    {1,0,0,0,0,0,7,1},{8,0,4,0,0,0,2,0},{5,0,6,8,0,0,0,4},{1,0,0,2,0,0,3,5}},
            // 5A
            {{5,6,5,2,10,2,2,2},{1,4,0,3,5,0,0,0},{1,6,1,0,0,0,0,0},{1,0,0,0,0,0,7,1},
                    {1,0,0,0,0,0,2,0},{8,0,0,0,0,0,0,0},{5,0,3,8,0,0,0,4},{1,0,0,2,0,0,3,5}},
            // 5B
            {{5,2,2,2,6,5,2,2},{1,4,0,0,0,0,7,1},{9,5,0,0,0,0,2,0},{1,0,0,0,0,0,0,0},
                    {1,0,0,0,0,0,4,0},{8,0,0,0,0,0,6,1},{5,0,3,8,0,0,0,4},{1,0,0,2,0,0,3,5}},
            // 2A
            {{5,2,2,6,5,2,2,2},{1,0,0,0,0,3,8,0},{1,4,0,0,0,0,2,0},{1,6,1,0,0,4,0,0},
                    {1,0,0,0,3,5,0,0},{1,0,7,1,0,0,0,7},{8,0,2,0,0,0,0,10},{5,0,0,0,0,0,3,5}},
            // 2B
            {{5,2,2,2,6,5,2,2},{1,0,7,1,0,0,0,0},{1,0,2,0,0,0,0,0},{9,8,0,0,0,0,4,0},
                    {8,2,0,0,0,3,5,0},{5,0,0,0,0,4,0,0},{1,0,0,0,0,6,1,4},{1,0,0,7,1,0,3,5}},
            // 7A
            {{5,2,6,5,2,2,2,2},{1,0,0,0,3,8,0,0},{1,0,0,0,0,2,0,7},{8,0,0,0,0,0,0,2},
                    {5,0,0,7,1,0,4,0},{1,4,0,2,0,3,5,0},{1,6,1,0,0,0,0,4},{1,0,0,0,0,0,3,5}},
            // 7B
            {{5,2,2,2,6,5,2,2},{1,0,0,0,0,0,4,0},{1,0,4,0,0,3,5,0},{1,0,6,8,0,0,0,0},
                    {1,0,0,2,0,0,0,0},{1,7,1,0,0,0,0,0},{8,2,0,0,0,0,0,4},{5,0,0,0,0,7,9,5}},
            // 3A
            {{5,2,2,6,5,2,2,2},{1,0,0,0,0,7,1,0},{9,8,0,0,0,2,0,0},{8,2,0,0,0,0,4,0},
                    {5,0,0,0,0,3,5,0},{1,0,4,0,0,0,0,0},{1,0,6,1,0,0,0,4},{1,0,0,0,0,0,3,5}},
            // 3B
            {{5,2,10,2,6,5,2,2},{1,3,5,0,0,0,0,0},{1,0,0,0,0,0,0,0},{1,0,0,0,0,3,8,0},
                    {8,0,0,0,4,0,2,0},{5,0,0,0,6,1,0,0},{1,7,1,0,0,0,0,4},{1,2,0,0,0,0,3,5}},
            // 8A
            {{5,2,6,5,2,2,2,2},{1,0,0,0,0,0,4,0},{1,0,0,0,0,7,5,0},{1,0,0,0,0,2,0,0},
                    {1,0,0,0,0,0,0,0},{9,8,0,0,0,0,0,0},{8,2,0,0,4,0,0,4},{5,0,0,0,6,1,3,5}},
            // 8B
            {{5,2,2,2,2,6,5,2},{1,0,0,4,0,0,0,0},{1,0,3,5,0,0,0,0},{8,0,4,0,3,8,0,0},
                    {5,0,6,1,0,2,0,0},{1,0,0,0,7,1,0,0},{1,0,0,0,2,0,0,4},{1,0,0,0,0,0,3,5}},
            // 4A
            {{5,2,2,6,5,2,2,2},{1,0,0,0,0,0,0,0},{1,0,0,0,0,7,1,0},{1,0,4,0,0,2,0,0},
                    {8,0,6,1,0,0,0,0},{5,4,0,0,0,0,3,8},{9,5,0,0,0,0,0,10},{1,0,0,0,0,0,3,5}},
            // 4B
            {{5,2,2,6,5,2,2,2},{9,8,0,0,0,0,4,0},{1,2,0,0,0,0,6,1},{1,0,0,0,0,0,0,0},
                    {1,0,7,1,0,0,0,4},{8,0,2,0,0,0,3,5},{5,0,0,0,0,0,0,4},{1,0,0,0,0,0,3,5}},
            // 6A
            {{5,6,5,2,10,2,2,2},{1,0,0,0,6,1,0,0},{1,0,0,0,0,0,0,0},{9,8,0,0,0,0,0,0},
                    {1,2,0,0,0,4,0,0},{8,0,0,0,3,5,0,0},{5,0,0,7,1,0,0,4},{1,0,0,2,0,0,3,5}},
            // 6B
            {{5,2,2,2,6,5,2,2},{1,0,0,0,0,0,0,0},{8,0,0,0,0,0,7,1},{5,0,4,0,0,0,2,0},
                    {1,0,6,8,0,0,0,0},{1,0,0,2,0,4,0,0},{1,0,0,0,3,5,0,4},{1,0,0,0,0,0,3,5}}
    };

    /*
    1~16 - goals: (blue, yellow, green, red) * (star, gear, ball, cross)
        blue: 1~4
        yellow: 5~8
        green: 9~12
        red: 13~16
    17~24 - prism: (blue, yellow, green, red) * (/, \)
        blue: 17~18
        yellow: 19~20
        green: 21~22
        red: 23~24
     */
    private final static byte[][][] specTemplates = { // 16 temp * 8 col * 8 row
            // 1A
            {{0,0,0,0,0,0,0,0},{0,0,0,10,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,8,0},
                    {0,15,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,1,0,0,0},{0,0,0,0,0,0,0,0}},
            // 1B
            {{0,0,0,0,0,0,0,0},{0,0,0,0,21,0,0,0},{0,0,0,0,0,0,0,0},{0,15,0,0,0,0,0,0},
                    {0,0,0,0,0,0,8,0},{0,0,0,0,0,0,0,0},{0,0,10,1,0,0,0,0},{0,0,0,0,0,20,0,0}},
            // 5A
            {{0,0,0,0,0,0,0,0},{0,0,0,0,15,0,0,0},{0,10,0,0,0,0,0,0},{0,0,0,0,0,0,8,0},
                    {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,1,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 5B
            {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,8,0},{0,10,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,1,0},{0,0,0,15,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 2A
            {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,3,0},{0,0,0,0,0,0,0,0},{0,6,0,0,0,0,0,0},
            //        {0,0,0,0,0,9,0,0},{0,0,16,0,0,0,0,25},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
                    {0,0,0,0,0,9,0,0},{0,0,16,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 2B
            {{0,0,0,0,0,0,0,0},{0,0,16,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,9,0,0,0,0,0,0},
            //        {0,0,0,0,0,0,6,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,3,0,0},{0,0,0,25,0,0,0,0}},
                    {0,0,0,0,0,0,6,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,3,0,0},{0,0,0,0,0,0,0,0}},
            // 7A
            {{0,0,0,0,0,0,0,0},{0,0,0,0,0,3,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
            //{{0,0,0,0,0,0,0,0},{0,0,0,0,0,3,0,0},{0,0,0,0,0,0,0,25},{0,0,0,0,0,0,0,0},
                    {0,0,0,16,0,0,0,0},{0,0,0,0,0,0,9,0},{0,6,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 7B
            {{0,0,0,0,0,0,0,0},{0,0,24,0,0,0,0,0},{0,0,0,0,0,0,6,0},{0,0,3,9,0,0,0,0},
                    {0,0,0,0,0,0,0,0},{0,16,0,0,0,0,0,0},{0,0,0,22,0,0,0,0},{0,0,0,0,0,0,0,0}},
            //        {0,0,0,0,0,0,0,0},{0,16,0,0,0,0,0,0},{0,0,0,22,0,0,0,0},{0,0,0,0,0,25,0,0}},
            // 3A
            {{0,0,0,0,0,0,0,0},{0,0,0,0,0,12,0,0},{0,13,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,7,0},{0,0,0,0,0,0,0,0},{0,0,2,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 3B
            {{0,0,0,0,0,0,0,0},{0,0,7,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,2,0},
                    {0,0,0,0,0,0,0,0},{0,0,0,0,13,0,0,0},{0,12,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 8A
            {{0,0,0,0,0,0,0,0},{0,0,17,0,0,0,0,0},{0,0,0,0,0,12,7,0},{0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,24},{0,13,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,2,0,0,0}},
            // 8B
            {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,7,0,0,0,0},{0,0,0,0,0,2,0,0},
                    {0,0,13,0,0,0,0,0},{0,0,0,0,12,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 4A
            {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,4,0,0},{0,0,0,0,0,0,0,0},
                    {0,0,11,0,0,0,0,0},{0,0,0,0,0,0,0,14},{0,5,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 4B
            {{0,0,0,0,0,0,0,0},{0,14,0,0,0,0,0,0},{0,0,0,0,0,0,11,0},{0,0,0,0,0,0,0,0},
                    {0,0,4,0,0,0,0,0},{0,0,0,0,0,0,0,5},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 6A
            {{0,0,0,0,0,0,0,0},{0,0,0,0,11,0,0,0},{0,0,0,0,0,0,0,0},{0,14,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},{0,0,0,0,0,5,0,0},{0,0,0,4,0,0,0,0},{0,0,0,0,0,0,0,0}},
            // 6B
            {{0,0,0,0,0,0,0,0},{0,0,0,0,18,0,0,0},{0,0,0,0,0,0,4,0},{0,0,0,0,0,0,20,0},
                    {0,0,11,14,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,5,0,0},{0,0,0,0,0,0,0,0}}
    };

    // rotate clockwise
    private void rotateArray(byte[][] board) {
        byte temp;
        for (byte i = 0; i < 7; i++)
            for (byte j = 0; j < 7-i; j++) {
                temp = board[i][j];
                board[i][j] = board[7-j][7-i];
                board[7-j][7-i] = temp;
            }

        for (byte i = 0; i < 4; i++)
            for (byte j = 0; j < 8; j++) {
                temp = board[i][j];
                board[i][j] = board[7-i][j];
                board[7-i][j] = temp;
            }
    }

    private final byte[] matchTable = {0,2,3,4,1,6,7,8,5,10,9,0,0,0,0,0,0,18,17,20,19,22,21,24,23};

    private void rotateWall(byte[][] board) {
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (1 <= board[i][j] && board[i][j] <= 10)
                    board[i][j] = matchTable[board[i][j]];
    }

    private void rotatePrism(byte[][] spec) {
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (17 <= spec[i][j] && spec[i][j] <= 24)
                    spec[i][j] = matchTable[spec[i][j]];
    }

    private void rotateBoard(byte[][] board, byte times) {
        for (byte i = 0; i < times; i++) {
            rotateArray(board);
            rotateWall(board);
        }
    }

    private void rotateSpec(byte[][] spec, byte times) {
        for (byte i = 0; i < times; i++) {
            rotateArray(spec);
            rotatePrism(spec);
        }
    }

    public void newRandomBoard() {
        Random random = new Random();
        boolean flag;
        for (byte i = 0; i < 5; i++)
            do {
                flag = true;
                robotPos[i][0] = (byte)random.nextInt(16);
                robotPos[i][1] = (byte)random.nextInt(16);

                for (byte j = 0; j < i; j++)
                    if (robotPos[i][0] == robotPos[j][0] && robotPos[i][1] == robotPos[j][1]) {
                        flag = false;
                        break;
                    }
                if ((robotPos[i][0] == 7 || robotPos[i][0] == 8) && (robotPos[i][1] == 7 || robotPos[i][1] == 8))
                    flag = false;
            } while (!flag);

        byte[] parts = new byte[]{
                (byte)random.nextInt(4),
                (byte)(random.nextInt(4) + 4),
                (byte)(random.nextInt(4) + 8),
                (byte)(random.nextInt(4) + 12)
        };
        byte temp;
        if (random.nextInt(2) > 0) {temp = parts[0]; parts[0] = parts[1]; parts[1] = temp;}
        if (random.nextInt(2) > 0) {temp = parts[1]; parts[1] = parts[2]; parts[2] = temp;}
        if (random.nextInt(2) > 0) {temp = parts[2]; parts[2] = parts[3]; parts[3] = temp;}
        if (random.nextInt(2) > 0) {temp = parts[3]; parts[3] = parts[0]; parts[0] = temp;}

        // generate board
        byte[][] tempBoard = new byte[8][8];

        for (byte i = 0; i < 8; i++)
            System.arraycopy(boardTemplates[parts[0]][i], 0, tempBoard[i], 0, 8);
        rotateBoard(tempBoard, (byte)0);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, board[i], 0, 8);

        for (byte i = 0; i < 8; i++)
            System.arraycopy(boardTemplates[parts[1]][i], 0, tempBoard[i], 0, 8);
        rotateBoard(tempBoard, (byte)1);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, board[i], 8, 8);

        for (byte i = 0; i < 8; i++)
            System.arraycopy(boardTemplates[parts[2]][i], 0, tempBoard[i], 0, 8);
        rotateBoard(tempBoard, (byte)2);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, board[i + 8], 8, 8);

        for (byte i = 0; i < 8; i++)
            System.arraycopy(boardTemplates[parts[3]][i], 0, tempBoard[i], 0, 8);
        rotateBoard(tempBoard, (byte)3);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, board[i + 8], 0, 8);

        // generate spec
        for (byte i = 0; i < 8; i++)
            System.arraycopy(specTemplates[parts[0]][i], 0, tempBoard[i], 0, 8);
        rotateSpec(tempBoard, (byte)0);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, spec[i], 0, 8);

        for (byte i = 0; i < 8; i++)
            System.arraycopy(specTemplates[parts[1]][i], 0, tempBoard[i], 0, 8);
        rotateSpec(tempBoard, (byte)1);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, spec[i], 8, 8);

        for (byte i = 0; i < 8; i++)
            System.arraycopy(specTemplates[parts[2]][i], 0, tempBoard[i], 0, 8);
        rotateSpec(tempBoard, (byte)2);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, spec[i + 8], 8, 8);

        for (byte i = 0; i < 8; i++)
            System.arraycopy(specTemplates[parts[3]][i], 0, tempBoard[i], 0, 8);
        rotateSpec(tempBoard, (byte)3);
        for (byte i = 0; i < 8; i++)
            System.arraycopy(tempBoard[i], 0, spec[i + 8], 0, 8);

        if (!fixBoardSeam(board)) newRandomBoard();

        goalNo = -1;
        for (byte i = 0; i < 16; i++)
            goalsRemained[i] = (byte)(i + 1);

        for (byte i = 0; i < 19; i++) {
            byte t1 = (byte)random.nextInt(16);
            byte t2 = (byte)random.nextInt(16);
            byte tmp = goalsRemained[t1];
            goalsRemained[t1] = goalsRemained[t2];
            goalsRemained[t2] = tmp;
        }

        solver.preprocess();
    }

    public byte getNextGoal() {
        solution = null;
        goalNo++;
        if (goalNo >= 16) return -1;
        return goalsRemained[goalNo];
    }

    public byte getGoal() {
        if (goalNo >= 16) return -1;
        return goalsRemained[goalNo];
    }

    private boolean fixBoardSeam(byte[][] board) {
        for (byte i = 0; i < 16; i++) {
            if (board[i][7] == 3 || board[i][7] == 6 || board[i][7] == 7 || board[i][7] == 9) {
                if (board[i][8] == 0) { board[i][8] = 1; continue; }
                if (board[i][8] == 1) continue;
                if (board[i][8] == 2) { board[i][8] = 5; continue; }
                if (board[i][8] == 3) { board[i][8] = 9; continue; }
                if (board[i][8] == 4) { board[i][8] = 8; continue; }
                return false;
            }
            else if (board[i][8] == 1 || board[i][8] == 5 || board[i][8] == 8 || board[i][8] == 9) {
                if (board[i][7] == 0) { board[i][7] = 3; continue; }
                if (board[i][7] == 1) { board[i][7] = 9; continue; }
                if (board[i][7] == 2) { board[i][7] = 6; continue; }
                if (board[i][7] == 3) continue;
                if (board[i][7] == 4) { board[i][7] = 7; continue; }
                return false;
            }

            if (board[7][i] == 4 || board[7][i] == 7 || board[7][i] == 8 || board[7][i] == 10) {
                if (board[8][i] == 0) { board[8][i] = 2; continue; }
                if (board[8][i] == 1) { board[8][i] = 5; continue; }
                if (board[8][i] == 2) continue;
                if (board[8][i] == 3) { board[8][i] = 6; continue; }
                if (board[8][i] == 4) { board[8][i] = 10; continue; }
                return false;
            }
            else if (board[8][i] == 2 || board[8][i] == 5 || board[8][i] == 6 || board[8][i] == 10) {
                if (board[7][i] == 0) { board[7][i] = 4; continue; }
                if (board[7][i] == 1) { board[7][i] = 8; continue; }
                if (board[7][i] == 2) { board[7][i] = 10; continue; }
                if (board[7][i] == 3) { board[7][i] = 7; continue; }
                if (board[7][i] == 4) continue;
                return false;
            }
        }
        return true;
    }

    public byte[][] getBoard() {
        return board;
    }

    public byte[][] getSpec() {
        return spec;
    }

    public byte[][] getRobotPos() {
        return robotPos;
    }

    List<Pair<Byte, Byte>> solution = null;

    public void findSolution() {
        solution = solver.findSolution();
    }

    public List<Pair<Byte, Byte>> getSolution() {
        return solution;
    }
}
