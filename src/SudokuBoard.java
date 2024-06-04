import java.util.Random;

/**
 * Lớp SudokuBoard tạo ra một bảng Sudoku.
 */
public class SudokuBoard {
    private int[][] board;                                  // Bảng Sudoku
    private int[][] solutionBoard;                          // Bảng Sudoku chứa giải (ẩn khi trò chơi bắt đầu)
    private static final int BOARD_SIZE = 9;                // Kích thước bảng Sudoku
    private static final int MIN_FILLED_CELLS = 30;         // Số ô ít nhất cho trước giá trị

    /**
     * Hàm khởi tạo tạo bảng Sudoku mới.
     */
    public SudokuBoard() {
        board = new int[BOARD_SIZE][BOARD_SIZE];        // Tạo bảng 9x9
        generateBoard();                                // Khởi tạo bảng, cho trước ô có sẵn giá trị nhất định
        solutionBoard = copyBoard(board);               // Copy bảng, lưu lại các giá trị đã cho để làm đáp án
        removeDigits();                                 // Xóa các ô ngẫu nhiên cho trò chơi
    }

    /**
     * Phương thức tạo bảng Sudoku cho trò chơi.
     */
    private void generateBoard() {
        fillDiagonal();
        fillRemaining(0, 3);
    }

    /**
     * Phương thức điền các ô chéo của bảng Sudoku.
     */
    private void fillDiagonal() {
        for (int i = 0; i < BOARD_SIZE; i += 3) {
            fillBox(i, i);
        }
    }

    /**
     * Phương thức điền các ô còn lại của bảng Sudoku.
     * @param i Hàng cần điền.
     * @param j Cột cần điền.
     * @return Trả về true nếu bảng đã được điền đầy, ngược lại trả về false.
     */
    private boolean fillRemaining(int i, int j) {
        // Cập nhật chỉ số hàng và cột khi đã điền hết một dòng hoặc cột
        if (j >= BOARD_SIZE && i < BOARD_SIZE - 1) {
            i++;
            j = 0;
        }

        // Nếu đã điền hết bảng thì trả về true
        if (i >= BOARD_SIZE && j >= BOARD_SIZE) {
            return true;
        }

        // Xác định vùng 3x3 và điều chỉnh chỉ số hàng và cột cho phù hợp
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

        // Điền các số từ 1 đến 9 vào ô trống
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

    /**
     * Phương thức điền một hộp con 3x3 của bảng Sudoku.
     * @param row Hàng của hộp cần điền.
     * @param col Cột của hộp cần điền.
     */
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

    /**
     * Phương thức kiểm tra xem một số có an toàn để điền vào ô không.
     * @param row Hàng của ô cần kiểm tra.
     * @param col Cột của ô cần kiểm tra.
     * @param num Số cần kiểm tra.
     * @return Trả về true nếu số num an toàn để điền vào ô, ngược lại trả về false.
     */
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

    /**
     * Phương thức kiểm tra xem một số có an toàn để điền vào ô không.
     * @param i Hàng của ô cần kiểm tra.
     * @param j Cột của ô cần kiểm tra.
     * @param num Số cần kiểm tra.
     * @return Trả về true nếu số num an toàn để điền vào ô, ngược lại trả về false.
     */
    private boolean isSafe(int i, int j, int num) {
        return !usedInRow(i, num) && !usedInCol(j, num) && !usedInBox(i - i % 3, j - j % 3, num);
    }

    /**
     * Các hương thức kiểm tra xem một số đã được sử dụng trong hàng hay chưa.
     * @param i hàng cần kiểm tra
     * @param num số cần kiểm tra
     * @return Trả về true nếu số num đã được sử dụng, ngược lại trả về false.
     */
    private boolean usedInRow(int i, int num) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[i][j] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Các hương thức kiểm tra xem một số đã được sử dụng trong cột hay chưa.
     * @param j hàng cần kiểm tra
     * @param num số cần kiểm tra
     * @return Trả về true nếu số num đã được sử dụng, ngược lại trả về false.
     */
    private boolean usedInCol(int j, int num) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][j] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Các hương thức kiểm tra xem một số đã được sử dụng trong hàng hay chưa.
     * @param rowStart hàng bắt đầu cần kiểm tra
     * @param colStart cột bắt đầu cần kiểm tra
     * @param num số cần kiểm tra
     * @return Trả về true nếu số num đã được sử dụng, ngược lại trả về false.
     */
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

    /**
     * Phương thức loại bỏ các chữ số từ bảng Sudoku để tạo ra một trò chơi.
     */
    private void removeDigits() {
        int totalCells = BOARD_SIZE * BOARD_SIZE;
        int cellsToRemove = totalCells - MIN_FILLED_CELLS;
        Random rand = new Random();

        // Loại bỏ các chữ số từ bảng một cách ngẫu nhiên
        while (cellsToRemove > 0) {
            int i = rand.nextInt(BOARD_SIZE);
            int j = rand.nextInt(BOARD_SIZE);
            if (board[i][j] != 0) {
                board[i][j] = 0;
                cellsToRemove--;
            }
        }
    }

    /**
     * Phương thức sao chép bảng Sudoku.
     * @param original Bảng gốc cần sao chép.
     * @return Bản sao của bảng gốc.
     */
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, BOARD_SIZE);
        }
        return copy;
    }

    /**
     * Phương thức trả về bảng Sudoku hiện tại.
     * @return Bảng Sudoku hiện tại.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Phương thức trả về bảng Sudoku giải đáp.
     * @return Bảng Sudoku giải đáp.
     */
    public int[][] getSolutionBoard() {
        return solutionBoard;
    }
}
