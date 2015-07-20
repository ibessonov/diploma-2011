package sl.parser;

import sl.parser.nodes.Node;
import java.util.Stack;

public class SimpleParseTreeBuilder implements ParseTreeBuilder {

    private Stack<Node> stack;

    public SimpleParseTreeBuilder() {
        stack = new Stack<Node>();
    }

    @Override
    public void clear() {
        stack.clear();
    }

    @Override
    public void closeNode() {
        if (stack.size() <= 1) {
            return;
        }
        Node node = stack.pop();
        stack.lastElement().addChild(node);
    }

    @Override
    public void openNode(Node node) throws ParseException {
        stack.add(node);
    }

    @Override
    public ParseTree result() {
        return (stack.empty() ? null : new ParseTree(stack.lastElement()));
    }
}
