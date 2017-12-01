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
 * Test cases for the SymbolTableManager singleton.
 * @version Feb 8, 2015
 */
public class SymbolTableManagerTest
{
	private SymbolTableManager stm = SymbolTableManager.instance;
	
	@Before
	public void setup()
	{
		stm.reset();
	}
	
	@Test
	public void addSymbol()
	{
		final Binding symbol = stm.add("a");
		assertEquals("a", symbol.id);
		assertEquals(tUNDEFINED, symbol.type);
	}
	
	@Test
	public void addSymbolAndType()
	{
		final Binding symbol = stm.add("a", tBOOLEAN);
		assertEquals("a", symbol.id);
		assertEquals(tBOOLEAN, symbol.type);
	}

	@Test(expected=DijkstraException.class)
	public void addSymbolTwice()
	{
		stm.add("a");
		stm.add("a", tINT);
	}
	
	@Test
	public void addSameIdInParentAndChild()
	{
		final Binding a1 = stm.add("a");
		stm.enterScope();
		final Binding a2 = stm.add("a");
		assertFalse(a1 == a2);
		assertTrue(stm.getBinding("a") == a2);
		stm.exitScope();
		assertFalse(stm.getBinding("a") == a2);
		assertTrue(stm.getBinding("a") == a1);
	}
}
