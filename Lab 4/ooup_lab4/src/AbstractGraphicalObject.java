import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {

    private Point[] hotPoints;
    private boolean[] hotPointsSelected;
    private boolean selected;

    List<GraphicalObjectListener> listeners = new ArrayList<>();

    public AbstractGraphicalObject(Point[] hotPoints) {
        this.hotPoints = hotPoints;
        hotPointsSelected = new boolean[hotPoints.length];
        notifySelectionListeners();
    }

    public Point getHotPoint(int idx) {
        return hotPoints[idx];
    }

    public void setHotPoint(int idx, Point hotPoint) {
        hotPoints[idx] = hotPoint;
        notifyListeners();
    }

    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    public double getHotPointDistance(int idx, Point point) {
        return GeometryUtil.distanceFromPoint(hotPoints[idx], point);
    }

    public boolean isHotPointSelected(int idx) {
        return hotPointsSelected[idx];
    }

    public void setHotPointSelected(int idx, boolean mode) {
        hotPointsSelected[idx] = mode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    public void translate(Point dp) {
        for(int i = 0; i < hotPoints.length; i++)
            hotPoints[i] = hotPoints[i].translate(dp);
        notifyListeners();
    }

    public void addGraphicalObjectListener(GraphicalObjectListener gol) {
        listeners.add(gol);
    }

    public void removeGraphicalObjectListener(GraphicalObjectListener gol) {
        listeners.remove(gol);
    }

    protected void notifyListeners() {
        for(GraphicalObjectListener gol : listeners)
            gol.graphicalObjectChanged(this);
    }

    protected void notifySelectionListeners() {
        for(GraphicalObjectListener gol : listeners)
            gol.graphicalObjectSelectionChanged(this);
    }
}
