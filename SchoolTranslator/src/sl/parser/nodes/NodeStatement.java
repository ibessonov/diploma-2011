package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 *   {@link NodeIfStatement}
 * | {@link NodeSwitchStatement}
 * | {@link NodeCycleStatement}
 * | {@link NodeReadStatement}
 * | {@link NodeWriteStatement}
 * | {@link NodeReturnStatement}
 * | {@link NodeCallFunction}
 * | {@link NodeAssignment}</pre>
 * Свойства узла:<pre>
 * Integer "LINE_NUMBER":
 *   - номер строки, на которой начинается утверждение</pre>
 */
public class NodeStatement extends SimpleNode {

    public NodeStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
