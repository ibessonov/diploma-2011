package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link NodeBlock}
 * "пока" {@link NodeExpression}</pre>
 */
public class NodeRepeatStatement extends SimpleNode {

    public NodeRepeatStatement(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
	    throws ParseTreeVisitorException {
	return visitor.visit(this, data);
    }
}