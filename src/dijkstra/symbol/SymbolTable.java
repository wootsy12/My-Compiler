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
import dijkstra.utility.DijkstraException;

/**
 * A symbol table for one lexical scope.
 * @version Feb 16, 2017
 */
public class SymbolTable
{
	private final SymbolTable parent;
	private final Map<String, Binding> symbols;
	
	/**
	 * Sole constructor. Creates the symbol table with the specified parent. The parent
	 * can be null for the global lexical level symbol table.
	 * @param parent the symbol table at the enclosing lexical level.
	 */
	public SymbolTable(SymbolTable parent)
	{
		this.parent = parent;
		symbols = new HashMap<String, Binding>();
	}
	
	/**
	 * Add the specified Binding to the current symbol table.
	 * @param binding the binding to add to the table
	 * @return the binding that was added
	 * @throws DijkstraSymbolException if the binding already exists in this table
	 */
	public Binding add(Binding binding) 
	{


		final Binding s = symbols.put(binding.id, binding);
		
		if (s != null) {	// Binding was already in the table
			throw new DijkstraException(
					"Attempting to add a duplicate binding to a binding table" + s.id);
		}
		return binding;
	}
	
	/**
	 * Add the binding to the symbol table if there is no binding for the same name
	 * visible from the current scope.
	 * @param binding the binding to add if not new
	 * @return the binding that is visible from this scope, either the argument given or the one
	 * 		that can be seen from this scope.
	 */
	public Binding addIfNew(Binding binding)
	{

		Binding s = getBinding(binding.id);

		if (s == null) {
			symbols.put(binding.id, binding);
			s = binding;
		}
		return s;
	}
	
	/**
	 * Get the binding with the specified key in the current scope.
	 * @param id the desired binding's ID
	 * @return the binding referenced or null if it does not exist.
	 */
	public Binding getBinding(String id)
	{

		
		Binding binding = symbols.get(id);

		SymbolTable st = this;
		while (binding == null && st.parent != null) {
			st = st.parent;
			binding = st.getBinding(id);
		}
		return binding;
	}
	
	public int getNumberOfSymbols()
	{
		return symbols.size();
	}
	
	/**
	 * @return the parent of this symbol table
	 */
	public SymbolTable getParent()
	{
		return parent;
	}

	/**
	 * @return the bindings
	 */
	public Collection<Binding> getBindings()
	{
		return symbols.values();
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((symbols == null) ? 0 : symbols.hashCode());
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SymbolTable)) {
			return false;
		}
		SymbolTable other = (SymbolTable) obj;
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		if (symbols == null) {
			if (other.symbols != null) {
				return false;
			}
		} else if (!symbols.equals(other.symbols)) {
			return false;
		}
		return true;
	}
}
