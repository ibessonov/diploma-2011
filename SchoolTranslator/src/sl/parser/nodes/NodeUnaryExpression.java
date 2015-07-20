package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeUnarySign} {@link NodeUnaryValue}</pre>
 */
public class NodeUnaryExpression extends SimpleNode {

    public NodeUnaryExpression(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
