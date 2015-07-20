package sl.parser.nodes;

import sl.parser.ParseTree;

/**
 * Реализация паттерна Visitor для класса {@link ParseTree}.
 */
public abstract class ParseTreeVisitor {

    protected abstract Object visit(NodeStart node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeTypes node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeTypeDeclaration node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeIntegerConstant node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeType node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeFunctions node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeFormalParameter node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeVariables node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeIdentifier node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeVariableDeclaration node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeBlock node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeReadStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeWriteStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeSwitchStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeCycleStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeForStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeWhileStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeRepeatStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeReturnStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeIfStatement node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeCallFunction node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeLeftValue node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeBracketsInExpression node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeAssignment node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeExpression node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeConditionalAndExpression node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeRelationExpression node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeRelationSign node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeAdditiveExpression node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeAdditiveSign node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeMultiplicativeExpression node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeMultiplicativeSign node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeUnaryExpression node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeUnarySign node, Object data)
            throws ParseTreeVisitorException;

    protected abstract Object visit(NodeUnaryValue node, Object data)
            throws ParseTreeVisitorException;
}
