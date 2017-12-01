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

package dijkstra.utility;

import java.util.*;
import org.antlr.v4.runtime.tree.*;
import dijkstra.symbol.Binding;

/**
 * This is a simple data structure that holds extra fields that we want to be associated
 * with each node in the Parse tree.
 * @version Feb 20, 2017
 */
public class NodeProp
{
	private DijkstraType nodeType;
	private Binding binding;

	/**
	 * Default constructor creates a node property object with default values.
	 */
	public NodeProp()
	{
		nodeType = DijkstraType.tUNDEFINED;
		binding = null;
	}
	
	/**
	 * Get the node type. If there is a binding, use the binding type.
	 * @return the node type
	 */
	public DijkstraType getType()
	{
		return binding != null ? binding.type : nodeType;
	}
	
	/**
	 * Set the type of the node property object.
	 * @param type
	 */
	public void setType(DijkstraType type)
	{
		if (binding != null) {
			binding.type = type;
		}
		nodeType = type;
	}
	
	/**
	 * @return the binding for this property
	 */
	public Binding getBinding()
	{
		return binding;
	}
	
	/**
	 * Set the binding for the node property object
	 * @param b the binding
	 */
	public void setBinding(Binding b)
	{
		binding = b;
		nodeType = b.type;
	}
}
