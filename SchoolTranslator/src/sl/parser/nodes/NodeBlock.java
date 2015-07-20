package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *   {@link NodeStatement} ";"
 * )+</pre>
 */
public class NodeBlock extends SimpleNode {

    public NodeBlock(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
