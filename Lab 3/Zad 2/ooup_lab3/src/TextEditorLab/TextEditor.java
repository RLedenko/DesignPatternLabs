package TextEditorLab;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

public class TextEditor extends JComponent {
    private final TextEditorModel model;
    private Location cursorLocation;
    private Location oldCursor;
    public ClipboardStack clipboardStack;
    TextEditorFrame containerFrame;

    private static final int LINE_HEIGHT = 20;

    private boolean switchCaseIsArrowKey(int code) {
        return code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN;
    }

    public TextEditor(TextEditorModel model, TextEditorFrame containerFrame) {
        this.model = model;
        this.containerFrame = containerFrame;
        this.cursorLocation = model.cursorLocation;
        clipboardStack = new ClipboardStack();

        setFocusable(true);
        setFont(new Font("Monospaced", Font.PLAIN, 14));

        model.addCursorObserver(loc -> {
            cursorLocation = loc;
            repaint();
        });

        model.addTextObserver(this::repaint);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean shift = e.isShiftDown(), ctrl = e.isControlDown();
                int code = e.getKeyCode();
                if(!shift || oldCursor == null)
                    oldCursor = model.cursorLocation.copy();


                switch (code) {
                    case KeyEvent.VK_LEFT -> model.moveCursorLeft();
                    case KeyEvent.VK_RIGHT -> model.moveCursorRight();
                    case KeyEvent.VK_UP -> model.moveCursorUp();
                    case KeyEvent.VK_DOWN -> model.moveCursorDown();
                    case KeyEvent.VK_BACK_SPACE -> model.deleteBefore();
                    case KeyEvent.VK_DELETE -> model.deleteAfter();
                }

                if(ctrl) {
                    if(shift)
                        switch (code) {
                            case KeyEvent.VK_V -> pasteAndTakeAction.actionPerformed(null);
                        }
                    else
                        switch (code) {
                            case KeyEvent.VK_C -> copyAction.actionPerformed(null);
                            case KeyEvent.VK_X -> cutAction.actionPerformed(null);
                            case KeyEvent.VK_V -> pasteAction.actionPerformed(null);
                            case KeyEvent.VK_Z -> undoAction.actionPerformed(null);
                            case KeyEvent.VK_Y -> redoAction.actionPerformed(null);
                        }
                }

                if(!shift)
                    oldCursor = null;

                if(shift && switchCaseIsArrowKey(code)) {
                    model.setSelectionRange(new LocationRange(oldCursor, model.cursorLocation.copy()));
                }
                else if(!shift && switchCaseIsArrowKey(code)) {
                    model.setSelectionRange(null);
                }
                containerFrame.updateLabel();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(c == 8 || c == 127) return;

                if(c == '\n') {
                    model.insert("\n");
                }
                else if(!Character.isISOControl(c)) {
                    model.insert(c);
                }
                containerFrame.updateLabel();
            }
        });
    }

    private void extendSelection(int dx, int dy) {
        Location old = model.cursorLocation.copy();

        if(dx == -1) model.moveCursorLeft();
        else if(dx == 1) model.moveCursorRight();
        else if(dy == -1) model.moveCursorUp();
        else if(dy == 1) model.moveCursorDown();

        Location newLoc = model.cursorLocation.copy();
        model.setSelectionRange(new LocationRange(old, newLoc));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics();
        int y = LINE_HEIGHT;

        LocationRange sel = model.getSelectionRange();
        if(sel != null) {
            Location start = sel.getMin();
            Location end = sel.getMax();
            g.setColor(new Color(173, 216, 230));

            for(int i = start.line; i <= end.line; i++) {
                String line = model.getLine(i);
                int lineLen = line.length();

                int x1 = (i == start.line) ? fm.stringWidth(line.substring(0, Math.min(start.column, lineLen))) : 0;
                int x2 = (i == end.line) ? fm.stringWidth(line.substring(0, Math.min(end.column, lineLen))) : fm.stringWidth(line);

                int height = LINE_HEIGHT;
                int drawY = i * LINE_HEIGHT + 5;
                g.fillRect(10 + x1, drawY, x2 - x1, height);
            }
        }

        g.setColor(Color.BLACK);
        Iterator<String> it = model.allLines();
        int lineNum = 0;
        while(it.hasNext()) {
            String line = it.next();
            g.drawString(line, 10, (lineNum + 1) * LINE_HEIGHT);
            lineNum++;
        }

        g.setColor(Color.RED);
        String line = model.getLine(cursorLocation.line);
        int cursorX = 10 + fm.stringWidth(line.substring(0, Math.min(cursorLocation.column, line.length())));
        int cursorY = cursorLocation.line * LINE_HEIGHT + 5;
        g.drawLine(cursorX, cursorY, cursorX, cursorY + LINE_HEIGHT - 5);
    }

    // FILE
    Action openAction = new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
            if(fileChooser.showOpenDialog(TextEditor.this) == JFileChooser.APPROVE_OPTION)
                model.loadFile(fileChooser.getSelectedFile().getPath());
        }
    };

    Action saveAction = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
            if(fileChooser.showSaveDialog(TextEditor.this) == JFileChooser.APPROVE_OPTION)
                model.saveFile(fileChooser.getSelectedFile().getPath() + (fileChooser.getSelectedFile().getPath().contains(".txt") ? "" : ".txt"));
        }
    };

    Action exitAction = new AbstractAction("Exit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    // EDIT
    Action copyAction = new AbstractAction("Copy") {
        @Override
        public void actionPerformed(ActionEvent e) {
            clipboardStack.push(model.selectionRangeAsString());
            containerFrame.updateLabel();
        }
    };

    Action cutAction = new AbstractAction("Cut") {
        @Override
        public void actionPerformed(ActionEvent e) {
            clipboardStack.push(model.selectionRangeAsString());
            model.deleteRange(model.selectionRange);
            containerFrame.updateLabel();
        }
    };

    Action pasteAction = new AbstractAction("Paste") {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.insert(clipboardStack.peek());
            containerFrame.updateLabel();
        }
    };


    Action pasteAndTakeAction = new AbstractAction("Paste and take") {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.insert(clipboardStack.pop());
            containerFrame.updateLabel();
        }
    };

    Action undoAction = new AbstractAction("Undo") {
        @Override
        public void actionPerformed(ActionEvent e) {
            UndoManager.getInstance().undo();
            containerFrame.updateLabel();
        }
    };

    Action redoAction = new AbstractAction("Redo") {
        @Override
        public void actionPerformed(ActionEvent e) {
            UndoManager.getInstance().redo();
            containerFrame.updateLabel();
        }
    };

    Action deleteSelectionAction = new AbstractAction("Delete selection") {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.deleteRange(model.selectionRange);
            containerFrame.updateLabel();
        }
    };

    Action clearAction = new AbstractAction("Clear document") {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.clear();
            model.moveCursorToStart();
            containerFrame.updateLabel();
        }
    };

    // MOVE
    Action documentStartAction = new AbstractAction("Cursor to document start") {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.moveCursorToStart();
            containerFrame.updateLabel();
        }
    };

    Action documentEndAction = new AbstractAction("Cursor to document end") {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.moveCursorToEnd();
            containerFrame.updateLabel();
        }
    };
}
