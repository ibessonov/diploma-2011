package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *     "арг"
 *   | "рез"
 * ) {@link NodeType} {@link NodeIdentifier}</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "PARAM_TYPE_TOKEN"
 *   - токен имеет тип {@link sl.compiler.TokenKind#ARGUMENT ARGUMENT}, если пераметр
 *     передается по значению иначе {@link sl.compiler.TokenKind#RESULT RESULT}</pre>
 */
public class NodeFormalParameter extends SimpleNode {

    public NodeFormalParameter(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
