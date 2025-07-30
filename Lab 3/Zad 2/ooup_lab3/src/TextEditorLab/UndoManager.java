package TextEditorLab;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UndoManager {

    static UndoManager instance;

    Stack<EditAction> undoStack = new Stack<>();
    Stack<EditAction> redoStack = new Stack<>();

    List<UndoObserver> undoObservers = new ArrayList<>();

    private UndoManager() {

    }

    public void notifyUndoObservers() {
        for(UndoObserver o : undoObservers)
            o.updateStacks(undoStack.isEmpty(), redoStack.isEmpty());
    }

    public static UndoManager getInstance() {
        if(instance == null)
            instance = new UndoManager();
        return instance;
    }

    public void undo() {
        if(undoStack.isEmpty()) return;
        EditAction c = undoStack.pop();
        redoStack.push(c);
        c.execute_undo();
        notifyUndoObservers();
    }

    public void redo() {
        if(redoStack.isEmpty()) return;
        EditAction c = redoStack.pop();
        c.execute_do();
        undoStack.push(c);
        notifyUndoObservers();
    }

    public void push(EditAction c) {
        redoStack.clear();
        undoStack.push(c);
        notifyUndoObservers();
    }

    public boolean canUndo() {
        return undoStack.isEmpty();
    }

    public boolean canRedo() {
        return redoStack.isEmpty();
    }
}
