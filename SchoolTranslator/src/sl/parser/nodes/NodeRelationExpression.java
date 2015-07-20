package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeAdditiveExpression} (
 *   {@link NodeRelationSign} {@link NodeAdditiveExpression}
 * )?</pre>
 */
public class NodeRelationExpression extends SimpleNode {

    public NodeRelationExpression(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
