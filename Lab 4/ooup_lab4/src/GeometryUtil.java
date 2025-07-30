public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        double dx = e.getX() - s.getX();
        double dy = e.getY() - s.getY();

        if (dx == 0 && dy == 0)
            return distanceFromPoint(p, s);

        double t = ((p.getX() - s.getX()) * dx + (p.getY() - s.getY()) * dy) / (dx * dx + dy * dy);

        if (t < 0)
            return distanceFromPoint(p, s);
        else if (t > 1)
            return distanceFromPoint(p, e);
        else {
            double projX = s.getX() + t * dx;
            double projY = s.getY() + t * dy;
            return Math.hypot(p.getX() - projX, p.getY() - projY);
        }
    }
}