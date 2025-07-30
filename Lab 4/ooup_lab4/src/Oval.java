import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGraphicalObject {

    public static final int ovalPointCount = 30;

    public Oval() {
        super(new Point[]{new Point(10, 0), new Point(0, 10)});
    }

    public Oval(Point right, Point down) {
        super(new Point[]{right, down});
    }

    public Point[] generateRenderPoints() {
        Point right = getHotPoint(0);
        Point down = getHotPoint(1);

        int center_x = down.getX(), center_y = right.getY(), radius_x = Math.abs(down.getX() - right.getX()), radius_y = Math.abs(down.getY() - right.getY());

        Point[] renderPoints = new Point[ovalPointCount];
        for(int i = 0; i < ovalPointCount; i++) {
            double angle = 2. * Math.PI * i / ovalPointCount;
            renderPoints[i] = new Point((int)(center_x + radius_x * Math.cos(angle)), (int)(center_y + radius_y * Math.sin(angle)));
        }

        return renderPoints;
    }

    @Override
    public Rectangle getBoundingBox() {
        Point right = getHotPoint(0);
        Point down = getHotPoint(1);

        int center_x = down.getX(), center_y = right.getY(), radius_x = Math.abs(down.getX() - right.getX()), radius_y = Math.abs(down.getY() - right.getY());

        return new Rectangle(center_x - radius_x, center_y - radius_y, 2 * radius_x, 2 * radius_y);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Point right = getHotPoint(0);
        Point down = getHotPoint(1);

        int center_x = down.getX(), center_y = right.getY(), radius_x = Math.abs(down.getX() - right.getX()), radius_y = Math.abs(down.getY() - right.getY());

        int translated_x = mousePoint.getX() - center_x, translated_y = mousePoint.getY() - center_y;

        double normalized_x = (double)(translated_x) / radius_x, normalized_y = (double)(translated_y) / radius_y;
        double dist_normal = Math.sqrt(normalized_x * normalized_x + normalized_y * normalized_y);

        if(dist_normal <= 1) return 0.;

        double dist = Math.sqrt(translated_x * translated_x + translated_y * translated_y);

        double angle = Math.atan2(normalized_y, normalized_x);
        double internal_x = Math.cos(angle) * radius_x, internal_y = Math.sin(angle) * radius_y;
        double internal = Math.sqrt(internal_x * internal_x + internal_y * internal_y);

        return Math.abs(dist - internal);
    }

    @Override
    public void render(Renderer r) {
        r.fillPolygon(generateRenderPoints());
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] dataPoints = data.split(" ");
        Point right = IOUtil.pointFromFileEntry(dataPoints[0], dataPoints[1]), down = IOUtil.pointFromFileEntry(dataPoints[2], dataPoints[3]);
        if(right != null)
            this.setHotPoint(0, right);
        if(down != null)
            this.setHotPoint(1, down);

        stack.push(this);
    }

    @Override
    public void save(List<String> rows) {
        rows.add(getShapeID() + " " + getHotPoint(0).getX() + " " + getHotPoint(0).getY() + " " + getHotPoint(1).getX() + " " + getHotPoint(1).getY());
    }
}
