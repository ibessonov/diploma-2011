package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 *   "цел"
 * | "вещ"
 * | "лит"
 * | "стр"
 * | "лог"
 * | {@link sl.compiler.TokenKind#IDENTIFIER IDENTIFIER}
 * </pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "NAME_TOKEN":
 *   - токен, содержащий имя типа
 * </pre>
 */
public class NodeType extends SimpleNode {

    public NodeType(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
