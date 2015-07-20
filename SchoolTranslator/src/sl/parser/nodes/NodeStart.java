package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * "алг" {@link NodeIdentifier} ";"
 * {@link NodeTypes}
 * {@link NodeFunctions}
 * {@link NodeVariables}
 * "нач" {@link NodeBlock} "кон"
 * &lt;конец файла&gt;</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "NAME_TOKEN":
 *   - токен {@link sl.compiler.TokenKind#ALGORITHM ALGORITHM}</pre>
 */
public class NodeStart extends SimpleNode {

    public NodeStart(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
	    throws ParseTreeVisitorException {
	return visitor.visit(this, data);
    }
}