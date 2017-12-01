/*******************************************************************************
 * This file is used in CS4533/CS544, Compiler Construction & Techniques of
 * Language Translation, Worcester Polytechnic Institute.
 *
 * Copyright (c) 2016-17 Gary F. Pollice
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package dijkstra.lexparse;

import static org.junit.Assert.*;
import org.antlr.v4.runtime.*;
import org.junit.*;

import static dijkstra.lexparse.DijkstraLexer.*;

/**
 * Test cases for the Toy Dijkstra lexer.
 */
public class DijkstraLexerTest
{
	private DijkstraLexer lexer;
	private Token t;
	
	@Test
	public void recognizeReservedWords()
	{
		makeLexer("boolean false fi if input int do od print program true");
		checkNextToken(BOOLEAN, "boolean");
		checkNextToken(FALSE, "false");
		checkNextToken(FI, "fi");
		checkNextToken(IF, "if");
		checkNextToken(INPUT, "input");
		checkNextToken(INT, "int");
		checkNextToken(DO, "do");
		checkNextToken(OD, "od");
		checkNextToken(PRINT, "print");
		checkNextToken(PROGRAM, "program");
		checkNextToken(TRUE, "true");
	}
	
	@Test
	public void recognizeSeparators()
	{
		makeLexer(";()::");
		checkNextToken(SEMICOLON, ";");
		checkNextToken(LPAR, "(");
		checkNextToken(RPAR, ")");
		checkNextToken(GUARD, "::");
		nextToken();
		assertEquals(EOF, t.getType());
	}
	
	@Test
	public void recognizeAssignOp()
	{
		makeLexer("<-");
		checkNextToken(ASSIGN, "<-");
	}
	
	@Test
	public void recognizeEqualityOperators()
	{
		makeLexer("= ~=");
		checkNextToken(EQ, "=");
		checkNextToken(NEQ, "~=");
	}
	
	@Test
	public void recognizeRelationalOperators()
	{
		makeLexer("< >");
		checkNextToken(LT, "<");
		checkNextToken(GT, ">");
	}
	
	@Test
	public void recognizeArithmetOperators()
	{
		makeLexer("+-*/");
		checkNextToken(PLUS, "+");
		checkNextToken(MINUS, "-");
		checkNextToken(STAR, "*");
		checkNextToken(SLASH, "/");
	}
	
	@Test
	public void recognizeLogicalNot()
	{
		makeLexer("~");
		checkNextToken(TILDE, "~");
	}
	
	@Test
	public void recognizeSimpleIDs()
	{
		makeLexer("hello Test1 var_1 isOpen?");
		checkNextToken(ID, "hello");
		checkNextToken(ID, "Test1");
		checkNextToken(ID, "var_1");
		checkNextToken(ID, "isOpen?");
	}
	
	@Test
	public void recognizeIntegers()
	{
		makeLexer("42 12345678901234567890");
		checkNextToken(INTEGER, "42");
		checkNextToken(INTEGER, "12345678901234567890");
	}
	
	@Test
	public void recognizeMixed()
	{
		makeLexer("42hello world_215");
		checkNextToken(INTEGER, "42");
		checkNextToken(ID, "hello");
		checkNextToken(ID, "world_215");
		nextToken();
		assertEquals(EOF, t.getType());
	}
	
	@Test(expected=RecognitionException.class)
	public void invalidCharacterThrowsException()
	{
		makeLexer("hello%");
//		lexer.removeErrorListeners();
		lexer.addErrorListener(new TestListener());
		checkNextToken(ID, "hello");
		nextToken();
	}
	
	// Helper methods
	private void makeLexer(String text)
	{
		lexer = new DijkstraLexer(new ANTLRInputStream(text));
	}

	private void nextToken()
	{
		t = lexer.nextToken();
	}

	private void checkNextToken(int tokenType, String tokenText)
	{
		nextToken();
		assertEquals(tokenType, t.getType());
		assertEquals(tokenText, t.getText());
	}
}

class TestListener extends BaseErrorListener
{
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
			int line, int charPositionInLine, String msg, RecognitionException e)
	{
		throw e;
	}
}
