package Model;

import Controller.IA.AILevel;
import Global.Configuration;
import View.PlayerSettings;
import View.Settings;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Scanner;

public class GameDataManager {
    static String savePath = "./save.dat";

    public static void saveMatch(Match match, Settings settings)  throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath));

        // Write lines and column
        writer.write(Integer.toString(match.nbLines));
        writer.write(' ');
        writer.write(Integer.toString(match.nbCol));
        writer.write(System.lineSeparator());

        // Write current state
        int [] currentState = match.getState();
        for (int i = 0; i < match.getNbCol(); i++) {
            writer.write(Integer.toString(currentState[i]));
            if(i < match.getNbCol() - 1)
                writer.write(' ');
        }
        writer.write(System.lineSeparator());

        // Write scores
        writer.write(Integer.toString(match.players[0].getScore()));
        writer.write(' ');
        writer.write(Integer.toString(match.players[1].getScore()));
        writer.write(System.lineSeparator());

        // Write current player index
        writer.write(Integer.toString(match.currentPlayerIndex));
        writer.write(System.lineSeparator());

        // Write player type
        writer.write(getPlayerType(settings.getPlayer1Settings()));
        writer.write(' ');
        writer.write(getPlayerType(settings.getPlayer2Settings()));
        writer.write(System.lineSeparator());

        // Write undo redo history
        int undoCount = 0;
        var pastIt = match.pastIterator();
        while (pastIt.hasNext()){
            Move move = pastIt.next();
            writer.write(Integer.toString(move.line));
            writer.write(' ');
            writer.write(Integer.toString(move.column));
            writer.write(';');
            writeState(writer, move.previousState);
            writer.write(System.lineSeparator());
            undoCount++;
        }

        var futureIt = match.futurIterator();
        while (futureIt.hasNext()){
            Move move = futureIt.next();
            writer.write(Integer.toString(move.line));
            writer.write(' ');
            writer.write(Integer.toString(move.column));
            writer.write(';');
            writeState(writer, move.previousState);
            writer.write(System.lineSeparator());
        }

        writer.write(Integer.toString(undoCount));
        writer.write(System.lineSeparator());

        writer.close();
    }

    private static void writeState(BufferedWriter writer, int[] state){
        for (int i = 0; i < state.length; i++) {
            try {
                writer.write(Integer.toString(state[i]));
                if(i < state.length - 1)
                    writer.write(' ');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static char getPlayerType(PlayerSettings settings){
        if(!settings.isAI())
            return 'J';
        else{
            switch (settings.getAiLevel()){
                case EASY -> {
                     return 'E';
                }
                case MEDIUM -> {
                    return 'M';
                }
                case HARD -> {
                    return 'H';
                }
            }
        }
        return Character.MAX_HIGH_SURROGATE;
    }

    public static void loadMatch(Game game) throws FileNotFoundException {
        File file = new File(savePath);
        Scanner scanner = new Scanner(file);

        // Read nb lines and columns
        String sizesLine = scanner.nextLine();
        String[] sizes = sizesLine.split(" ");
        int nbLines = Integer.parseInt(sizes[0]);
        int nbCol = Integer.parseInt(sizes[1]);

        Configuration.setWaffleSize(nbLines, nbCol);

        // Read current state
        String currentStateLine = scanner.nextLine();
        int[] currentState = convertToIntArray(currentStateLine.split(" "));

        // Read scores
        String scoresLine = scanner.nextLine();
        String[] scores = scoresLine.split(" ");
        int scorePlayer1 = Integer.parseInt(scores[0]);
        int scorePlayer2 = Integer.parseInt(scores[1]);

        // Read current player nbUndo
        String currentPlayerIndexLine = scanner.nextLine();
        int currentPlayerIndex = Integer.parseInt(currentPlayerIndexLine);

        // Creation du match
        game.createMatch(nbLines, nbCol, currentState, scorePlayer1, scorePlayer2, currentPlayerIndex);
        Match match = game.getMatch();

        // Read player type
        String playerTypesLine = scanner.nextLine();
        String[] playerTypes = playerTypesLine.split(" ");
        if(Objects.equals(playerTypes[0], "J")){
            Configuration.setPlayer1Settings(null);
        }
        else{
            if(Objects.equals(playerTypes[0], "E")){
                Configuration.setPlayer1Settings(AILevel.EASY);
            } else if(Objects.equals(playerTypes[0], "M")){
                Configuration.setPlayer1Settings(AILevel.MEDIUM);
            } else if(Objects.equals(playerTypes[0], "H")){
                Configuration.setPlayer1Settings(AILevel.HARD);
            }
        }

        if(Objects.equals(playerTypes[1], "J")){
            Configuration.setPlayer2Settings(null);
        }
        else{
            if(Objects.equals(playerTypes[1], "E")){
                Configuration.setPlayer2Settings(AILevel.EASY);
            } else if(Objects.equals(playerTypes[1], "M")){
                Configuration.setPlayer2Settings(AILevel.MEDIUM);
            } else if(Objects.equals(playerTypes[1], "H")){
                Configuration.setPlayer2Settings(AILevel.HARD);
            }
        }

        // Read history
        int nbUndo = 0, nbMoves = 0;
        Deque<Move> moves = new ArrayDeque<>();
        while (scanner.hasNext()) {
            String ligne = scanner.nextLine();
            String[] l = ligne.split(";");

            if(l.length == 1){
                nbUndo = Integer.parseInt(l[0]);
                break;
            }

            String[] moveCord = l[0].split(" ");
            int line = Integer.parseInt(moveCord[0]);
            int col = Integer.parseInt(moveCord[1]);

            int[] movePreviousState = convertToIntArray(l[1].split(" "));
            Move move = new Move(match, line, col, movePreviousState);
            moves.addLast(move);
            nbMoves++;
        }

        for (int i = 0; i < nbMoves; i++) {
            if(i < nbUndo){
                match.past.addLast(moves.removeFirst());
            }
            else{
                match.future.addLast(moves.removeFirst());
            }
        }
    }

    private static int[] convertToIntArray(String[] stringArray){
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return  intArray;
    }
}
