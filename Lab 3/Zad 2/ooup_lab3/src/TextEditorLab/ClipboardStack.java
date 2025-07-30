package TextEditorLab;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {
    private Stack<String> texts = new Stack<>();

    private final List<ClipboardObserver> clipboardObservers = new ArrayList<>();

    public ClipboardStack() {

    }

    public void push(String str) {
        texts.push(str);
    }

    public String pop() {
        return isEmpty() ? "" : texts.pop();
    }

    public String peek() {
        return isEmpty() ? "" : texts.peek();
    }

    public boolean isEmpty() {
        return texts.empty();
    }

    public void clear() {
        texts.clear();
    }

    public void addClipboardObserver(ClipboardObserver obs) {
        clipboardObservers.add(obs);
    }

    public void removeClipboardObserver(ClipboardObserver obs) {
        clipboardObservers.remove(obs);
    }

    public void updateClipboardObservers() {
        for(ClipboardObserver c : clipboardObservers) {
            c.updateClipboard();
        }
    }
}
