package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 *   {@link sl.compiler.TokenKind#INTEGER_LITERAL INTEGER_LITERAL}
 * | {@link sl.compiler.TokenKind#FLOATING_POINT_LITERAL FLOATING_POINT_LITERAL}
 * | {@link sl.compiler.TokenKind#CHARACTER_LITERAL CHARACTER_LITERAL}
 * | {@link sl.compiler.TokenKind#STRING_LITERAL STRING_LITERAL}
 * | "да"
 * | "нет"
 * | "(" {@link NodeExpression} ")"
 * | {@link NodeCallFunction}
 * | (
 *     {@link NodeIdentifier} {@link NodeBracketsInExpression} (
 *       "." {@link NodeIdentifier} {@link NodeBracketsInExpression}
 *     )*
 *   )</pre>
 * Свойства узла:<pre>
 * {@link sl.compiler.Token} "NAME_TOKEN":
 *   - в первых шести случаях токен, которым представлен узел,
 *     иначе null
 * </pre>
 */
public class NodeUnaryValue extends SimpleNode {

    public NodeUnaryValue(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
