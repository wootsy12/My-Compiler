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

import static org.junit.Assert.*;
import org.junit.*;
import dijkstra.utility.DijkstraException;
import static dijkstra.utility.DijkstraType.*;

/**
 * Description
 * @version Feb 6, 2015
 */
public class SymbolTableTest
{
	private SymbolTable st, st1;
	private Binding a = new Binding("a"),
			b = new Binding("b"),
			c = new Binding("c");
	@Before
	public void setup()
	{
		st = new SymbolTable(null);
		st1 = new SymbolTable(st);
	}
	
	@Test
	public void addFirstItem()
	{
		assertEquals(tUNDEFINED, a.type);
		st.add(a);
		assertEquals(a, st.getBinding("a"));
	}
	
	@Test
	public void getInParent()
	{
		st.add(a);
		assertEquals(a, st1.getBinding("a"));
	}
	
	@Test
	public void getNonexistentElement()
	{
		assertNull(st.getBinding("notHere"));
	}
	
	@Test
	public void shadowSymbol()
	{
		st.add(a);
		final Binding symbol = new Binding("a");
		st1.add(symbol);
		final Binding symbol1 = st1.getBinding("a");
		assertEquals(a, symbol1);
		assertTrue(symbol == symbol1);
		assertFalse(a == symbol1);
	}
	
	@Test
	public void addIfNew()
	{
		st.add(a);
		final Binding symbol = st1.addIfNew(new Binding("a"));
		assertTrue(a == symbol);
	}
	
	@Test(expected=DijkstraException.class)
	public void addDuplicateSymbol()
	{
		st.add(a);
		st.add(new Binding("a"));
	}
}
