package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeIdentifier} {@link NodeBracketsInExpression} (
 *   "." {@link NodeIdentifier} {@link NodeBracketsInExpression}
 * )*</pre>
 */
public class NodeLeftValue extends SimpleNode {

    public NodeLeftValue(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
