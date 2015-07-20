package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 *   "*"
 * | "/"</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "SIGN_TOKEN":
 *   - токен, содержащий знак операции,
 *     может принимать 2 значения: {@link sl.compiler.TokenKind#MULTIPLY MULTIPLY} и {@link sl.compiler.TokenKind#DIVIDE DIVIDE}
 * </pre>
 */
public class NodeMultiplicativeSign extends SimpleNode {

    public NodeMultiplicativeSign(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
