import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer {

    private List<String> lines = new ArrayList<>();
    private String fileName;

    public SVGRendererImpl(String fileName) {
        this.fileName = fileName;
        lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"800\" height=\"600\">");
    }

    public void close() throws IOException {
        lines.add("</svg>");
        File file = new File(fileName);
        FileOutputStream fos = null;

        fos = new FileOutputStream(file, false);
        for(String line : lines)
            fos.write((line + "\n").getBytes());

        fos.close();
    }

    @Override
    public void drawLine(Point s, Point e) {
        lines.add("<line x1=\"" + s.getX() + "\" y1=\"" + s.getY() + "\" x2=\"" + e.getX() + "\" y2=\"" + e.getY() + "\" stroke=\"blue\" />");
    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder poly = new StringBuilder("<polygon points=\"");
        for(Point p : points)
            poly.append(pointToSVGString(p)).append(" ");
        poly.append("\" fill=\"blue\" stroke=\"red\" />");

        lines.add(poly.toString());
    }

    private String pointToSVGString(Point p) {
        return p.getX() + "," + p.getY();
    }
}
