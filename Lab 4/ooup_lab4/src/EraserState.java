import java.util.ArrayList;
import java.util.List;

public class EraserState implements State {

    private DocumentModel model;
    private List<Point> points;

    public EraserState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        points = new ArrayList<>();
        points.add(new Point(mousePoint.getX(), mousePoint.getY()));
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        for(Point p : points) {
            GraphicalObject potential = model.findintersectingGraphicalObject(p);
            if(potential != null)
                model.removeGraphicalObject(potential);
        }
        points.clear();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        Point prev = points.getLast();

        int dx = mousePoint.getX() - prev.getX();
        int dy = mousePoint.getY() - prev.getY();

        int steps = 1 + (int)Math.sqrt(dx * dx + dy * dy);

        for(int i = 1; i < steps; i++)
            points.add(new Point(prev.getX() + i * dx / steps, prev.getY() + i * dy / steps));
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {
        if(points != null)
            for(int i = 0; i < points.size() - 1; i++)
                r.drawLine(points.get(i), points.get(i + 1));
    }

    @Override
    public void onLeaving() {

    }
}
