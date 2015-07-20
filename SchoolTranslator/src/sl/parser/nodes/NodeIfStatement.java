package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "если" {@link NodeExpression} "то"
 * {@link NodeBlock} (
 *   "иначе" {@link NodeBlock}
 * )?
 * "все"</pre>
 */
public class NodeIfStatement extends SimpleNode {

    public NodeIfStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
