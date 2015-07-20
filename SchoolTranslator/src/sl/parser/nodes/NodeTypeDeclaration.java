package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *   {@link NodeType} (
 *     "таб" "[" NodeIntegerConstant ":" NodeIntegerConstant (
 *       "," NodeIntegerConstant ":" NodeIntegerConstant
 *     )*
 *     "]"
 *   )?
 * ) | (
 *   "запись" "нач" (
 *     {@link NodeType} {@link NodeIdentifier} ";"
 *   )+
 *   "кон"
 * )</pre>
 * Свойства узла:<pre>
 * {@link Boolean} "IS_RECORD"
 *   - true если объявляемый тип является записью
 *   - false иначе</pre>
 */
public class NodeTypeDeclaration extends SimpleNode {

    public NodeTypeDeclaration(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
