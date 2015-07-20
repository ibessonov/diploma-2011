package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "выбор" {@link NodeExpression} (
 *   "при" {@link NodeIntegerConstant} ":" {@link NodeBlock}
 * )+
 * (
 *   "иначе" ":" {@link NodeBlock}
 * )?
 * "все"</pre>
 */
public class NodeSwitchStatement extends SimpleNode {

    public NodeSwitchStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
	    throws ParseTreeVisitorException {
	return visitor.visit(this, data);
    }
}