package TextEditorLab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class TextEditorModel {
    public List<String> lines;
    public Location cursorLocation;
    public LocationRange selectionRange;

    private final List<CursorObserver> cursorObservers = new ArrayList<>();
    private final List<TextObserver> textObservers = new ArrayList<>();

    public TextEditorModel(String text) {
        this.lines = new ArrayList<>(Arrays.asList(text.split("\n")));
        this.cursorLocation = new Location(0, 0);
        this.selectionRange = null;
    }

    public void addCursorObserver(CursorObserver obs) {
        cursorObservers.add(obs);
    }

    public void removeCursorObserver(CursorObserver obs) {
        cursorObservers.remove(obs);
    }

    public void addTextObserver(TextObserver obs) {
        textObservers.add(obs);
    }

    public void removeTextObserver(TextObserver obs) {
        textObservers.remove(obs);
    }

    void notifyCursorObservers() {
        for(CursorObserver obs : cursorObservers)
            obs.updateCursorLocation(cursorLocation.copy());
    }

    private void notifyTextObservers() {
        for(TextObserver obs : textObservers)
            obs.updateText();
    }

    public void moveCursor(Location l) {
        cursorLocation.line = l.line;
        cursorLocation.column = l.column;
        notifyCursorObservers();
    }

    public void moveCursorToStart() {
        Location prev = cursorLocation.copy();

        cursorLocation.line = 0;
        cursorLocation.column = 0;
        notifyCursorObservers();

        UndoManager.getInstance().push(new CursorMoveAction(this, prev, cursorLocation.copy()));
    }

    public void moveCursorToEnd() {
        Location prev = cursorLocation.copy();

        cursorLocation.line = lines.size() - 1;
        cursorLocation.column = lines.getLast().length();
        notifyCursorObservers();

        UndoManager.getInstance().push(new CursorMoveAction(this, prev, cursorLocation.copy()));
    }

    public void moveCursorLeft() {
        Location prev = cursorLocation.copy();

        if(cursorLocation.column > 0)
            cursorLocation.column--;
        else if(cursorLocation.line > 0) {
            cursorLocation.line--;
            cursorLocation.column = lines.get(cursorLocation.line).length();
        }
        else return;

        UndoManager.getInstance().push(new CursorMoveAction(this, prev, cursorLocation.copy()));
        notifyCursorObservers();
    }

    public void moveCursorRight() {
        Location prev = cursorLocation.copy();

        String line = lines.get(cursorLocation.line);
        if(cursorLocation.column < line.length())
            cursorLocation.column++;
        else if(cursorLocation.line < lines.size() - 1) {
            cursorLocation.line++;
            cursorLocation.column = 0;
        }
        else return;

        UndoManager.getInstance().push(new CursorMoveAction(this, prev, cursorLocation.copy()));
        notifyCursorObservers();
    }

    public void moveCursorUp() {
        if(cursorLocation.line > 0) {
            Location prev = cursorLocation.copy();

            cursorLocation.line--;
            cursorLocation.column = Math.min(cursorLocation.column, lines.get(cursorLocation.line).length());

            UndoManager.getInstance().push(new CursorMoveAction(this, prev, cursorLocation.copy()));
            notifyCursorObservers();
        }
    }

    public void moveCursorDown() {
        if(cursorLocation.line < lines.size() - 1) {
            Location prev = cursorLocation.copy();

            cursorLocation.line++;
            cursorLocation.column = Math.min(cursorLocation.column, lines.get(cursorLocation.line).length());

            UndoManager.getInstance().push(new CursorMoveAction(this, prev, cursorLocation.copy()));
            notifyCursorObservers();
        }
    }

    public void deleteBefore() {
        if(hasSelection()) {
            LocationRange range = selectionRange;
            String deletedText = selectionRangeAsString();
            UndoManager.getInstance().push(new DeleteRangeAction(this, deletedText, range));
            deleteRange(range);
            return;
        }

        if(cursorLocation.line == 0 && cursorLocation.column == 0) return;

        String deletedChar = "";
        Location deletedAt = cursorLocation.copy();

        if(cursorLocation.column > 0)
            deletedChar = lines.get(cursorLocation.line).substring(cursorLocation.column - 1, cursorLocation.column);
        else
            deletedChar = "\n";

        UndoManager.getInstance().push(new DeleteBeforeAction(this, deletedChar, deletedAt));

        if(cursorLocation.column > 0) {
            String line = lines.get(cursorLocation.line);
            String newLine = line.substring(0, cursorLocation.column - 1) + line.substring(cursorLocation.column);
            lines.set(cursorLocation.line, newLine);
            cursorLocation.column--;
        }
        else {
            String currentLine = lines.remove(cursorLocation.line);
            cursorLocation.line--;
            String prevLine = lines.get(cursorLocation.line);
            cursorLocation.column = prevLine.length();
            lines.set(cursorLocation.line, prevLine + currentLine);
        }
        notifyCursorObservers();
        notifyTextObservers();
    }

    public void deleteAfter() {
        if(hasSelection()) {
            deleteRange(selectionRange);
            return;
        }

        String line = lines.get(cursorLocation.line);
        if(cursorLocation.column < line.length()) {
            String newLine = line.substring(0, cursorLocation.column) + line.substring(cursorLocation.column + 1);
            lines.set(cursorLocation.line, newLine);
        }
        else if(cursorLocation.line < lines.size() - 1) {
            String nextLine = lines.remove(cursorLocation.line + 1);
            lines.set(cursorLocation.line, line + nextLine);
        }
        else return;
        notifyTextObservers();
    }

    public void deleteRange(LocationRange r) {
        Location start = r.getMin();
        Location end = r.getMax();
        StringBuilder deleted = new StringBuilder();

        if(start.line == end.line) {
            String line = lines.get(start.line);
            deleted.append(line, start.column, end.column);

            String newLine = line.substring(0, start.column) + line.substring(end.column);
            lines.set(start.line, newLine);
        }
        else {
            String first = lines.get(start.line);
            String last = lines.get(end.line);

            deleted.append(first.substring(start.column)).append("\n");
            for(int i = start.line + 1; i < end.line; i++)
                deleted.append(lines.get(i)).append("\n");
            deleted.append(last, 0, end.column);

            String newLine = first.substring(0, start.column) + last.substring(end.column);
            lines.set(start.line, newLine);
            for (int i = end.line; i > start.line; i--)
                lines.remove(i);
        }
        UndoManager.getInstance().push(new DeleteRangeAction(this, deleted.toString(), r));

        cursorLocation = start.copy();
        selectionRange = null;

        notifyCursorObservers();
        notifyTextObservers();
    }

    public void insertWithoutUndo(char c, Location loc) {
        String line = lines.get(loc.line);
        String newLine = line.substring(0, loc.column) + c + line.substring(loc.column);
        lines.set(loc.line, newLine);
        cursorLocation = new Location(loc.line, loc.column + 1);
        notifyCursorObservers();
        notifyTextObservers();
    }

    public void insertWithoutUndo(String text, Location loc) {
        if(hasSelection()) deleteRangeWithoutUndo(selectionRange);

        String[] insertLines = text.split("\n", -1);
        String currentLine = lines.get(loc.line);
        String before = currentLine.substring(0, loc.column);
        String after = currentLine.substring(loc.column);

        if(insertLines.length == 1) {
            lines.set(loc.line, before + insertLines[0] + after);
            cursorLocation = new Location(loc.line, loc.column + insertLines[0].length());
        }
        else {
            lines.set(loc.line, before + insertLines[0]);
            for (int i = 1; i < insertLines.length - 1; i++) {
                lines.add(loc.line + i, insertLines[i]);
            }
            lines.add(loc.line + insertLines.length - 1, insertLines[insertLines.length - 1] + after);
            cursorLocation = new Location(loc.line + insertLines.length - 1, insertLines[insertLines.length - 1].length());
        }

        notifyCursorObservers();
        notifyTextObservers();
    }

    public void deleteRangeWithoutUndo(LocationRange r) {
        Location start = r.getMin();
        Location end = r.getMax();
        if(start.line == end.line) {
            String line = lines.get(start.line);
            String newLine = line.substring(0, start.column) + line.substring(end.column);
            lines.set(start.line, newLine);
        }
        else {
            String first = lines.get(start.line).substring(0, start.column);
            String last = lines.get(end.line).substring(end.column);
            lines.set(start.line, first + last);
            for(int i = end.line; i > start.line; i--)
                lines.remove(i);
        }
        cursorLocation = start.copy();
        selectionRange = null;
        notifyCursorObservers();
        notifyTextObservers();
    }

    public void insert(char c) {
        if(hasSelection()) deleteRange(selectionRange);

        Location insertLoc = cursorLocation.copy();

        String line = lines.get(cursorLocation.line);
        String newLine = line.substring(0, cursorLocation.column) + c + line.substring(cursorLocation.column);
        lines.set(cursorLocation.line, newLine);
        cursorLocation.column++;

        UndoManager.getInstance().push(new InsertTextAction(this, String.valueOf(c), insertLoc));

        notifyCursorObservers();
        notifyTextObservers();
    }

    public void insert(String text) {
        if(hasSelection()) deleteRange(selectionRange);
        String[] insertLines = text.split("\n", -1);
        String currentLine = lines.get(cursorLocation.line);
        String before = currentLine.substring(0, cursorLocation.column);
        String after = currentLine.substring(cursorLocation.column);

        Location insertLoc = cursorLocation.copy();

        if(insertLines.length == 1) {
            lines.set(cursorLocation.line, before + insertLines[0] + after);
            cursorLocation.column += insertLines[0].length();
        }
        else {
            lines.set(cursorLocation.line, before + insertLines[0]);
            for (int i = 1; i < insertLines.length - 1; i++)
                lines.add(cursorLocation.line + i, insertLines[i]);
            lines.add(cursorLocation.line + insertLines.length - 1, insertLines[insertLines.length - 1] + after);
            cursorLocation.line += insertLines.length - 1;
            cursorLocation.column = insertLines[insertLines.length - 1].length();
        }

        UndoManager.getInstance().push(new InsertTextAction(this, text, insertLoc));

        notifyCursorObservers();
        notifyTextObservers();
    }

    public LocationRange getSelectionRange() {
        return selectionRange;
    }

    public void setSelectionRange(LocationRange range) {
        this.selectionRange = range;
        notifyTextObservers();
    }

    public boolean hasSelection() {
        return selectionRange != null && !selectionRange.getMin().equals(selectionRange.getMax());
    }

    public Iterator<String> allLines() {
        return lines.iterator();
    }

    public Iterator<String> linesRange(int index1, int index2) {
        return lines.subList(index1, index2).iterator();
    }

    public String getLine(int index) {
        if(index < 0 || index >= lines.size()) return "";
        return lines.get(index);
    }

    public int getLineCount() {
        return lines.size();
    }

    public void mergeLineWithPrevious(int idx) {
        lines.set(idx - 1, lines.get(idx - 1).concat(lines.get(idx)));
        lines.remove(idx);
        notifyCursorObservers();
    }


    public String selectionRangeAsString() {
        if(!hasSelection())
            return "";
        StringBuilder accu = new StringBuilder();

        Location start = selectionRange.getMin();
        Location end = selectionRange.getMax();


        if(start.line != end.line)
        {
            accu.append(lines.get(start.line).substring(start.column)).append("\n");
            for(int i = start.line + 1; i <= end.line - 1; i++)
                accu.append(lines.get(i)).append("\n");
            accu.append(lines.get(end.line).substring(0, end.column));
        }
        else
            accu.append(lines.get(start.line).substring(start.column, end.column));


        return accu.toString();
    }

    public Location getCursorLocation() {
        return cursorLocation.copy();
    }

    public List<String> getLines() {
        return new ArrayList<>(lines);
    }

    public void loadFile(String path) {
        File file = new File(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        }
        catch(FileNotFoundException e) {
            return;
        }

        moveCursorToStart();
        lines.clear();
        while(scanner.hasNextLine())
            lines.add(scanner.nextLine());

        scanner.close();
    }

    public void saveFile(String path) {
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            for(String line : lines)
                fos.write((line + "\n").getBytes());

            fos.close();
        }
        catch(IOException ignored) {

        }
    }

    public void clear() {
        deleteRange(new LocationRange(new Location(0, 0), new Location(lines.size() - 1, lines.getLast().length())));
    }


}
