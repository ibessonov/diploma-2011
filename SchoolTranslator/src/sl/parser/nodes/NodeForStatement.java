package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "для" {@link NodeLeftValue}
 *   "от" {@link NodeAdditiveExpression}
 *   "до" {@link NodeAdditiveExpression}
 * {@link NodeBlock}</pre>
 */
public class NodeForStatement extends SimpleNode {

    public NodeForStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
