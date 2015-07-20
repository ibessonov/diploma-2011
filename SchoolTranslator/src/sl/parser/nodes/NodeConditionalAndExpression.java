package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeRelationExpression} (
 *   "и" {@link NodeRelationExpression}
 * )*</pre>
 */
public class NodeConditionalAndExpression extends SimpleNode {

    public NodeConditionalAndExpression(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
