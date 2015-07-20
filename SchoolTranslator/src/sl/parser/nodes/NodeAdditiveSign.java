package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 *   "+"
 * | "-"</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "SIGN_TOKEN":
 *   - токен, содержащий знак операции,
 *     может принимать 2 значения: {@link sl.compiler.TokenKind#PLUS PLUS} и {@link sl.compiler.TokenKind#MINUS MINUS}
 * </pre>
 */
public class NodeAdditiveSign extends SimpleNode {

    public NodeAdditiveSign(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
