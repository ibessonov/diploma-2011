package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *   "алг" (
 *     {@link NodeType}
 *   )?
 *   {@link NodeIdentifier} "(" (
 *     {@link NodeFormalParameter} (
 *       "," {@link NodeFormalParameter}
 *     )*
 *   )?
 *   ")"
 *   {@link NodeVariables}
 *   "нач" {@link NodeBlock} "кон" ";"
 * )*</pre>
 */
public class NodeFunctions extends SimpleNode {

    public NodeFunctions(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
