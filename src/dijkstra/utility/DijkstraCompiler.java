/*******************************************************************************
 * This file is used in CS4533/CS544, Compiler Construction & Techniques of Language
 * Translation, Worcester Polytechnic Institute. Copyright (c) 2016-17 Gary F. Pollice All
 * rights reserved. This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and
 * is available at http://www.eclipse.org/legal/epl-v10.html Copyright ©2016 Gary F.
 * Pollice
 *******************************************************************************/

package dijkstra.utility;

import static org.junit.Assert.assertTrue;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import dijkstra.lexparse.DijkstraParser;
import dijkstra.symbol.SymbolCreator;
import dijkstra.typecheck.DijkstraTypeChecker;

/**
 * This is what the DijkstraFactory will create.
 * 
 * @version Feb 26, 2017
 */
public class DijkstraCompiler
{
	private DijkstraParser parser;
	private ParserRuleContext tree;

	/**
	 * Creates a new compiler. Modify this class for your compiler.
	 */
	public DijkstraCompiler()
	{
		// do nothing for my implementation
	}

	/**
	 * This method performs the parse. If you want to see what the tree looks like, use
	 * <br>
	 * <code>System.out.println(tree.toStringTree());<code></br>
	 * after calling this method.
	 * 
	 * @param inputText
	 *            the text to parse
	 * @return the parse tree
	 */
	public ParserRuleContext doParse(ANTLRInputStream inputText)
	{
		parser = DijkstraFactory.makeParser(inputText);
		tree = parser.dijkstraText();
		assertTrue(true);
		return tree;
	}

	/**
	 * Perform through symbol table creation
	 * @param inputText
	 * @return the parse tree
	 */
	public ParserRuleContext doSymbol(ANTLRInputStream inputText)
	{
		doParse(inputText);
		ParseTreeWalker w = new ParseTreeWalker();
		SymbolCreator symbolCreator = new SymbolCreator();
		w.walk(symbolCreator, tree);
		return tree;
	}

	/**
	 * Perform through type checking
	 * @param inputText
	 * @return the parse tree
	 */
	public ParserRuleContext doTypeCheck(ANTLRInputStream inputText)
	{
		doSymbol(inputText);
		DijkstraTypeChecker checker = new DijkstraTypeChecker();
		tree.accept(checker);
		return tree;
	}
	
	/**
	 * @return the current parse tree
	 */
	public ParserRuleContext getTree()
	{
		return tree;
	}
	
	/**
	 * @return the current parser
	 */
	public DijkstraParser getParser()
	{
		return  parser;
	}
}
