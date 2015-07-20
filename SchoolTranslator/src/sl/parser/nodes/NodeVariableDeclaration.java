package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 *   {@link NodeIdentifier}
 * | (
 *     "таб" {@link NodeIdentifier}
 *     "[" {@link NodeIntegerConstant} ":" {@link NodeIntegerConstant} (
 *       "," {@link NodeIntegerConstant} ":" {@link NodeIntegerConstant}
 *     )*
 *     "]"
 *   )</pre>
 * * Свойства узла:<pre>
 * {@link Boolean} "IS_TABLE"
 *   - true если объявляемая переменная является массивом,
 *     false иначе
 * </pre>
 */
public class NodeVariableDeclaration extends SimpleNode {

    public NodeVariableDeclaration(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
