package Model;

public record Coordinate(int col, int line) {
    @Override
    public String toString() {
        char col = (char) (65 + col());
        return "(" + line + ", " + col + ")";
    }
}
