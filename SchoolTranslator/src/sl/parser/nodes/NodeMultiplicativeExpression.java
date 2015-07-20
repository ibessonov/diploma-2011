package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeUnaryExpression} (
 *   {@link NodeMultiplicativeSign} {@link NodeUnaryExpression}
 * )*</pre>
 */
public class NodeMultiplicativeExpression extends SimpleNode {

    public NodeMultiplicativeExpression(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
