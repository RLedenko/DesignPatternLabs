import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {

    private List<GraphicalObject> objects;
    private DocumentModel model;
    private Canvas canvas;
    private State currentState;

    public GUI(List<GraphicalObject> objects) {
        super();

        currentState = new IdleState();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        this.objects = objects;
        model = new DocumentModel();
        canvas = new Canvas(model, currentState);

        JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton(new AbstractAction("Učitaj") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = askForFileName("", "Load");
                List<String> lines = loadAndReadFile(path);
                if(lines == null) {
                    JOptionPane.showMessageDialog(GUI.this, "Greška prilikom učitavanja!");
                    return;
                }

                model.clear();
                Stack<GraphicalObject> stack = new Stack<>();
                for(String line : lines) {
                    int firstSpace = line.indexOf(" ");
                    String tag = line.substring(0, firstSpace), params = line.substring(firstSpace + 1);
                    GraphicalObject go = GraphicalObjectFactory.getInstance().generate(tag);
                    go.load(stack, params);
                }

                Stack<GraphicalObject> inverted_stack = new Stack<>();
                while(!stack.isEmpty())
                    inverted_stack.push(stack.pop());

                while(!inverted_stack.isEmpty())
                    model.addGraphicalObject(inverted_stack.pop());

                canvas.repaint();
            }
        }));
        toolBar.add(new JButton(new AbstractAction("Pohrani") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = askForFileName("export.txt", "Save");
                List<String> rows = new ArrayList<>();
                for(GraphicalObject go : model.list())
                    go.save(rows);
                if(!writeToFile(path, rows))
                    JOptionPane.showMessageDialog(GUI.this, "Greška prilikom učitavanja!");
            }
        }));
        toolBar.add(new JButton(new AbstractAction("SVG export") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = askForFileName("export.svg", "Save");
                SVGRendererImpl r = new SVGRendererImpl(path);
                for(GraphicalObject o : model.list())
                    o.render(r);
                try {
                    r.close();
                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(GUI.this, "Greška prilikom spremanja!");
                }
            }
        }));

        for(GraphicalObject o : objects) {
            toolBar.add(new JButton(new AbstractAction(o.getShapeName()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateState(new AddShapeState(model, o));
                }
            }));
            GraphicalObjectFactory.getInstance().register(o.getShapeID(), o);
        }
        GraphicalObjectFactory.getInstance().register("@COMP", new CompositeShape(new ArrayList<>()));

        toolBar.add(new JButton(new AbstractAction("Selektiraj") {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateState(new SelectShapeState(model));
            }
        }));
        toolBar.add(new JButton(new AbstractAction("Briši") {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateState(new EraserState(model));
            }
        }));

        add(toolBar, BorderLayout.NORTH);

        canvas.setFocusable(true);

        add(canvas, BorderLayout.CENTER);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                switch(code) {
                    case KeyEvent.VK_ESCAPE -> updateState(new IdleState());
                }

                currentState.keyPressed(code);
                canvas.repaint();
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                canvas.requestFocusInWindow();
                currentState.mouseDown(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
                canvas.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentState.mouseUp(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
                canvas.repaint();
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentState.mouseDragged(new Point(e.getX(), e.getY()));
                canvas.repaint();
            }
        });

        setVisible(true);
        canvas.repaint();
    }

    private void updateState(State newState) {
        currentState.onLeaving();
        canvas.repaint();
        currentState = newState;
        canvas.updateState(newState);
    }

    private String askForFileName(String defaultName, String goal) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Odaberi direktorij");
        fileChooser.setSelectedFile(new File(defaultName));

        if(fileChooser.showDialog(this, goal) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getPath();

        return null;
    }

    private List<String> loadAndReadFile(String fileName) {
        List<String> lines = new ArrayList<>();

        Scanner scanner = null;
        try {
            File file = new File(fileName);
            scanner = new Scanner(file);
        }
        catch (Exception e) {
            return null;
        }

        while(scanner.hasNextLine())
            lines.add(scanner.nextLine());

        scanner.close();

        return lines;
    }

    private boolean writeToFile(String fileName, List<String> lines) {
        FileOutputStream fos = null;
        try {
            File file = new File(fileName);
            fos = new FileOutputStream(file, false);
            for(String line : lines)
                fos.write((line + "\n").getBytes());

            fos.close();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
