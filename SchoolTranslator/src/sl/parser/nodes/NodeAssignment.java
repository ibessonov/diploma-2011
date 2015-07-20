package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeLeftValue} ":=" {@link NodeExpression}</pre>
 */
public class NodeAssignment extends SimpleNode {

    public NodeAssignment(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
