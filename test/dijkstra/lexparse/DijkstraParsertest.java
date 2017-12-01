package dijkstra.lexparse;

import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.junit.Test;
import dijkstra.utility.DijkstraFactory;

public class DijkstraParsertest
{
	private DijkstraParser parser;
	private ParserRuleContext tree;
	
	@Test
	public void minimalProgram()
	{
		doParse("int i");
	}
	
	@Test
	public void twoDeclarations()
	{
		doParse("int i\nboolean j");
	}
/*
	@Test
	public void fibonacci()
	{
		String text =
				"\n"
				+ "	 i<-0"
				+ "  int f1\n"
				+ "  int f2\n"
				+ "  input n\n"
				+ "  f1 <- 1 f2 <- 1 "
				+ "  if\n"
				+ "    n < 3 :: print 1\n"
				+ "    n > 2 :: n <- n - 2\n"
				+ "  fi\n"
				+ "  do \n"
				+ "    i<n::t <- f1\n"
				+ "    f1 <- f2\n"
				+ "    f2 <- t + f1\n"
				+ "  i<-i+1\n"
				+ "	 od"
				+ "  print f2";
		doParse(text);
		showTree();
		assertTrue(true);
	}
*/
	@Test
	public void demo()
	{
		doParse("input i; i <- i + 1");
//		showTree();
	}

	// Helper methods
	private void makeParser(String inputText)
	{
		parser = DijkstraFactory.makeParser(new ANTLRInputStream(inputText));
	}

	/**
	 * This method performs the parse. If you want to see what the tree looks like, use
	 * 		<br><code>System.out.println(tree.toStringTree());<code></br>
	 * after calling this method.
	 * @param inputText the text to parse
	 */
	private String doParse(String inputText)
	{
		makeParser("program test " + inputText);
		tree = parser.dijkstraText();
		assertTrue(true);
		return tree.toStringTree(parser);
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
