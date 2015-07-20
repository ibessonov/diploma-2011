package sl.parser;

import sl.parser.nodes.Node;
import sl.parser.nodes.ParseTreeVisitor;
import sl.parser.nodes.ParseTreeVisitorException;

public class ParseTree {

    private Node root;

    public ParseTree(Node node) {
        root = node;
    }

    public void traverse(ParseTreeVisitor visitor)
            throws ParseTreeVisitorException {
        root.accept(visitor, null);
    }

    public void traverse(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        root.accept(visitor, data);
    }
}
