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

public class SudokuGame extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];
    private JButton checkButton = new JButton("Check");
    private SudokuBoard generator;
    private int[][] board;
    private int[][] solutionBoard;

    public SudokuGame() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField();
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 20));

                // Determine the thickness of the borders
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;

                // Set the border
                cells[i][j].setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                gridPanel.add(cells[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        add(checkButton, BorderLayout.SOUTH);

        checkButton.addActionListener(new CheckButtonListener());

        generator = new SudokuBoard();
        board = generator.getBoard();
        solutionBoard = generator.getSolutionBoard();
        populateBoard();

        setSize(600, 600);
        setVisible(true);
    }

    private void populateBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    cells[i][j].setText(String.valueOf(board[i][j]));
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                    cells[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    private class CheckButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkSudoku();
        }
    }

    private void checkSudoku() {
        boolean isCorrect = true;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].isEditable()) {
                    String text = cells[i][j].getText();
                    if (text.isEmpty() || !text.matches("[1-9]")) {
                        cells[i][j].setBackground(Color.RED);
                        isCorrect = false;
                    } else {
                        int value = Integer.parseInt(text);
                        if (value == solutionBoard[i][j]) {
                            cells[i][j].setBackground(Color.GREEN);
                        } else {
                            cells[i][j].setBackground(Color.RED);
                            isCorrect = false;
                        }
                    }
                }
            }
        }

        if (isCorrect) {
            JOptionPane.showMessageDialog(null, "Sudoku is correct!");
            restartGame(); // Restart the game after the message dialog is closed
            return;
        }

        // Delay for 5 seconds and then reset colors
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetColors();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

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

    private void restartGame() {
        board = generator.getBoard();
        solutionBoard = generator.getSolutionBoard();
        populateBoard();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
