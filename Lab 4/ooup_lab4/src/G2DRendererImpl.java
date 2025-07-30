import java.awt.*;
import java.util.Arrays;

public class G2DRendererImpl implements Renderer {

    private Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
    }

    @Override
    public void fillPolygon(Point[] points) {
        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(Arrays.stream(points).mapToInt(Point::getX).toArray(), Arrays.stream(points).mapToInt(Point::getY).toArray(), Oval.ovalPointCount);
        g2d.setColor(Color.RED);
        g2d.drawPolygon(Arrays.stream(points).mapToInt(Point::getX).toArray(), Arrays.stream(points).mapToInt(Point::getY).toArray(), Oval.ovalPointCount);
    }

}