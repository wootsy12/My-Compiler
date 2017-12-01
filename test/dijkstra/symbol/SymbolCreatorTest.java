/*******************************************************************************
 * Copyright (c) 2015 Gary F. Pollice
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Used in CS4533/CS544 at Worcester Polytechnic Institute
 *******************************************************************************/

package dijkstra.symbol;

import static dijkstra.utility.DijkstraType.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.*;
import dijkstra.lexparse.DijkstraParser;
import dijkstra.utility.*;

/**
 * Description
 * @version Feb 15, 2015
 */
public class SymbolCreatorTest
{
	private DijkstraParser parser;
	private ParserRuleContext tree;
	private SymbolCreator symbolCreator;
	private SymbolTableManager stm;
	private NodePropManager npm = NodePropManager.instance;
	
	@Before
	public void setUp() throws Exception
	{
		stm = SymbolTableManager.instance;
		stm.reset();
		npm.reset();
	}

	
	// MY TESTS
	
	@Test
	public void assignDeclaresVariable()
	{
		doSymbol("a <- 5");
		assertEquals(1, stm.currentSymbolTable.getNumberOfSymbols());
		assertEquals(tUNDEFINED, stm.getBinding("a").type);
		assertEquals(1,  npm.getNodePropCount());
	}
	
	@Test
	public void assignDeclaresTwoVariables()
	{
		doSymbol("a,b <- 5,6");
		assertEquals(2, stm.currentSymbolTable.getNumberOfSymbols());
		assertEquals(tUNDEFINED, stm.getBinding("a").type);
		assertEquals(2,  npm.getNodePropCount());
	}
	
	@Test
	public void iteration()
	{
		doSymbol("int a, b; \n"
				+ "do\n"
				+ " a > b :: print true\n"
				+ " a < b :: print false\n"
				+ "od");

		Binding s = stm.getBinding("b");
		assertEquals(tINT, s.type);
	}

	@Test
	public void typeDefineThenInput()
	{
		doSymbol("boolean a; input a");
		Binding s = stm.getBinding("a");
		assertEquals(tBOOLEAN, s.type);
	}
	
	// YOUR TESTS
	
	@Test
	public void nestedScope()
	{
		doSymbol("{ int i; }");
		assertEquals(2, stm.getTables().size());
		assertEquals(1, npm.getNodePropCount());
	}
	
	@Test
	public void DeepNestedScope()
	{
		doSymbol("{{{{{a <- true}}}}}");
		assertEquals(6, stm.getTables().size());
		assertEquals(1,  npm.getNodePropCount());
	}

	@Test
	public void singleDeclaration()
	{
		doSymbol("int i");
		assertEquals(1, stm.tables.size());
		assertEquals(1, stm.currentSymbolTable.getNumberOfSymbols());
		assertNotNull(stm.getBinding("i"));
		assertEquals(tINT, stm.getBinding("i").type);
	}
	
	
	@Test(expected=DijkstraException.class)
	public void doubleDeclaration()
	{
		doSymbol("int i; int i");
	}
	
	@Test
	public void inputStatementDeclaresVariable()
	{
		doSymbol("input i");
		assertEquals(1, stm.tables.size());
		assertEquals(1, stm.currentSymbolTable.getNumberOfSymbols());
		assertNotNull(stm.getBinding("i"));
		assertEquals(1,  npm.getNodePropCount());
	}

	@Test
	public void inputStatementDoesNotAddBindingForAlreadyDeclaredSymbol()
	{
		doSymbol("boolean b; input b");
		assertEquals(1, stm.currentSymbolTable.getNumberOfSymbols());
		assertEquals(tBOOLEAN, stm.getBinding("b").type);
		assertEquals(2,  npm.getNodePropCount());
	}
	
	


	@Test
	public void assignDoesNotAddBindingForAlreadyDeclaredSymbol()
	{
		doSymbol("input a; a<- 42");
		assertEquals(1, stm.currentSymbolTable.getNumberOfSymbols());
		assertEquals(tUNDEFINED, stm.getBinding("a").type);
		assertEquals(2,  npm.getNodePropCount());
	}
	
	@Test(expected=DijkstraException.class)
	public void undefinedVariableInAssignExpression()
	{
		doSymbol("a <- b");
	}
	
	@Test(expected=DijkstraException.class)
	public void assignUndefinedVariableToItself()
	{
		doSymbol("a <- a + 1");
	}
	
	@Test(expected=DijkstraException.class)
	public void printExpressionWithUndefinedVariable()
	{
		doSymbol("print x + 42");
	}

	@Test(expected=DijkstraException.class)
	public void undefinedVariableInLoopExpression()
	{
		doSymbol("input a; do b<a b <- b + 1 od");
	}

	@Test(expected=DijkstraException.class)
	public void undefinedVariableInGuardExpression()
	{
		doSymbol("int a;\n"
				+ "if\n"
				+ "  a > 1 :: print a\n"
				+ "  b > a :: print a\n"
				+ "fi");
	}

	// Helper methods
	private void makeParser(String inputText)
	{
		parser = DijkstraFactory.makeParser(new ANTLRInputStream(inputText));
	}

	/**
	 * This method performs the parse. If you want to see what the tree looks like, use <br>
	 * <code>System.out.println(tree.toStringTree());<code></br>
	 * after calling this method.
	 * 
	 * @param inputText
	 *            the text to parse
	 */
	private String doParse(String inputText)
	{
		makeParser("program test " + inputText);
		tree = parser.dijkstraText();
		assertTrue(true);
		return tree.toStringTree(parser);
	}

	private void doSymbol(String inputText)
	{
		doParse(inputText);
		ParseTreeWalker w = new ParseTreeWalker();
		symbolCreator = new SymbolCreator();
		w.walk(symbolCreator, tree);
	}
	
	/**
	 * Call this routine to display the parse tree. Hit ENTER on the console to continue.
	 */
	private void showTree()
	{
		System.out.println(tree.toStringTree(parser));
		List<String> ruleNames = Arrays.asList(parser.getRuleNames());
		TreeViewer tv = new TreeViewer(ruleNames, tree);
		JFrame frame = new JFrame("Parse Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(tv);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        BufferedReader br = 
                new BufferedReader(new InputStreamReader(System.in));
        try {
			br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
