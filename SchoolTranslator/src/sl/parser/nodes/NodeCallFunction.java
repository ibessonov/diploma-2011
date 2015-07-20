package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeIdentifier} "(" (
 *   {@link NodeExpression} (
 *     "," {@link NodeExpression}
 *   )*
 * )?
 * ")"</pre>
 */
public class NodeCallFunction extends SimpleNode {

    public NodeCallFunction(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
