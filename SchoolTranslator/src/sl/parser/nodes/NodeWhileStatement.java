package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "пока" {@link NodeExpression}
 * {@link NodeBlock}</pre>
 */
public class NodeWhileStatement extends SimpleNode {

    public NodeWhileStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
