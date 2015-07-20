package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeConditionalAndExpression} (
 *   "или" {@link NodeConditionalAndExpression}
 * )*</pre>
 */
public class NodeExpression extends SimpleNode {

    public NodeExpression(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
