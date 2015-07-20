package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *   "-"
 * | "не"
 * )?</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "SIGN_TOKEN":
 *   - токен, содержащий знак операции,
 *     может принимать значения: {@link sl.compiler.TokenKind#MINUS MINUS}, {@link sl.compiler.TokenKind#NOT NOT} и null</pre>
 */
public class NodeUnarySign extends SimpleNode {

    public NodeUnarySign(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
	    throws ParseTreeVisitorException {
	return visitor.visit(this, data);
    }
}