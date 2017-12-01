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

import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.*;
import dijkstra.lexparse.DijkstraParser;
import dijkstra.symbol.*;
import dijkstra.utility.*;
import static dijkstra.utility.DijkstraType.*;

/**
 * Type checker test cases.
 * @version Feb 24, 2017
 */
public class TypeCheckerTest
{
	private DijkstraParser parser;
	private ParserRuleContext tree;
	private NodePropManager npm = NodePropManager.instance;
	private SymbolTableManager stm;
	
	@Before
	public void setUp() throws Exception
	{
		stm = SymbolTableManager.instance;
		stm.reset();
		npm.reset();
	}
	
	
	// MY TESTS
	
	@Test
	public void assignSingle()
	{
		doTypeCheck("int i\n"
				+ "i <- 1");
		assertEquals(tINT, stm.getBinding("i").type);
	}

	@Test(expected=DijkstraException.class)
	public void wrongType()
	{
		doTypeCheck("int i; i <- false");
	}
	
	@Test
	public void printBool()
	{
		doTypeCheck("print true");
		assertEquals(tBOOLEAN, npm.getType(tree.getChild(0).getChild(2).getChild(0).getChild(1)));
	}
	
	@Test
	public void complexBoolExpression()
	{
		doTypeCheck("boolean a \n"
				+ "a <- 5 + 7 < 6 * 3");
		assertEquals(tBOOLEAN, stm.getBinding("a").type);
	}

	@Test
	public void complexBoolExpression2()
	{
		doTypeCheck("boolean a \n"
				+ "a <- 6 * 7 = 84 / 2");
		assertEquals(tBOOLEAN, stm.getBinding("a").type);
	}
	
	
	@Test
	public void testINTtoFLOATChange()
	{
		doTypeCheck("float a , d\n"
				+ "int b,c \n"
				+ " input b; "
				+ "a <- b "
				+ "c <- a");
		assertEquals(tFLOAT, stm.getBinding("a").type);
		
		assertEquals(tFLOAT, stm.getBinding("d").type);
		assertEquals(tFLOAT, stm.getBinding("c").type);
	}
	
	
	@Test
	public void intConstant()
	{
		doTypeCheck("i <- 42");
		assertEquals(tINT, stm.getBinding("i").type);
	}
	
	@Test
	public void floatConstant()
	{
		doTypeCheck("i <- 42.5");
		assertEquals(tFLOAT, stm.getBinding("i").type);
	}
	
	@Test
	public void checkIfInt()
	{
		doTypeCheck("int a, b; z <- a+b");
		assertEquals(tINT, stm.getBinding("z").type);
	}
	
	@Test
	public void checkifFloat()
	{
		doTypeCheck("int a; float b; z <- a+b");
		assertEquals(tFLOAT, stm.getBinding("z").type);
	}
	
	
	// YOUR TESTS
	
	@Test
	public void intDeclaration()
	{
		doTypeCheck("int i");
		assertEquals(tINT, stm.getBinding("i").type);
	}
	
	@Test
	public void floatDeclaration()
	{
		doTypeCheck("float i");
		assertEquals(tFLOAT, stm.getBinding("i").type);
	}
	
	@Test
	public void booleanTrueConstant()
	{
		doTypeCheck("print true");
		ParseTree node = tree.getChild(0).getChild(2).getChild(0).getChild(1).getChild(0);
		assertEquals(tBOOLEAN, npm.getType(node));
	}
	
	@Test
	public void booleanExpression()
	{
		doTypeCheck("print true");
		ParseTree node = tree.getChild(0).getChild(2).getChild(0).getChild(1);
		assertEquals(tBOOLEAN, npm.getType(node));
	}
	
	
	
	
	
	
	@Test
	public void impliedDeclaration()
	{
		doTypeCheck("b <- false");
		assertEquals(tBOOLEAN, stm.getBinding("b").type);
	}
	
	@Test
	public void notExpression()
	{
		doTypeCheck("print ~true");
		final ParseTree node = tree.getChild(0).getChild(2).getChild(0).getChild(1);
		assertEquals(tBOOLEAN, npm.getType(node));
	}
	
	@Test(expected=DijkstraException.class)
	public void invalidNotExpressionType()
	{
		doTypeCheck("print ~42");
	}
	
	@Test
	public void unaryMinusExpression()
	{
		doTypeCheck("print -42");
		final ParseTree node = tree.getChild(0).getChild(2).getChild(0).getChild(1);
		assertEquals(tINT, npm.getType(node));
	}
	
	@Test(expected=DijkstraException.class)
	public void invalidUnaryMinusExpressionType()
	{
		doTypeCheck("print -false");
	}
	
	@Test
	public void addTwoIntegerConstants()
	{
		doTypeCheck("i <- 40 + 2");
		assertEquals(tINT, stm.getBinding("i").type);
	}
	
	@Test(expected=DijkstraException.class)
	public void multiplyIntAndBoolean()
	{
		doTypeCheck("print 3 * false");
	}
	
	@Test(expected=DijkstraException.class)
	public void subtractBooleanFromInt()
	{
		doTypeCheck("print true - 42");
	}
	
	@Test(expected=DijkstraException.class)
	public void divideBooleanIntoInt()
	{
		doTypeCheck("print 42 / false");
	}
	
	@Test
	public void relationExpressionIsBoolean()
	{
		doTypeCheck("b <- 42 < 43");
		assertEquals(tBOOLEAN, stm.getBinding("b").type);
	}
	
	@Test(expected=DijkstraException.class)
	public void booleanLessThanInt()
	{
		doTypeCheck("print true < 1");
	}
	
	@Test(expected=DijkstraException.class)
	public void intGreaterThanBoolean()
	{
		doTypeCheck("x <- 42 > false");
	}
	
	@Test
	public void intEqualIntIsBoolean()
	{
		doTypeCheck("b <- 42 = 43");
		assertEquals(tBOOLEAN, stm.getBinding("b").type);
	}
	
	@Test
	public void booleanEqualBooleanIsBoolean()
	{
		doTypeCheck("b <- false = true");
		assertEquals(tBOOLEAN, stm.getBinding("b").type);
	}
	
	@Test(expected=DijkstraException.class)
	public void intNotEqualBoolean()
	{
		doTypeCheck("print 42 ~= false");
	}
	
	@Test(expected=DijkstraException.class)
	public void booleanEqualInt()
	{
		doTypeCheck("print true = 42");
	}
	
	@Test
	public void parenthesizedInt()
	{
		doTypeCheck("i <- (42)");
		assertEquals(tINT, stm.getBinding("i").type);
	}
	
	@Test
	public void multipleOperationIntExpression()
	{
		doTypeCheck("i <- 42 * 3 - 90 + 24 / 4");
		assertEquals(tINT, stm.getBinding("i").type);
	}
	
	@Test
	public void relationAndEquality()
	{
		doTypeCheck("b <- 42 > 41 = 42 < 43");
		assertEquals(tBOOLEAN, stm.getBinding("b").type);
	}
	
	@Test
	public void definedIdInExpression()
	{
		doTypeCheck("int i\nj <- i");
		assertEquals(tINT, stm.getBinding("i").type);
		assertEquals(tINT, stm.getBinding("j").type);
	}
	
	@Test
	public void inferredIdInExpression()
	{
		doTypeCheck("b <- true\n b1 <- ~b");
		assertEquals(tBOOLEAN, stm.getBinding("b").type);
		assertEquals(tBOOLEAN, stm.getBinding("b1").type);
		assertTrue(stm.allSymbolsAreTyped());
	}
	
	@Test
	public void inferredIdAssignedToSelf()
	{
		doTypeCheck("input i\n i <- i + 1");
		assertEquals(tINT, stm.getBinding("i").type);
	}
	
	@Test
	public void twoInputVariables()
	{
		doTypeCheck("input i; input j; k <- i / j");
		assertEquals(tINT, stm.getBinding("i").type);
		assertEquals(tINT, stm.getBinding("j").type);
		assertEquals(tINT, stm.getBinding("k").type);
	}
	
	@Test
	public void mixedTypeInferredExpression()
	{
		doTypeCheck("input i; input j; k <- i + j < i - j");
		assertEquals(tINT, stm.getBinding("i").type);
		assertEquals(tINT, stm.getBinding("j").type);
		assertEquals(tBOOLEAN, stm.getBinding("k").type);
	}

	// Helper methods.
	private void doTypeCheck(String inputText)
	{
		DijkstraCompiler compiler = new DijkstraCompiler();
		tree = compiler.doTypeCheck(new ANTLRInputStream("program test " + inputText));
		parser = compiler.getParser();
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
	
	private void printTree()
	{
		System.out.println(tree.toStringTree(parser));
	}
}
