package TextEditorLab;

public class Location implements Comparable<Location> {
    public int line;
    public int column;

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public Location copy() {
        return new Location(line, column);
    }

    @Override
    public int compareTo(Location other) {
        if(this.line != other.line)
            return Integer.compare(this.line, other.line);
        return Integer.compare(this.column, other.column);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Location other)) return false;
        return this.line == other.line && this.column == other.column;
    }

    @Override
    public int hashCode() {
        return 31 * line + column;
    }
}