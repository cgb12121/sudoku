import java.util.Random;

public class SudokuBoard {
    private int[][] board;
    private int[][] solutionBoard;
    private static final int BOARD_SIZE = 9;
    private static final int MIN_FILLED_CELLS = 30;

    public SudokuBoard() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        generateBoard();
        solutionBoard = copyBoard(board);
        removeDigits();
    }

    private void generateBoard() {
        fillDiagonal();
        fillRemaining(0, 3);
    }

    private void fillDiagonal() {
        for (int i = 0; i < BOARD_SIZE; i += 3) {
            fillBox(i, i);
        }
    }

    private boolean fillRemaining(int i, int j) {
        if (j >= BOARD_SIZE && i < BOARD_SIZE - 1) {
            i++;
            j = 0;
        }
        if (i >= BOARD_SIZE && j >= BOARD_SIZE) {
            return true;
        }
        if (i < 3) {
            if (j < 3) {
                j = 3;
            }
        } else if (i < BOARD_SIZE - 3) {
            if (j == (i / 3) * 3) {
                j += 3;
            }
        } else {
            if (j == BOARD_SIZE - 3) {
                i++;
                j = 0;
                if (i >= BOARD_SIZE) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= BOARD_SIZE; num++) {
            if (isSafe(i, j, num)) {
                board[i][j] = num;
                if (fillRemaining(i, j + 1)) {
                    return true;
                }
                board[i][j] = 0;
            }
        }
        return false;
    }

    private void fillBox(int row, int col) {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num;
                do {
                    num = rand.nextInt(9) + 1;
                } while (!isSafeBox(row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }

    private boolean isSafeBox(int row, int col, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[row + i][col + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSafe(int i, int j, int num) {
        return !usedInRow(i, num) && !usedInCol(j, num) && !usedInBox(i - i % 3, j - j % 3, num);
    }

    private boolean usedInRow(int i, int num) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[i][j] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInCol(int j, int num) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][j] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[rowStart + i][colStart + j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeDigits() {
        int totalCells = BOARD_SIZE * BOARD_SIZE;
        int cellsToRemove = totalCells - MIN_FILLED_CELLS;
        Random rand = new Random();

        while (cellsToRemove > 0) {
            int i = rand.nextInt(BOARD_SIZE);
            int j = rand.nextInt(BOARD_SIZE);
            if (board[i][j] != 0) {
                board[i][j] = 0;
                cellsToRemove--;
            }
        }
    }

    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, BOARD_SIZE);
        }
        return copy;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolutionBoard() {
        return solutionBoard;
    }
}
