package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * (
 *   {@link NodeType}
 *   {@link NodeVariableDeclaration} (
 *     "," {@link NodeVariableDeclaration}
 *   )*
 *   ";"
 * )*</pre>
 */
public class NodeVariables extends SimpleNode {

    public NodeVariables(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
}
