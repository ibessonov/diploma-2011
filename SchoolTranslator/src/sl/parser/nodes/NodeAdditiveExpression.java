package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeMultiplicativeExpression}
 * (
 *   {@link NodeAdditiveSign} {@link NodeMultiplicativeExpression}
 * )*</pre>
 */
public class NodeAdditiveExpression extends SimpleNode {

    public NodeAdditiveExpression(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
