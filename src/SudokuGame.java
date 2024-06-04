import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Lớp SudokuGame giao diện cho Sudoku.
 */
public class SudokuGame extends JFrame {
    // Biến cells lưu trữ một mảng 2 chiều 9x9 các ô nhập liệu
    private JTextField[][] cells = new JTextField[9][9];
    // Biến checkButton kiểm tra trạng thái của bảng Sudoku
    private JButton checkButton = new JButton("Check");
    // Biến generator tạo ra các bảng Sudoku mới
    private SudokuBoard generator;
    // Biến board là mảng hai chiều của bảng Sudoku
    private int[][] board;
    // Biến solutionBoard là mảng hai chiều chứa giải pháp bảng Sudoku
    private int[][] solutionBoard;

    /**
     * Hàm khởi tạo tạo ra cửa sổ trò chơi Sudoku.
     */
    public SudokuGame() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo lưới 9x9 của ô nhập liệu Sudoku
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Tạo một ô nhập liệu mới và thiết lập các thuộc tính
                cells[i][j] = new JTextField();
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 20));

                // Xác định độ dày của viền dựa trên vị trí trong lưới
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;

                // Tạo viền cho ô nhập liệu
                cells[i][j].setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                // Thêm ô nhập liệu vào lưới
                gridPanel.add(cells[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);    // Thêm lưới vào giữa cửa sổ
        add(checkButton, BorderLayout.SOUTH);   // Nút "Check" dưới cùng

        checkButton.addActionListener(new CheckButtonListener());   // Sự kiện cho nút "Check"

        generator = new SudokuBoard();      // Khởi tạo object để tạo bảng mơi
        board = generator.getBoard();       // Tạo bảng
        solutionBoard = generator.getSolutionBoard();       // Tạo mới bảng lưu lại lời giải
        populateBoard();                    // Điển vào bảng

        //Size bảng, hiển thị
        setSize(600, 600);
        setVisible(true);
    }

    /**
     * Phương thức điền các ô đã biết sẵn vào bảng Sudoku.
     * Các ô đã biết sẵn sẽ được điền với màu nền LIGHT_GRAY và không cho phép chỉnh sửa.
     * Các ô trống sẽ được điền với màu nền WHITE và cho phép chỉnh sửa.
     */
    private void populateBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Nếu ô đã được điền trước đó
                if (board[i][j] != 0) {
                    // Đặt văn bản của ô là giá trị của ô đã biết sẵn
                    cells[i][j].setText(String.valueOf(board[i][j]));

                    // Không cho phép chỉnh sửa ô
                    cells[i][j].setEditable(false);

                    // Đặt màu nền cho ô
                    cells[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    // Nếu ô chưa được điền trước đó

                    // Xóa văn bản của ô
                    cells[i][j].setText("");

                    // Cho phép chỉnh sửa ô
                    cells[i][j].setEditable(true);

                    // Đặt màu nền cho ô
                    cells[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    /**
     * Lớp sự kiện cho nút "Check".
     * Khi người dùng nhấp vào nút "Check", phương thức checkSudoku() sẽ được gọi.
     */
    private class CheckButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkSudoku();
        }
    }

    /**
     * Phương thức kiểm tra xem Sudoku đã đúng hay chưa.
     * Các ô nhập liệu sẽ được kiểm tra xem liệu chúng có đúng theo quy tắc của Sudoku hay không.
     * Các ô không hợp lệ sẽ được đánh dấu màu đỏ, còn các ô hợp lệ sẽ được đánh dấu màu xanh.
     * Nếu toàn bộ Sudoku đúng, một hộp thoại thông báo sẽ xuất hiện và trò chơi sẽ được khởi động lại sau đó.
     */
    private void checkSudoku() {
        // Biến kiểm tra xem Sudoku có đúng không
        boolean isCorrect = true;

        // Duyệt qua tất cả các ô nhập liệu
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Nếu ô là ô có thể chỉnh sửa
                if (cells[i][j].isEditable()) {
                    // Lấy văn bản từ ô
                    String text = cells[i][j].getText();
                    // Kiểm tra xem văn bản có hợp lệ không
                    if (text.isEmpty() || !text.matches("[1-9]")) {
                        // Nếu không hợp lệ, đánh dấu màu đỏ và đặt isCorrect thành false
                        cells[i][j].setBackground(Color.RED);
                        isCorrect = false;
                    } else {
                        // Nếu hợp lệ, chuyển đổi văn bản sang số nguyên và so sánh với giải pháp
                        int value = Integer.parseInt(text);
                        if (value == solutionBoard[i][j]) {
                            // Nếu đúng, đánh dấu màu xanh
                            cells[i][j].setBackground(Color.GREEN);
                        } else {
                            // Nếu không đúng, đánh dấu màu đỏ và đặt isCorrect thành false
                            cells[i][j].setBackground(Color.RED);
                            isCorrect = false;
                        }
                    }
                }
            }
        }

        // Nếu toàn bộ Sudoku đúng
        if (isCorrect) {
            // Hiển thị hộp thoại thông báo và khởi động lại trò chơi sau khi hộp thoại được đóng
            JOptionPane.showMessageDialog(null, "Sudoku is correct!");
            restartGame();
            return;
        }

        // Nếu Sudoku không đúng, đặt lại màu sắc sau 5 giây
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetColors();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Phương thức đặt lại màu sắc của các ô.
     */
    private void resetColors() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].isEditable()) {
                    cells[i][j].setBackground(Color.WHITE);
                } else {
                    cells[i][j].setBackground(Color.LIGHT_GRAY);
                }
            }
        }
    }

    /**
     * Phương thức khởi động lại trò chơi Sudoku.
     */
    private void restartGame() {
        board = generator.getBoard();
        solutionBoard = generator.getSolutionBoard();
        populateBoard();
    }

    /**
     * Phương thức chạy ứng dụng.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
