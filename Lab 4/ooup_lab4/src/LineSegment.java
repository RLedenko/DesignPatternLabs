import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGraphicalObject {

    public LineSegment() {
        super(new Point[]{new Point(0, 0), new Point(10, 0)});
    }

    public LineSegment(Point start, Point end) {
        super(new Point[]{start, end});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point start = getHotPoint(0), end = getHotPoint(1);
        return new Rectangle(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.abs(start.getX() - end.getX()), Math.abs(start.getY() - end.getY()));
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] dataPoints = data.split(" ");
        Point start = IOUtil.pointFromFileEntry(dataPoints[0], dataPoints[1]), end = IOUtil.pointFromFileEntry(dataPoints[2], dataPoints[3]);
        if(start != null)
            this.setHotPoint(0, start);
        if(end != null)
            this.setHotPoint(1, end);

        stack.push(this);
    }

    @Override
    public void save(List<String> rows) {
        rows.add(getShapeID() + " " + getHotPoint(0).getX() + " " + getHotPoint(0).getY() + " " + getHotPoint(1).getX() + " " + getHotPoint(1).getY());
    }
}
