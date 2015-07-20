package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *   "[" {@link NodeAdditiveExpression} (
 *     "," {@link NodeAdditiveExpression}
 *   )*
 *   "]"
 * )?</pre>
 */
public class NodeBracketsInExpression extends SimpleNode {

    public NodeBracketsInExpression(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
