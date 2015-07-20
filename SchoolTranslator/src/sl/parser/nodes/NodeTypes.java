package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *   "тип" {@link NodeIdentifier} "=" {@link NodeTypeDeclaration} ";"
 * )*</pre>
 */
public class NodeTypes extends SimpleNode {

    public NodeTypes(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
	    throws ParseTreeVisitorException {
	return visitor.visit(this, data);
    }
}