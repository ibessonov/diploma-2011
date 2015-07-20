package sl.parser.nodes;

import sl.parser.Token;

public interface Node {

    public void addChild(Node n);

    public Node getChild(int i);

    public int getNumChildren();

    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException;

    public Token getFirstToken();
}
