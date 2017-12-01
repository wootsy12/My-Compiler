/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package dijkstra.typecheck;

import static dijkstra.lexparse.DijkstraParser.*;
import static dijkstra.typecheck.TypeCheckRules.*;
import static dijkstra.utility.DijkstraType.*;
import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import dijkstra.lexparse.*;
import dijkstra.lexparse.DijkstraParser.*;
import dijkstra.symbol.Binding;
import dijkstra.utility.*;

/**
 * Type checking visitor.
 * @version Feb 24, 2017
 */
public class DijkstraTypeChecker extends DijkstraBaseVisitor<DijkstraType>
{
	private final NodePropManager npm;
	private final boolean DEBUG = false;
	public DijkstraParser parser = null;
	public Deque<DijkstraType> typeNeededStack = new ArrayDeque<DijkstraType>();
	
	public DijkstraTypeChecker()
	{
		npm = NodePropManager.instance;
	}
	
	public DijkstraType visitAssignStatement(AssignStatementContext assign)
	{

		DijkstraType exprType = assign.expressionList().expression(0).accept(this);
		DijkstraType idType = visitTerminalNode(assign.idList().ID(0));


		switch (idType) {
			case tUNDEFINED:
				debug("Setting " + assign.idList().getText() + " to " + exprType);
				npm.setType(assign.idList().ID(0), exprType);
				break;
			default:	 // TODO unify the types
				if (!exactTypeMatchRule.test(idType, exprType) && !numerableTypeMatchRule.test(idType, exprType)) {
					throw new DijkstraException("Invalid assign statment");
				}
		}
		return null;
	}
	
	public DijkstraType visitOutputStatement(OutputStatementContext print)
	{
		return print.expression().accept(this);
	}
	
	public DijkstraType visitExpression(ExpressionContext expr)
	{
		DijkstraType exprType = null;
		switch (expr.getChildCount()) {
			case 1:
				// set the node type to the child type
				TerminalNode node = (TerminalNode)expr.getChild(0);
				exprType = visitTerminalNode(node);
				break;
			case 2:
				exprType = typeCheckUnaryOperatorExpression(expr);
				break;
			case 3:		// expr1 op expr2 or ( expr )
				if (expr.getChild(0) instanceof TerminalNode) {	// '('
					exprType = expr.expression(0).accept(this);
				} else {
					exprType = typeCheckBinaryOperatorExpression(expr);
				}
				break;
			default:
				throw new DijkstraException("Invalid expression node");
		}
		npm.setType(expr, exprType);
		return exprType;
	}

	/**
	 * Determine if the unary operator had the right expression.
	 * @param expr the expression the unary operator applies to
	 * @return the type of the resulting expression
	 */
	private DijkstraType typeCheckUnaryOperatorExpression(ExpressionContext expr)
	{
		DijkstraType exprType;
		final int uop = ((TerminalNode)expr.getChild(0)).getSymbol().getType();
		
		final DijkstraType uopType = uop == TILDE ? tBOOLEAN : tINT;
		
		// push tBinary on neededStack
		typeNeededStack.push(uopType);
		exprType = expr.expression(0).accept(this);	// walk the first (only) expression
		// pop neededStack
		typeNeededStack.pop();
		if (!exactTypeMatchRule.test(uopType, exprType)) {
			throw new DijkstraException("Type mismatch on unary operator ["
					+ uopType + "," + exprType + "]");
		}
		return exprType;
	}

	/**
	 * We have a binary operator expression left op right
	 * @param expr the expression node
	 * @param type
	 * @return the expression type
	 */
	private DijkstraType typeCheckBinaryOperatorExpression(ExpressionContext expr)
	{
		DijkstraType exprType = null;
		final int bop = ((TerminalNode)expr.getChild(1)).getSymbol().getType();
		// Push type needed onto the stack
		typeNeededStack.push(bopType(bop));
		final DijkstraType leftType = expr.expression(0).accept(this);
		final DijkstraType rightType = expr.expression(1).accept(this);
		typeNeededStack.pop();
		switch (bop) {
			case PLUS:	case MINUS:	case STAR:	case SLASH:

				if (!numerableTypeMatchRule.test(leftType, rightType)) {
					throw new DijkstraException("Invalid arithmetic expression:  "
							+ leftType + expr.getChild(1).getText() + rightType);
				}
				if(exactTypeMatchRule.test(leftType, rightType))
					exprType = tINT;
				else
					exprType = tFLOAT;
				break;
			case LT:		case GT:
				if (!relationalTypeRule.test(leftType, rightType)) {
					throw new DijkstraException("Invalid relational expression: "
							+ leftType + expr.getChild(1).getText() + rightType);
				}
				exprType = tBOOLEAN;
				break;
			case EQ:		case NEQ:
				if (!equalityTypeRule.test(leftType, rightType)) {
					throw  new DijkstraException("Invalid equality expression: "
							+ leftType + expr.getChild(1).getText() + rightType);
				}
				exprType = tBOOLEAN;
				break;
			default:
				throw new DijkstraException("Invalid binary operator");
		}
		return exprType;
	}
	
	private DijkstraType bopType(int bop)
	{
		DijkstraType type = null;
		switch (bop) {
			case PLUS:	case MINUS:	case STAR:	case SLASH:
				type = tINT;
				break;
			default:
				type = tBOOLEAN;
		}
		return type;
	}
	
	public DijkstraType visitTerminalNode(TerminalNode node)
	{
		DijkstraType type = null;
		switch (node.getSymbol().getType()) {
			case INTEGER:
				type = tINT;
				break;
			case FLOATCONST:
				type = tFLOAT;
				break;
			case TRUE:
			case FALSE:
				type = tBOOLEAN;
				break;
			case ID:
				type = npm.getType(node);
				Binding b = npm.getBinding(node);
				if (type == tUNDEFINED & !typeNeededStack.isEmpty()) {
					type = typeNeededStack.peek();
				}
				break;
			default:
				throw new DijkstraException("Invalid terminal node type");
		}
		npm.setType(node, type);
		return type;
	}
	
	// For Debugging
	private void debug(String s)
	{
		if (DEBUG)
			System.out.println("DEBUG: " + s);
	}
}
