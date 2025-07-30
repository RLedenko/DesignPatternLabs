package TextEditorLab;

public class LocationRange {
    private final Location start;
    private final Location end;

    public LocationRange(Location start, Location end) {
        this.start = start;
        this.end = end;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public Location getMin() {
        return (start.compareTo(end) <= 0) ? start : end;
    }

    public Location getMax() {
        return (start.compareTo(end) >= 0) ? start : end;
    }
}
