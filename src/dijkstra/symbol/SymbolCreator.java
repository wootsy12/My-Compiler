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

package dijkstra.symbol;

import static dijkstra.lexparse.DijkstraParser.ID;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;
import dijkstra.lexparse.*;
import dijkstra.utility.*;
import static dijkstra.utility.DijkstraType.*;

/**
 * This class is a tree walker that walks the parse tree and creates symbol table entries
 * for 
 * @version Feb 16, 2017
 */
public class SymbolCreator extends DijkstraBaseListener
{
	private final SymbolTableManager stm;
	private final NodePropManager npm;
	private int expressionDepth = 0;
	private final boolean DEBUG = false;
	
	/**
	 * Default constructor. Ensures that we have the SymbolTableManager and
	 * the NodeProp map.
	 */
	public SymbolCreator()
	{
		stm = SymbolTableManager.instance;
		npm = NodePropManager.instance;
	}
	
	public void enterCompoundStatement(DijkstraParser.CompoundStatementContext ctx)
	{
		stm.enterScope();
	}
	
	public void exitScope(DijkstraParser.CompoundStatementContext ctx)
	{
		stm.exitScope();
	}
	
	public void exitDeclaration(DijkstraParser.DeclarationContext decl)
	{
		int temp=0;
		if(decl.idList().getChildCount()>1)
			temp=1;

		// get the lexeme representing the type
		final DijkstraType type = DijkstraType.getType(decl.t.getText());
		// get the ID lexeme
		for(int i=0;i<decl.idList().getChildCount()-temp;i++) {
			String id = decl.idList().ID(i).getText();
			

			Binding b = stm.add(new Binding(id, type));	// throws DijkstraException if already there.
			npm.setBinding(decl.idList().ID(i), b);				// the ID has the binding and type 
		}
		
	}
	
	public void exitInputStatement(DijkstraParser.InputStatementContext in)
	{
		
		Binding b = stm.addIfNew(new Binding(in.idList().ID(0).getText()));

		npm.setBinding(in.idList().ID(0), b);					// the ID has the binding and type
	}
	
	public void enterAssignStatement(DijkstraParser.AssignStatementContext assign)
	{
		debug("enterAssignment: " + assign.getText());

	}
	
	public void exitAssignStatement(DijkstraParser.AssignStatementContext assign)
	{
		
		int temp=0;
		if(assign.idList().getChildCount()>1)
			temp=1;
		for(int i=0;i<assign.idList().getChildCount()-temp;i++) {
			Binding b = stm.addIfNew(new Binding(assign.idList().ID(i).getText()));
			if(stm.getBinding(assign.getChild(2).getText())!=null) {

				if(stm.getBinding(assign.getChild(2).getText()).type==tFLOAT && stm.getBinding(assign.getChild(0).getText()).type==tINT) {
					b.type=tFLOAT;
				}
			}
			
			


			npm.setBinding(assign.idList().ID(i), b);
		}
		
		
	}
	
	public void enterExpression(DijkstraParser.ExpressionContext expr)
	{
		expressionDepth++;
	}
	
	public void exitExpression(DijkstraParser.ExpressionContext expr)
	{
		expressionDepth--;
	}
	
	public void visitTerminal(TerminalNode tn)
	{
		final Token t = tn.getSymbol();
		if (t.getType() == ID && expressionDepth > 0) {
			Binding b = stm.getBinding(t.getText());
			if (b == null) {
				throw new DijkstraException("Attempt to use ID: " + t.getText()
					+ " before it is defined");
			}
			// Important: See video 10-5
			npm.getType(tn);	// Force the creation of the node property object
			npm.setBinding(tn, b);  // Set the binding
			npm.setType(tn, b.type);
		}
	}
	
	// For Debugging
	private void debug(String s)
	{
		if (DEBUG)
			System.out.println("DEBUG: " + s);
	}
}
