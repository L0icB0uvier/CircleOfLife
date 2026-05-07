package Model;

public record Coordinate(int col, int line) {
    @Override
    public String toString() {
//        return "(" + col + ", " + line + ")";
        char col = (char) (65 + col());
        return "(" + (line + 1) + ", " + col + ")";
    }
}
