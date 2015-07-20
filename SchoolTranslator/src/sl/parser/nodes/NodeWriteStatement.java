package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "вывод" {@link NodeExpression} (
 *   "," {@link NodeExpression}
 * )*</pre>
 */
public class NodeWriteStatement extends SimpleNode {

    public NodeWriteStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
