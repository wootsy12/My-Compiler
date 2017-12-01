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

import java.util.*;
import dijkstra.utility.DijkstraType;
import static dijkstra.utility.DijkstraType.*;

/**
 * The manager for the symbol tables. This handles the scopes and also contains
 * methods for printing the tables, testing, and debugging.
 * @version Feb 16, 2017
 */
public class SymbolTableManager
{
	public static SymbolTableManager instance =  new SymbolTableManager();
	public SymbolTable currentSymbolTable;
	public final List<SymbolTable> tables;
	private final boolean DEBUG = true;
	
	/**
	 * Constructor that sets up the initial (global) symbol table.
	 */
	private SymbolTableManager()
	{
		tables = new ArrayList<SymbolTable>();
		currentSymbolTable = new SymbolTable(null);	// Global symbol table
		tables.add(currentSymbolTable);
	}
	
	/**
	 * Enter a new scope. This adds a new symbol table to the lexical scope.
	 * @return the new current symbol table
	 */
	public SymbolTable enterScope()
	{
		currentSymbolTable = new SymbolTable(currentSymbolTable);
		tables.add(currentSymbolTable);
		return currentSymbolTable;
	}
	
	/**
	 * Exit a scope.
	 * @return the new current symbol table
	 */
	public SymbolTable exitScope()
	{
		currentSymbolTable = currentSymbolTable.getParent();
		return currentSymbolTable;
	}
	
	// The next methods are pass through methods to the current symbol table, but the
	// symbol table manager takes care of creating the appropriate symbols.
	
	/**
	 * Add a symbol to the current symbol table.
	 * @param binding the symbol to add
	 * @return the added symbol
	 * @throws DijkstraSymbolException if the symbol already exists in this table
	 * @see SymbolTable#add(Binding)
	 */
	public Binding add(Binding binding)
	{
		return currentSymbolTable.add(binding);
	}
	
	/**
	 * Add a symbol to the current symbol table with a type of UNDEFINED.
	 * @param id the symbol name 
	 * @return the added symbol
	 * @throws DijkstraException if the symbol already exists in this table
	 * @see SymbolTable#add(Symbol)
	 * @see Symbol#Symbol(String)
	 */
	public Binding add(String id)
	{
		return currentSymbolTable.add(new Binding(id));
	}
	
	/**
	 * Add a symbol to the current symbol table with the type specified.
	 * @param id the symbol name 
	 * @param type the symbol's type
	 * @return the added symbol
	 * @throws DijkstraSymbolException if the symbol already exists in this table
	 * @see SymbolTable#add(Binding)
	 * @see Binding#Binding(String, dijkstra.utility.DijkstraType)
	 */
	public Binding add(String id, DijkstraType type)
	{
		return currentSymbolTable.add(new Binding(id, type));
	}
	
	/**
	 * Add a symbol to the current symbol table with a type of UNDEFINED.
	 * @param binding symbol to add 
	 * @return the symbol (whether added or one that was visible)
	 * @see SymbolTable#addIfNew(Binding)
	 * @see Binding#Binding(String)
	 */
	public Binding addIfNew(Binding binding)
	{
		return currentSymbolTable.addIfNew(binding);
	}
	
	/**
	 * Add a symbol to the current symbol table with a type of UNDEFINED.
	 * @param id the symbol name 
	 * @return the symbol (whether added or one that was visible)
	 * @see SymbolTable#addIfNew(Binding)
	 * @see Binding#Binding(String)
	 */
	public Binding addIfNew(String id)
	{
		return currentSymbolTable.addIfNew(new Binding(id));
	}
	
	/**
	 * Add a binding to the current symbol table with a type of UNDEFINED.
	 * @param id the symbol name 
	 * @param type the type associated with the id
	 * @return the binding (whether added or one that was visible)
	 * @see SymbolTable#addIfNew(Binding)
	 * @see Binding#Binding(String)
	 */
	public Binding addIfNew(String id, DijkstraType type)
	{
		return currentSymbolTable.addIfNew(new Binding(id, type));
	}
	
	/**
	 * Get the binding with the specified key in the current scope.
	 * @param id the desired symbol's ID
	 * @return the symbol referenced or null if it does not exist.
	 * @see SymbolTable#getBinding(String)
	 */
	public Binding getBinding(String id)
	{
		return currentSymbolTable.getBinding(id);
	}

	/**
	 * @return the tables
	 */
	public List<SymbolTable> getTables()
	{
		return tables;
	}
	
	// Next methods added for testing and debugging
	/**
	 * @param i
	 * @return the symbol table at index i in the symbol table array
	 */
	public SymbolTable getSymbolTable(int i)
	{
		return tables.get(i);
	}
	
	/**
	 * Check to make sure that all IDs are typed. This may be used at the
	 * end of the type checking pass. Also used for testing.
	 * @return true if all IDs are typed
	 */
	public boolean allSymbolsAreTyped()
	{
		boolean result = true;
		for (SymbolTable st : tables) {
			result = result && checkBindings(st);
		}
		return result;
	}
	
	private boolean checkBindings(SymbolTable st)
	{
		for (Binding b : st.getBindings()) {
			if (b.type == tUNDEFINED) return false;
		}
		return true;
	}
	
	public void reset()
	{
		tables.clear();
		currentSymbolTable = new SymbolTable(null);
		tables.add(currentSymbolTable);
	}
}
