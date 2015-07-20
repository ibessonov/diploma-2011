package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "нц" (
 *   {@link NodeForStatement}
 * | {@link NodeWhileStatement}
 * | {@link NodeRepeatStatement}
 * ) "кц"</pre>
 */
public class NodeCycleStatement extends SimpleNode {

    public NodeCycleStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
