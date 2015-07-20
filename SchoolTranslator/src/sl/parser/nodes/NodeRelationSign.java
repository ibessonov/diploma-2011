package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 *   "&gt;"
 * | "&gt;="
 * | "&lt;"
 * | "&lt;="
 * | "="
 * | "&lt;&gt;"</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "SIGN_TOKEN":
 *   - токен, содержащий знак отношения,
 *     принимает одно из значений:
 *     {@link sl.compiler.TokenKind#GREATER GREATER} {@link sl.compiler.TokenKind#GREATER_OR_EQUALS GREATER_OR_EQUALS}
 *     {@link sl.compiler.TokenKind#LESS LESS} {@link sl.compiler.TokenKind#LESS_OR_EQUALS LESS_OR_EQUALS}
 *     {@link sl.compiler.TokenKind#EQUALS EQUALS} {@link sl.compiler.TokenKind#NOT_EQUALS NOT_EQUALS}
 * </pre>
 */
public class NodeRelationSign extends SimpleNode {

    public NodeRelationSign(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
