import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

/**
 * Created by matW7 on 16.9.2015.
 */
public class FindInAllDirections {
    private JPanel panelMain;
    private JButton buttonFind;
    private JTextField textFieldFind;
    private JTextPane textPane;
    private JPanel panelBottom;
    private char[][] charArray; //2d array of chars from textPane

    public FindInAllDirections() {
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFind();
            }
        };

        //on "enter" or click button go to onFind()
        buttonFind.addActionListener(action);
        textFieldFind.addActionListener(action);


        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {/*
                StyledDocument doc = textPane.getStyledDocument();
                SimpleAttributeSet set = new SimpleAttributeSet();
                StyleConstants.setBackground(set, Color.WHITE);
                doc.setCharacterAttributes(0, textPane.getDocument().getLength(), set, true);
                doc.setParagraphAttributes(0, textPane.getText().length(), set, true);*/
            }
        });


        textPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                StyledDocument doc = textPane.getStyledDocument();
                SimpleAttributeSet set = new SimpleAttributeSet();
                StyleConstants.setBackground(set, Color.WHITE);
                doc.setCharacterAttributes(0, textPane.getDocument().getLength(), set, true);
            }
        });
    }


    public void onFind() {
        int maxColumns = checkInputColumns(textPane.getText()); //first check if text in textPane is rectangle
        if (maxColumns != -1) {
            charArray = new char[textPane.getText().split("\n").length][maxColumns];//initialize 2d array, count /n for # of lines
            writeToArray(textPane.getText()); //write to char array

            if (!textFieldFind.getText().isEmpty()) {
                String inputField = textFieldFind.getText();
                findInCharArray(inputField); //find inputField text in charArray and color it
            }
        } else {
            JOptionPane.showMessageDialog(null, "Input must be rectangular shape (number of columns must be the same in all rows).", "Warning", JOptionPane.OK_OPTION);
        }
    }

    private int checkInputColumns(String input) {
        //count max columns
        int countCols = 0;
        Scanner scanner = new Scanner(input);
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            int thisCols = line.length();

            if (countCols == 0) countCols = thisCols; //first time
            else if (countCols != thisCols) {
                return -1;
            }
        }
        scanner.close();

        return countCols;
    }

    private void writeToArray(String input) {
        //change values in charArray
        Scanner scanner = new Scanner(input);
        String line;
        int i = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            charArray[i] = line.toCharArray();
            i++;
        }
        scanner.close();
    }

    private void printArray(char[][] twodarray) {
        for (int i = 0; i < twodarray.length; i++) {
            for (int j = 0; j < twodarray[i].length; j++) {
                System.out.print(twodarray[i][j]);
            }
            System.out.println();
        }
    }

    private void printArray(int[][] twodarray) {
        for (int i = 0; i < twodarray.length; i++) {
            for (int j = 0; j < twodarray[i].length; j++) {
                System.out.print(twodarray[i][j]);
            }
            System.out.println();
        }
    }


    private void findInCharArray(String input) {
        //find first char of input in charArray and continue finding other chars
        for (int i = 0; i < charArray.length; i++) {
            for (int j = 0; j < charArray[i].length; j++) {
                if (input.charAt(0) == charArray[i][j]) {
                    findAround(i, j, input);
                }
            }
        }
    }

    private void findAround(int i, int j, String input) {
        //find input text in charArray around i,j coordinates
        int[][] foundPositions = new int[input.length()][2]; //x y coordiantes of found chars in charArray
        foundPositions[0] = new int[]{i, j};
        ////down
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][0] + 1) > charArray.length - 1) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0] + 1][foundPositions[ii][1]] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0] + 1, foundPositions[ii][1]};
                ;
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;

        ////right
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][1] + 1) > charArray[0].length - 1) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0]][foundPositions[ii][1] + 1] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0], foundPositions[ii][1] + 1};
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;

        ////left
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][1] - 1) < 0) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0]][foundPositions[ii][1] - 1] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0], foundPositions[ii][1] - 1};
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;

        ////up
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][0] - 1) < 0) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0] - 1][foundPositions[ii][1]] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0] - 1, foundPositions[ii][1]};
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;

        ////down right
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][0] + 1) > charArray.length - 1 || foundPositions[ii][1] + 1 > charArray[0].length - 1) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0] + 1][foundPositions[ii][1] + 1] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0] + 1, foundPositions[ii][1] + 1};
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;

        ////down left
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][0] + 1) > charArray.length - 1 || foundPositions[ii][1] - 1 < 0) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0] + 1][foundPositions[ii][1] - 1] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0] + 1, foundPositions[ii][1] - 1};
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;

        ////up right
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][0] - 1) < 0 || foundPositions[ii][1] + 1 > charArray[0].length - 1) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0] - 1][foundPositions[ii][1] + 1] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0] - 1, foundPositions[ii][1] + 1};
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;


        ////up left
        for (int ii = 0; ii < input.length() - 1; ii++) {
            if ((foundPositions[ii][0] - 1) < 0 || foundPositions[ii][1] - 1 < 0) {
                foundPositions[0][0] = -1;//not found
                break;
            }
            if (charArray[foundPositions[ii][0] - 1][foundPositions[ii][1] - 1] == input.charAt(ii + 1)) {
                foundPositions[ii + 1] = new int[]{foundPositions[ii][0] - 1, foundPositions[ii][1] - 1};
            } else {
                foundPositions[0][0] = -1;//not found
                break;
            }
        }
        if (foundPositions[0][0] != -1) {
            colorText(foundPositions);
        }
        foundPositions[0][0] = i;
    }

    private void colorText(int[][] foundPositions) {
        //color each character
        StyledDocument doc = textPane.getStyledDocument();
        int maxColumns = checkInputColumns(textPane.getText());

        for (int i = 0; i < foundPositions.length; i++) {
            SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setBackground(set, new Color(255, 0, 0));

            doc.setCharacterAttributes(foundPositions[i][0] * maxColumns + foundPositions[i][1] + foundPositions[i][0], 1, set, true);
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Find in all directions");
        frame.setMinimumSize(new Dimension(300, 300));
        frame.setContentPane(new FindInAllDirections().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
