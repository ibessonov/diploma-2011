package sl.parser.nodes;

import java.util.ArrayList;
import java.util.List;
import sl.parser.Token;

public abstract class SimpleNode implements Node {

    protected Token firstToken;
    protected List<Node> children = new ArrayList<Node>();

    protected SimpleNode(Token firstToken) {
        this.firstToken = firstToken;
    }

    @Override
    public void addChild(Node node) {
        children.add(node);
    }

    @Override
    public Node getChild(int index) {
        return children.get(index);
    }

    @Override
    public int getNumChildren() {
        return children.size();
    }

    @Override
    public Token getFirstToken() {
        return firstToken;
    }
}
