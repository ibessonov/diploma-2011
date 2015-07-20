/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sl.parser.nodes;

import sl.parser.Token;

/**
 * Строение узла:<pre>
 * ("-")? INTEGER_LITERAL</pre>
 */
public class NodeIntegerConstant extends SimpleNode {

    public NodeIntegerConstant(Token firstToken) {
        super(firstToken);
    }

    @Override
    public Object accept(ParseTreeVisitor visitor, Object data)
            throws ParseTreeVisitorException {
        return visitor.visit(this, data);
    }
    
}
