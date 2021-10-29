import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ScrabbleDriver extends Application {

    // Attrtibutes..
    private WordLetters wordLetters;
    public static CurrentHand currentHand;
    private Random random;
    private WordData wordData;
    private int playerScore;
    private int computerScore;
    private char[][] boardCells;
    private char[][] currentBoardCells;
    private int[][] boardValues;
    private Random randomNumberGenerator;
    private Label[][] label;
    private Label lblPlayerScore;
    private Label lblComputerScore;
    private AIScrable AScrable;

    // default..
    public ScrabbleDriver() {

        boardCells = new char[15][15];
        currentBoardCells = new char[16][16];
        boardValues = new int[15][15];
        randomNumberGenerator = new Random();
        wordData = WordData.getInstance();
        AScrable = new AIScrable("src/SOWPODS.txt");

        for (int x = 0; x < 15; x++) {

            for (int y = 0; y < 15; y++) {
                boardCells[x][y] = ' ';
                boardValues[x][y] = 1;
            }

        }

        boardCells[7][7] = '*';

        currentHand = new CurrentHand();
        wordLetters = new WordLetters();
        random = new Random();
        wordData = WordData.getInstance();

        for (int i = 0; i < 7; i++) {

            int indexRan = random.nextInt(wordLetters.getLettersInHand().size());
            currentHand.getPlayerHand().add(wordLetters.getLettersInHand().get(indexRan));
            wordLetters.getLettersInHand().remove(indexRan);

        }

        for (int i = 0; i < 7; i++) {

            currentHand.getComputerHand()
                    .add(wordLetters.getLettersInHand().get(random.nextInt(wordLetters.getLettersInHand().size())));
            wordLetters.getLettersInHand().remove(i);

        }

        for (int i = 0; i < 100; i++)
            wordLetters.getCurrentLettersInHand().add('!');

        for (int i = 0; i < 7; i++)
            currentHand.getCurrentPlayerHand().add('!');

        for (int i = 0; i < 7; i++)
            currentHand.getCurrentComputerHand().add('!');

    }



    // executing driver..
    public void execute() {

        int currentTile = 1;
        int row = 0;
        int column = 0;
        int playerTurn = 0;
        int valueFlag = 0;
        String columnValue = "";
        int countOfLoop = 0;
        int initCol = 0;
        int initRow = 0;
        boolean endFlag = false;
        boolean continueFlag = false;

        while (true) {

            setScore(getPlayerScore(), getComputerScore());
            countOfLoop = 0;
            initializeHand();
            printBoardOnScreen();
            initBoard(true);

            while (true) {

                String text = "\nWhich tile you want to add on board? Please enter the number next to the letter." +
                        " Enter 0 to make make your move.\n";

                for (int i = 0; i < getCurrentHand().getCurrentPlayerHand().size(); i++) {

                    if (getCurrentHand().getCurrentPlayerHand().get(i) != '!')
                        text += (i + 1) + ":" + getCurrentHand().getCurrentPlayerHand().get(i) + "\n";

                }

                if (countOfLoop == 0 && playerTurn != 0)
                    text += "8: Change the Tile Hand.\n";

                text += "Select the 1st tile.";

                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Select Tile");
                dialog.setContentText(text);
                Optional<String> result = dialog.showAndWait();
                try{
                    currentTile = Integer.parseInt(result.get());
                }catch(Exception er){
                    return;
                }


                if (currentTile == 8 && countOfLoop == 0 && playerTurn != 0) {

                    removePlayerHand();
                    continueFlag = true;
                    break;

                }

                if (countOfLoop == 0) {

                    if (currentTile != 0) {

                        if (playerTurn != 0) {

                            dialog = new TextInputDialog("");
                            dialog.setTitle("Select Column");
                            dialog.setContentText("Where you like to place this tile?\n" + "Enter Column Position (A-O): ");
                            result = dialog.showAndWait();
                            columnValue = result.get();

                            columnValue = columnValue.toUpperCase();
                            column = ((int) columnValue.charAt(0)) - 65;

                            dialog = new TextInputDialog("");
                            dialog.setTitle("Select Row");
                            dialog.setContentText("Where you like to place this tile?\n" + "Enter Row Position (15-0): ");
                            result = dialog.showAndWait();
                            row = Integer.parseInt(result.get());

                            setTileOnBoard(currentTile - 1, row, column, valueFlag);

                            dialog = new TextInputDialog("");
                            dialog.setTitle("Select Choice");
                            dialog.setContentText("Select the orientation of the word.\n"
                                    + "0: horizontal\n1: vertical\nYour Choice");
                            result = dialog.showAndWait();
                            valueFlag = Integer.parseInt(result.get());

                            initCol = column;
                            initRow = row;

                        } else if (playerTurn == 0) {

                            row = 7;
                            column = 7;
                            initCol = 7;
                            initRow = 7;
                            setTileOnBoard(currentTile - 1, row, column, 0);
                            dialog = new TextInputDialog("");
                            dialog.setTitle("Select Choice");
                            dialog.setContentText("Select the orientation of the word.\n"
                                    + "0: horizontal\n1: vertical\nYour Choice");
                            result = dialog.showAndWait();
                            valueFlag = Integer.parseInt(result.get());

                        }

                    } else if (currentTile == 0) {

                        initializeHand();

                        if (valueFlag == 0)
                            System.out.println(compareWord(initRow, initCol, valueFlag));
                        else if (valueFlag == 1)
                            System.out.println(compareWord(initCol, initRow, valueFlag));
                        break;

                    }

                } else if (countOfLoop != 0) {

                    if (currentTile != 0) {
                        System.out.println(getCurrentHand().getPlayerHand().get(0) + " "
                                + getCurrentHand().getCurrentPlayerHand().get(0));
                        if (valueFlag == 0)
                            setTileOnBoard(currentTile - 1, row, column + countOfLoop, valueFlag);
                        else if (valueFlag == 1)
                            setTileOnBoard(currentTile - 1, row - countOfLoop, column, valueFlag);

                    } else if (currentTile == 0) {

                        String entered = "";

                        if (valueFlag == 0)
                            entered = compareWord(initCol, initRow, valueFlag);
                        else if (valueFlag == 1)
                            entered = compareWord(initRow, initCol, valueFlag);

                        if (countOfLoop == 1)
                            countOfLoop--;

                        if (playerTurn == 0)
                            countOfLoop--;

                        if (isBoardCorrectToBeExecuted() == false
                                || (entered.length() > countOfLoop) == false) {/*   */

                            Alert alert = new Alert(AlertType.INFORMATION, "The tiles you have placed are invalid! Please try this again."
                                    , ButtonType.OK);
                            alert.showAndWait();
                            countOfLoop = 0;
                            initBoard(true);
                            continue;

                        } else {
                            if (playerTurn == 0)
                                playerTurn = 1;
                            reverseBoard(true);
                            updateScore(0, entered);
                        }

                        initializeHand();
                        break;
                    }

                }

                countOfLoop++;

            }

            reverseBoard(false);
            computerAIExecution(0);

            currentTile = 1;
            Alert alert = new Alert(AlertType.INFORMATION, getWordLetters().getLettersInHand().size() + " letters left in the bag!"
                    , ButtonType.OK);
            alert.showAndWait();
            if (getWordLetters().getLettersInHand().size() < 10) {
                alert = new Alert(AlertType.INFORMATION, "Game over!"
                        , ButtonType.OK);
                endFlag = true;
                break;
            }

            if (continueFlag){

                printBoardOnScreen();
                continue;
            }


            if (endFlag)
                break;

        }

        stopped();

    }

    public WordLetters getWordLetters() {

        return wordLetters;

    }

    public void setWordLetters(WordLetters wordLetters) {

        this.wordLetters = wordLetters;

    }

    public CurrentHand getCurrentHand() {

        return currentHand;

    }

    public void setCurrentHand(CurrentHand currentHand) {

        this.currentHand = currentHand;

    }


    public int getPlayerScore() {

        return playerScore;

    }

    public int getComputerScore() {

        return computerScore;

    }

    public void initBoard(boolean flag) {

        if(flag) {

            for (int i = 0; i < 15; i++) {

                for (int j = 0; j < 15; j++)
                    getCurrentBoardCells()[i][j] = getBoardCells()[i][j];

            }

            for (int i = 0; i < currentHand.getPlayerHand().size(); i++) {
                currentHand.getCurrentPlayerHand().set(i, currentHand.getPlayerHand().get(i));
            }

            for (int i = 0; i < currentHand.getPlayerHand().size(); i++) {
                if (currentHand.getPlayerHand().size() >= wordLetters.getCurrentLettersInHand().size())
                    break;
                else
                    wordLetters.getCurrentLettersInHand().set(i, wordLetters.getLettersInHand().get(i));
            }

        } else {

            setCurrentBoardCells(new char[15][15]);

            for (int i = 0; i < 15; i++) {

                for (int j = 0; j < 15; j++)
                    getCurrentBoardCells()[i][j] = getBoardCells()[i][j];

            }

            for (int i = 0; i < currentHand.getCurrentComputerHand().size(); i++) {
                currentHand.getComputerHand().set(i, currentHand.getCurrentComputerHand().get(i));
            }

            for (int i = 0; i < currentHand.getComputerHand().size(); i++) {

                if (currentHand.getComputerHand().size() >= wordLetters.getLettersInHand().size())
                    break;
                else
                    wordLetters.getLettersInHand().set(i, wordLetters.getCurrentLettersInHand().get(i));
            }
        }

    }

    @SuppressWarnings("resource")
    public void setTileOnBoard(int tileNum, int x, int y, int updateFlag) {

        if (!(x < 0 || x > 14 || y < 0 || y > 14)) {

            if (getCurrentBoardCells()[x][y] == ' ' || getCurrentBoardCells()[x][y] == '*') {

                if (currentHand.getCurrentPlayerHand().get(tileNum) != ' ') {

                    getCurrentBoardCells()[x][y] = currentHand.getCurrentPlayerHand().get(tileNum);
                    currentHand.getCurrentPlayerHand().remove(tileNum);
                    currentHand.getCurrentPlayerHand().add('!');

                } else {

                    System.out.println("What would you like the blank tile to represent?");
                    Scanner takingNumber = new Scanner(System.in);
                    getCurrentBoardCells()[x][y] = takingNumber.next().charAt(0);
                    currentHand.getCurrentPlayerHand().remove(tileNum);
                    currentHand.getCurrentPlayerHand().add('!');
                    System.out.println(currentHand.getCurrentPlayerHand().getHandData().toString());

                }

            } else {

                if (updateFlag == 0) {

                    setTileOnBoard(tileNum, x, y + 1, updateFlag);

                } else if (updateFlag == 1) {

                    setTileOnBoard(tileNum, x - 1, y, updateFlag);

                }

            }

        }

    }

    public void initializeHand() {

        for (int i = 0; i < currentHand.getPlayerHand().size(); i++) {

            if (currentHand.getPlayerHand().get(i) == '!')
                currentHand.getPlayerHand().remove(i);

        }

        while (currentHand.getPlayerHand().size() != 7) {

            int randValue = random.nextInt(wordLetters.getLettersInHand().size());
            currentHand.getPlayerHand().add(wordLetters.getLettersInHand().get(randValue));
            wordLetters.getLettersInHand().remove(randValue);

        }

        for (int i = 0; i < currentHand.getComputerHand().size(); i++) {
            if (currentHand.getComputerHand().get(i) == '!')
                currentHand.getComputerHand().remove(i);
        }

        while (currentHand.getComputerHand().size() != 7) {

            int randValue = random.nextInt(wordLetters.getLettersInHand().size());
            if (wordLetters.getLettersInHand().get(randValue) != ' ') {
                currentHand.getComputerHand().add(wordLetters.getLettersInHand().get(randValue));
                wordLetters.getLettersInHand().remove(randValue);
            }
        }

    }

    public void reverseBoard(boolean flag) {

        if(flag) {

            for (int i = 0; i < 15; i++) {

                for (int j = 0; j < 15; j++)
                    getBoardCells()[i][j] = getCurrentBoardCells()[i][j];

            }

            for (int i = 0; i < currentHand.getPlayerHand().size(); i++) {
                currentHand.getPlayerHand().set(i, currentHand.getCurrentPlayerHand().get(i));
            }

            for (int i = 0; i < currentHand.getPlayerHand().size(); i++) {
                wordLetters.getLettersInHand().set(i, wordLetters.getCurrentLettersInHand().get(i));
            }

        } else {

            for (int i = 0; i < 15; i++) {

                for (int j = 0; j < 15; j++)
                    getBoardCells()[i][j] = getCurrentBoardCells()[i][j];

            }

            for (int i = 0; i < currentHand.getCurrentComputerHand().size(); i++) {
                currentHand.getCurrentComputerHand().set(i, currentHand.getComputerHand().get(i));
            }

            for (int j = 0; j < currentHand.getCurrentComputerHand().size(); j++) {
                wordLetters.getCurrentLettersInHand().set(j, wordLetters.getLettersInHand().get(j));
            }

        }

    }

    public void updateScore(int playerComp, String madeWord) {

        int addedScore = 0;
        if (playerComp == 0) {

            addedScore = findWordScore(madeWord, true);
            playerScore += addedScore;
            System.out.println("You gain " + addedScore + " points");
        }

        if (playerComp == 1) {

            addedScore = findWordScore(madeWord, true);
            computerScore += addedScore;
            System.out.println("Opponent gains " + addedScore + " points");

        }

    }

    public ArrayList<String> findScrabbleWords() {

        ArrayList<String> allScrabbleWords = wordData.getWordsData();
        ArrayList<String> currentWords = new ArrayList<>();
        ArrayList<Character> currentLettersOfWord = new ArrayList<>();
        ArrayList<Character> lettersOnBoard = retrieveLetters();
        boolean running = true;
        boolean closeFlag = false;
        boolean missingWordFlag = false;
        boolean closeFlagToo = false;
        char notExists = '!';
        int yAxisNum = 0;
        int completeCount = 0;

        for (int i = 0; i < lettersOnBoard.size(); i++) {

            if (lettersOnBoard.get(i) == 'z') {
                yAxisNum = 0;
                break;
            }

            if (i == lettersOnBoard.size() - 1) {
                yAxisNum = allScrabbleWords.size() - 78958;
            }

        }

        for (int i = 0; i < currentHand.getComputerHand().size(); i++) {

            if (currentHand.getComputerHand().get(i) == 'z') {
                yAxisNum = 0;
                break;
            }

        }

        for (int i = 0; i < lettersOnBoard.size(); i++) {

            if (lettersOnBoard.get(i) == 'y') {
                break;
            }

            if (i == lettersOnBoard.size() - 1) {
                yAxisNum = allScrabbleWords.size() - 78502;
            }

        }

        for (int i = 0; i < currentHand.getComputerHand().size(); i++) {

            if (currentHand.getComputerHand().get(i) == 'y') {
                yAxisNum = 0;
                break;
            }

        }

        for (int i = 0; i < allScrabbleWords.size() - yAxisNum; i++) {

            if (allScrabbleWords.get(i).length() > 7)
                continue;

            notExists = '!';
            completeCount = 0;
            running = true;
            closeFlag = false;
            closeFlagToo = false;
            missingWordFlag = false;

            for (int j = 0; j < allScrabbleWords.get(i).length(); j++) {

                for (int k = 0; k < lettersOnBoard.size(); k++) {

                    if (allScrabbleWords.get(i).charAt(j) == lettersOnBoard.get(k)) {
                        closeFlag = true;
                        running = false;
                        break;
                    }

                }

                if (closeFlag)
                    break;

            }

            if (running)
                continue;

            currentLettersOfWord = new ArrayList<>();

            for (int j = 0; j < currentHand.getComputerHand().size(); j++)
                currentLettersOfWord.add(currentHand.getComputerHand().get(j));

            for (int k = 0; k < allScrabbleWords.get(i).length(); k++) {

                for (int l = 0; l < currentLettersOfWord.size(); l++) {

                    if (currentLettersOfWord.get(l) == allScrabbleWords.get(i).charAt(k)) {
                        completeCount++;
                        currentLettersOfWord.remove(l);
                        break;
                    } else
                        continue;
                }

                if (k == 7)
                    break;
            }

            for (int j = 0; j < allScrabbleWords.get(i).length(); j++) {

                for (int k = 0; k < currentHand.getComputerHand().size(); k++) {
                    if (allScrabbleWords.get(i).charAt(j) == currentHand.getComputerHand().get(k)) {
                        break;
                    }

                    if (k == currentHand.getComputerHand().size() - 1) {
                        notExists = allScrabbleWords.get(i).charAt(j);
                        closeFlagToo = true;
                        break;
                    }

                }

                if (closeFlagToo)
                    break;
            }

            for (int l = 0; l < lettersOnBoard.size(); l++) {
                if (lettersOnBoard.get(l) == notExists) {
                    missingWordFlag = true;
                    break;
                }
            }

            if (completeCount == allScrabbleWords.get(i).length() - 1 && missingWordFlag) {

                if (currentWords.isEmpty() == false) {
                    if (findWordScore(allScrabbleWords.get(i), false) >= findWordScore(currentWords.get(0), false))
                        currentWords.add(0, allScrabbleWords.get(i));
                    else
                        currentWords.add(allScrabbleWords.get(i));

                } else {
                    currentWords.add(allScrabbleWords.get(i));
                }

            } else
                continue;

        }

        System.out.print("Computer Hand: " + currentHand.getComputerHand().getHandData().toString());

        if (currentWords.size() == 0) {

            removeComputerHand();
            return findScrabbleWords();

        } else
            return currentWords;

    }

    public void computerTitleProgram(int num, int x, int y, int valueFlag) {

        if (!(x < 0 || x > 14 || y < 0 || y > 14)) {

            if (getCurrentBoardCells()[x][y] == ' ' || getCurrentBoardCells()[x][y] == '*') {

                if (currentHand.getComputerHand().get(num) != ' ') {

                    getCurrentBoardCells()[x][y] = currentHand.getComputerHand().get(num);
                    currentHand.getComputerHand().remove(num);
                    currentHand.getComputerHand().add('!');

                } else {

                    System.out.println("What would you like the blank tile to represent?");
                    @SuppressWarnings("resource")
                    Scanner scan1 = new Scanner(System.in);
                    getCurrentBoardCells()[x][y] = scan1.next().charAt(0);
                    currentHand.getComputerHand().remove(num);
                    currentHand.getComputerHand().add('!');

                }

            } else {

                if (valueFlag == 0) {
                    computerTitleProgram(num, x, y + 1, valueFlag);
                    return;
                } else if (valueFlag == 1) {
                    computerTitleProgram(num, x - 1, y, valueFlag);
                    return;
                }

            }

        }

    }

    public void computerAIExecution(int value) {

        setBoardCells(AScrable.execute(boardCells));
        computerScore += AScrable.mscore;
        initializeHand();

    }

    public void removePlayerHand() {

        for (int i = 0; i < currentHand.getPlayerHand().size(); i++) {
            if (currentHand.getPlayerHand().get(i) != '!')
                wordLetters.getLettersInHand().add(currentHand.getPlayerHand().get(i));
        }

        currentHand.getPlayerHand().clear();

        for (int i = 0; i < currentHand.getPlayerHand().size(); i++) {
            if (currentHand.getPlayerHand().get(i) == '!')
                currentHand.getPlayerHand().remove(i);
        }

        while (currentHand.getPlayerHand().size() != 7) {

            int randValue = random.nextInt(wordLetters.getLettersInHand().size());
            currentHand.getPlayerHand().add(wordLetters.getLettersInHand().get(randValue));
            wordLetters.getLettersInHand().remove(randValue);

        }

    }

    public void removeComputerHand() {

        for (int i = 0; i < currentHand.getCurrentComputerHand().size(); i++) {
            if (currentHand.getCurrentComputerHand().get(i) != '!')
                wordLetters.getLettersInHand().add(currentHand.getCurrentComputerHand().get(i));

        }

        currentHand.getComputerHand().clear();
        currentHand.getCurrentComputerHand().clear();

        for (int i = 0; i < 7; i++) {

            int rand = random.nextInt(wordLetters.getLettersInHand().size());
            currentHand.getCurrentComputerHand().add(wordLetters.getLettersInHand().get(rand));
            currentHand.getComputerHand().add(wordLetters.getLettersInHand().get(rand));
            wordLetters.getLettersInHand().remove(rand);

        }

    }

    public void stopped() {

        if (playerScore > computerScore)
            System.out.println("Congrats! You win with a score of !" + playerScore);
        else if (computerScore > playerScore)
            System.out.println("You lose! Please try again...");
        else
            System.out.println("I guess that's a tie? ");
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane();

        HBox labels = new HBox();
        labels.setAlignment(Pos.CENTER);
        lblPlayerScore = new Label("Player Score: 0");
        lblPlayerScore.setFont(Font.font ("Verdana", 14));
        lblComputerScore = new Label("Computer Score: 0");
        lblComputerScore.setFont(Font.font ("Verdana", 14));
        HBox.setMargin(lblComputerScore, new Insets(0, 0, 0, 20));
        labels.getChildren().add(lblPlayerScore);
        labels.getChildren().add(lblComputerScore);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setAlignment(Pos.CENTER);

        String[] header = { "-", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O" };
        for (int i = 0; i < 16; i++) {

            Label label = new Label(header[i]);
            label.setAlignment(Pos.CENTER);
            label.setMinSize(30, 30);
            label.setPadding(new Insets(8));
            label.setFont(Font.font ("Verdana", 10));
            label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            gridPane.add(label, i, 0);

        }

        label = new Label[15][15];
        for (int i = 0; i < 15; i++) {

            Label labelT = new Label(String.valueOf(15 - i));
            labelT.setAlignment(Pos.CENTER);
            labelT.setMinSize(30, 30);
            labelT.setPadding(new Insets(8));
            labelT.setFont(Font.font ("Verdana", 10));
            labelT.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            gridPane.add(labelT, 0, i);

            for (int j = 0; j < 15; j++) {

                label[i][j] = new Label("");
                label[i][j].setPadding(new Insets(8));
                label[i][j].setAlignment(Pos.CENTER);
                label[i][j].setMinSize(30, 30);
                label[i][j].setFont(Font.font ("Verdana", 10));
                label[i][j].setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                gridPane.add(label[i][j], j + 1, i + 1);

            }

        }

        String y = "#dbd039";
        String g = "#52e363";
        String b = "#7293f7";
        String p = "#e26df7";

        label[0][0].setStyle("-fx-background-color: "+y+";");
        label[0][3].setStyle("-fx-background-color: "+b+";");
        label[0][7].setStyle("-fx-background-color: "+y+";");
        label[0][11].setStyle("-fx-background-color: "+b+";");
        label[0][14].setStyle("-fx-background-color: "+y+";");

        label[1][1].setStyle("-fx-background-color: "+p+";");
        label[1][5].setStyle("-fx-background-color: "+g+";");
        label[1][9].setStyle("-fx-background-color: "+g+";");
        label[1][13].setStyle("-fx-background-color: "+p+";");

        label[2][2].setStyle("-fx-background-color: "+p+";");
        label[2][6].setStyle("-fx-background-color: "+b+";");
        label[2][8].setStyle("-fx-background-color: "+b+";");
        label[2][12].setStyle("-fx-background-color: "+p+";");

        label[3][0].setStyle("-fx-background-color: "+b+";");
        label[3][3].setStyle("-fx-background-color: "+p+";");
        label[3][7].setStyle("-fx-background-color: "+b+";");
        label[3][11].setStyle("-fx-background-color: "+p+";");
        label[3][14].setStyle("-fx-background-color: "+b+";");

        label[4][4].setStyle("-fx-background-color: "+p+";");
        label[4][10].setStyle("-fx-background-color: "+p+";");

        label[5][1].setStyle("-fx-background-color: "+g+";");
        label[5][5].setStyle("-fx-background-color: "+g+";");
        label[5][9].setStyle("-fx-background-color: "+g+";");
        label[5][13].setStyle("-fx-background-color: "+g+";");

        label[6][2].setStyle("-fx-background-color: "+b+";");
        label[6][6].setStyle("-fx-background-color: "+b+";");
        label[6][8].setStyle("-fx-background-color: "+b+";");
        label[6][12].setStyle("-fx-background-color: "+b+";");

        label[7][0].setStyle("-fx-background-color: "+y+";");
        label[7][3].setStyle("-fx-background-color: "+b+";");
        label[7][7].setStyle("-fx-background-color: "+p+";");
        label[7][11].setStyle("-fx-background-color: "+b+";");
        label[7][14].setStyle("-fx-background-color: "+y+";");

        label[8][2].setStyle("-fx-background-color: "+b+";");
        label[8][6].setStyle("-fx-background-color: "+b+";");
        label[8][8].setStyle("-fx-background-color: "+b+";");
        label[8][12].setStyle("-fx-background-color: "+b+";");

        label[9][1].setStyle("-fx-background-color: "+g+";");
        label[9][5].setStyle("-fx-background-color: "+g+";");
        label[9][9].setStyle("-fx-background-color: "+g+";");
        label[9][13].setStyle("-fx-background-color: "+g+";");

        label[10][4].setStyle("-fx-background-color: "+p+";");
        label[10][10].setStyle("-fx-background-color: "+p+";");

        label[11][0].setStyle("-fx-background-color: "+b+";");
        label[11][3].setStyle("-fx-background-color: "+p+";");
        label[11][7].setStyle("-fx-background-color: "+b+";");
        label[11][11].setStyle("-fx-background-color: "+p+";");
        label[11][14].setStyle("-fx-background-color: "+b+";");

        label[12][2].setStyle("-fx-background-color: "+p+";");
        label[12][6].setStyle("-fx-background-color: "+b+";");
        label[12][8].setStyle("-fx-background-color: "+b+";");
        label[12][12].setStyle("-fx-background-color: "+p+";");

        label[13][1].setStyle("-fx-background-color: "+p+";");
        label[13][5].setStyle("-fx-background-color: "+g+";");
        label[13][9].setStyle("-fx-background-color: "+g+";");
        label[13][13].setStyle("-fx-background-color: "+p+";");

        label[14][0].setStyle("-fx-background-color: "+y+";");
        label[14][3].setStyle("-fx-background-color: "+b+";");
        label[14][7].setStyle("-fx-background-color: "+y+";");
        label[14][11].setStyle("-fx-background-color: "+b+";");
        label[14][14].setStyle("-fx-background-color: "+y+";");

        label[7][7].setText("*");

        borderPane.setCenter(gridPane);
        borderPane.setTop(labels);
        Scene mainScene = new Scene(borderPane, 500, 550);
        stage.setTitle("Scrabble GUI Version");
        stage.setScene(mainScene);
        stage.show();

        execute();

    }

    public void setScore(int player, int computer) {

        lblPlayerScore.setText("Player Score: "+player);
        lblComputerScore.setText("Computer Score: "+computer);


    }

    public char[][] getBoardCells() {

        return boardCells;

    }

    public void setBoardCells(char[][] boardCells) {

        this.boardCells = boardCells;

    }

    public char[][] getCurrentBoardCells() {

        return currentBoardCells;

    }

    public void setCurrentBoardCells(char[][] currentBoardCells) {

        this.currentBoardCells = currentBoardCells;

    }

    public int[][] getBoardValues() {

        return boardValues;

    }

    public void setBoardValues(int[][] boardValues) {

        this.boardValues = boardValues;

    }

    public void clear() {

        for (int i = 0; i < 15; i++) {

            for (int j = 0; j < 15; j++) {

                label[i][j].setText("");

            }

        }

    }

    public void printBoardOnScreen() {

        for (int i = 0; i < boardCells.length; i++) {

            for (int j = 0; j < boardCells[0].length; j++) {

                label[i][j].setText(String.valueOf(boardCells[i][j]));

            }

        }

    }

    public boolean isBoardCorrectToBeExecuted() {

        String currentWord = "";
        int xIIndex = 0;
        int yIIndex = 0;
        int xEIndex = 0;
        int yEIndex = 0;

        for (int i = 0; i < 15; i++) {

            for (int j = 0; j < 15; j++) {

                if (currentBoardCells[i][j] != ' ') {
                    currentWord = "";
                    int tempX = i;
                    int tempY = j;

                    while (currentBoardCells[tempX][tempY] != ' ') {

                        xIIndex = tempX;
                        yIIndex = tempY;
                        tempY--;
                        if (tempY < 0)
                            break;
                    }

                    tempY = j;
                    while (currentBoardCells[tempX][tempY] != ' ') {

                        xEIndex = tempX;
                        yEIndex = tempY;
                        tempY++;
                        if (tempY > 14)
                            break;

                    }

                    for (int k = yIIndex; k <= yEIndex; k++) {
                        currentWord += currentBoardCells[i][k] + "";
                    }

                    if (!wordData.isWordExists(currentWord)) {

                        if (currentWord.length() > 1) {
                            return false;
                        }

                        if (i > 0 && i < 14) {

                            if (currentWord.length() == 1
                                    && (currentBoardCells[i - 1][j] == ' ' && currentBoardCells[i + 1][j] == ' ')) {
                                return false;
                            }

                        } else if (i == 0) {

                            if (currentWord.length() == 1 && currentBoardCells[i + 1][j] == ' ') {
                                return false;
                            }

                        } else if (i == 14) {

                            if (currentWord.length() == 1 && currentBoardCells[i - 1][j] == ' ') {
                                return false;
                            }

                        }
                    }

                    currentWord = "";

                    while (currentBoardCells[tempX][j] != ' ') {

                        xIIndex = tempX;
                        yIIndex = tempY;
                        tempX--;
                        if (tempX < 0)
                            break;

                    }

                    tempX = i;

                    while (currentBoardCells[tempX][j] != ' ') {

                        xEIndex = tempX + 1;
                        yEIndex = tempY;
                        tempX++;
                        if (tempX > 14)
                            break;

                    }

                    for (int l = xEIndex - 1; l >= xIIndex; l--) {

                        currentWord += currentBoardCells[l][j] + "";

                    }

                    if (wordData.isWordExists(currentWord) == false) {

                        if (currentWord.length() > 1) {
                            return false;
                        }

                        if (j > 0 && j < 15) {

                            if (currentWord.length() == 1 && currentBoardCells[i][j - 1] == ' '
                                    && currentBoardCells[i][j + 1] == ' ') {
                                return false;
                            }
                        } else if (j == 0) {

                            if (currentWord.length() == 1 && currentBoardCells[i][j + 1] == ' ') {
                                return false;
                            }

                        } else if (j == 15) {
                            if (currentWord.length() == 1 && currentBoardCells[i][j - 1] == ' ') {
                                return false;
                            }
                        }
                    }

                    currentWord = "";

                }

            }

        }
        return true;

    }

    public String compareWord(int initCol, int row, int value) {

        try {

            String currentWord = "";
            int startIndex = 0;
            int endIndex = 0;

            if (value == 0)
                endIndex = 14;

            int column = initCol;
            boolean flag = true;

            if (row >= 15)
                row = 14;
            else if (row < 1)
                row = 0;

            if (value == 0) {

                if (column < 1) {
                    startIndex = 0;
                    flag = false;
                } else if (column > 13) {
                    endIndex = 14;
                    flag = false;
                }

                if (row > 14)
                    row = 14;
                else if (row < 1)
                    row = 0;

                while (flag && currentBoardCells[row][column] != ' ') {

                    startIndex = column;
                    column--;
                    if (column < 1) {
                        startIndex = 0;
                        break;
                    }

                }

                column = initCol;

                if (column < 1) {
                    startIndex = 0;
                    flag = false;
                } else if (column > 13) {
                    endIndex = 14;
                    flag = false;
                }

                while (flag && currentBoardCells[row][column] != ' ') {

                    endIndex = column;
                    column++;
                    if (column > 13) {
                        endIndex = 14;
                        break;
                    } else if (column < 1) {
                        endIndex = 0;
                        break;
                    }

                }

                for (int count = startIndex; count <= endIndex; count++) {

                    currentWord += currentBoardCells[row][count] + "";

                }

            } else if (value == 1) {

                if (row > 14)
                    row = 14;
                else if (row < 1)
                    row = 0;

                if (column < 1) {
                    startIndex = 0;
                    flag = false;
                } else if (column > 13) {
                    endIndex = 14;
                    flag = false;
                }

                while (currentBoardCells[column][row] != ' ' && flag) {

                    startIndex = column;
                    column--;
                    if (column < 1) {
                        startIndex = 0;
                        break;
                    } else if (column > 13) {
                        startIndex = 14;
                        break;
                    }

                }

                column = initCol;

                if (column < 1) {
                    startIndex = 0;
                    flag = false;
                } else if (column > 13) {
                    endIndex = 14;
                    flag = false;
                }

                while (flag && currentBoardCells[column][row] != ' ') {

                    endIndex = column;
                    column++;
                    if (column > 13) {
                        endIndex = 14;
                        break;
                    } else if (column < 1) {
                        endIndex = 0;
                        break;
                    }

                }

                for (int k = endIndex; k >= startIndex; k--) {

                    currentWord += currentBoardCells[k][row] + "";

                }

            }

            System.out.println(startIndex + " " + endIndex + " Word = " + currentWord);

            if (currentWord.length() > currentWord.replace(" ", "").length()) {
                return "!";
            }

            return currentWord;

        } catch (Exception e) {
            return "!";
        }

    }

    public ArrayList<Character> retrieveLetters() {

        ArrayList<Character> letters = new ArrayList<>();

        for (int i = 0; i < 15; i++) {

            for (int j = 0; j < 15; j++) {

                if (i > 0 && i < 14 && j > 0 && j < 14) {

                    if (boardCells[i][j] != ' ') {

                        if (boardCells[i][j + 1] != ' ' && boardCells[i + 1][j] != ' ' && boardCells[i][j - 1] != ' '
                                && boardCells[i - 1][j] != ' ')
                            break;

                        if (boardCells[i][j + 1] == ' ' && boardCells[i][j - 1] == ' ')
                            letters.add(boardCells[i][j]);
                        else if (boardCells[i + 1][j] == ' ' && boardCells[i - 1][j] == ' ')
                            letters.add(boardCells[i][j]);

                    }
                }

            }

        }

        return letters;

    }

    public int findWordScore(String selectedWord, boolean extraFlag) {

        int score = 0;
        int firstValue = 1;
        int secondValue = 1;

        for (int i = 0; i < selectedWord.length(); i++) {

            if (extraFlag) {

                if (randomNumberGenerator.nextInt(100) % 4 == 0 && randomNumberGenerator.nextInt(100) % 5 == 0)
                    firstValue = 2;

                else if (randomNumberGenerator.nextInt(100) % 6 == 0 && randomNumberGenerator.nextInt(100) % 4 == 0
                        && randomNumberGenerator.nextInt(100) % 5 == 0)
                    firstValue = 3;

                else
                    firstValue = 1;

                if (firstValue != 1)
                    System.out.print("Extra Bonus Points: " + selectedWord.charAt(i) + " ! \7");

            } else
                firstValue = 1;

            switch (selectedWord.charAt(i)) {

                case 'a':
                case 'e':
                case 'i':
                case 'l':
                case 'n':
                case 'o':
                case 'r':
                case 's':
                case 't':
                case 'u':
                    score += 1 * firstValue;
                    break;

                case 'd':
                case 'g':
                    score += 2 * firstValue;
                    break;

                case 'b':
                case 'c':
                case 'm':
                case 'p':
                    score += 3 * firstValue;
                    break;

                case 'f':
                case 'h':
                case 'v':
                case 'w':
                case 'y':
                    score += 4 * firstValue;
                    break;

                case 'k':
                    score += 5 * firstValue;
                    break;

                case 'j':
                    score += 8 * firstValue;
                    break;

                case 'q':
                    score += 10 * firstValue;
                    break;

                case 'x':
                    score += 8 * firstValue;
                    break;

                case 'z':
                    score += 10 * firstValue;
                    break;

            }

        }

        if (extraFlag) {

            if (randomNumberGenerator.nextInt(100) % 5 == 0 && randomNumberGenerator.nextInt(100) % 2 == 0
                    && randomNumberGenerator.nextInt(100) % 2 == 0 && randomNumberGenerator.nextInt(100) % 2 == 0)
                secondValue = 2;
            if (randomNumberGenerator.nextInt(100) % 5 == 0 && randomNumberGenerator.nextInt(100) % 4 == 0
                    && randomNumberGenerator.nextInt(100) % 3 == 0 && randomNumberGenerator.nextInt(100) % 3 == 0)
                secondValue = 3;
            else
                secondValue = 1;
            if (randomNumberGenerator.nextInt(100) % 6 == 0 && randomNumberGenerator.nextInt(100) % 5 == 0
                    && randomNumberGenerator.nextInt(100) % 4 == 0 && randomNumberGenerator.nextInt(100) % 3 == 0
                    && randomNumberGenerator.nextInt(100) % 2 == 0) {
                secondValue = 0;
                System.out.println("You are very unlucky! You gain 0 points for this word!\7\7");
            }

            if (secondValue != 1 && secondValue != 0)
                System.out.println("Gained random bonus points! \7");

        } else
            secondValue = 1;

        return score * secondValue;

    }

    // Main method..
    public static void main(String[] args) {

        launch(args);

    }

}