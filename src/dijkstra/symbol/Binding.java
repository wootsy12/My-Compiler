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

import org.antlr.v4.runtime.Token;
import dijkstra.utility.DijkstraType;

/**
 * The Binding is a data structure that contains all the information about a specific
 * symbol (ID) in the program being compiled. Bindings are stored in symbol tables and are
 * indexed by the symbol at a specific lexical level.
 * @version Feb 16, 2017
 */
public class Binding
{
	public final String id;
	public DijkstraType type;
	public String value;
	public int address;
	public static final int NO_ADDRESS = Integer.MIN_VALUE	;
	
	/**
	 * Constructor that creates the Binding with the name, and an UNDEFINED type.
	 * @param id the symbol name
	 */
	public Binding(String id)
	{
		this(id, DijkstraType. tUNDEFINED);
	}
	
	/**
	 * Constructor that creates the Binding with the specified name and type.
	 * @param id the symbol name
	 * @param type the type assigned to the symbol
	 */
	public Binding(String id, DijkstraType type)
	{
		this.id = id;
		this.type = type;
		this.value = null;
		this.address = NO_ADDRESS;
	}
	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (!(obj instanceof Binding)) {
			return false;
		}
		Binding other = (Binding) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("name=");
		builder.append(id);
		builder.append(", DijkstraSymbol [type=");
		builder.append(type);
		builder.append(", value=");
		builder.append(value);
		builder.append(", address=");
		builder.append(address);
		builder.append("]");
		return builder.toString();
	}
}
