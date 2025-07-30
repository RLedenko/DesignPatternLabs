package TextEditorLab;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class TextEditorFrame extends JFrame {
    private final TextEditorModel model;
    private final TextEditor editor;
    private final JLabel statusLabel;

    public TextEditorFrame(TextEditorModel model) {
        super("OOUP LAB 3");

        this.model = model;
        editor = new TextEditor(model, this);

        statusLabel = new JLabel("");
        add(statusLabel, BorderLayout.SOUTH);
        updateLabel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
        editor.requestFocusInWindow();
        add(new JScrollPane(editor), BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();


        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(editor.openAction));
        fileMenu.add(new JMenuItem(editor.saveAction));
        fileMenu.add(new JMenuItem(editor.exitAction));
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JMenuItem(editor.undoAction));
        editMenu.add(new JMenuItem(editor.redoAction));
        editMenu.add(new JMenuItem(editor.cutAction));
        editMenu.add(new JMenuItem(editor.copyAction));
        editMenu.add(new JMenuItem(editor.pasteAction));
        editMenu.add(new JMenuItem(editor.pasteAndTakeAction));
        editMenu.add(new JMenuItem(editor.deleteSelectionAction));
        editMenu.add(new JMenuItem(editor.clearAction));
        menuBar.add(editMenu);

        JMenu moveMenu = new JMenu("Move");
        moveMenu.add(new JMenuItem(editor.documentStartAction));
        moveMenu.add(new JMenuItem(editor.documentEndAction));
        menuBar.add(moveMenu);

        JMenu pluginsMenu = new JMenu("Plugins");
        loadPlugins(pluginsMenu);
        menuBar.add(pluginsMenu);

        setJMenuBar(menuBar);
    }

    public void updateLabel() {
        Location loc = model.getCursorLocation();
        int lineCount = model.getLines().size();
        statusLabel.setText(String.format(" Line: %d, Column: %d | Total lines: %d", loc.line + 1, loc.column + 1, lineCount));
    }

    private void loadPlugins(JMenu pluginsMenu) {
        File pluginDir = new File("plugins");
        if(!pluginDir.exists() || !pluginDir.isDirectory()) return;

        File[] jars = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if(jars == null) return;

        for(File jarFile : jars) {
            try {
                URL[] urls = { jarFile.toURI().toURL() };
                URLClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());

                try(JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile))) {
                    JarEntry entry;
                    while((entry = jarStream.getNextJarEntry()) != null) {
                        if (entry.getName().endsWith(".class")) {
                            String className = entry.getName().replace("/", ".").replace(".class", "");
                            Class<?> clazz = Class.forName(className, true, loader);

                            if (Plugin.class.isAssignableFrom(clazz)) {
                                Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                                JMenuItem pluginItem = new JMenuItem(plugin.getName());
                                pluginItem.setToolTipText(plugin.getDescription());
                                pluginItem.addActionListener(e -> plugin.execute(model, UndoManager.getInstance(), editor.clipboardStack));
                                pluginsMenu.add(pluginItem);
                            }
                        }
                    }
                }
            }
            catch (Exception ignored) {

            }
        }
    }
}
