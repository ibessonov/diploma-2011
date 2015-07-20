package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "возврат" (
 *   {@link NodeExpression}
 * )?</pre>
 */
public class NodeReturnStatement extends SimpleNode {

    public NodeReturnStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
