package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * {@link sl.compiler.TokenKind#IDENTIFIER IDENTIFIER}</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "NAME_TOKEN":
 *   - токен, содержащий идентификатор</pre>
 */
public class NodeIdentifier extends SimpleNode {

    public NodeIdentifier(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
