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
import org.antlr.v4.runtime.tree.ParseTree;
import dijkstra.symbol.Binding;

/**
 * Description
 * @version Feb 25, 2017
 */
public class NodePropManager
{
	public final static NodePropManager instance = new NodePropManager();
	
	private Map<ParseTree, NodeProp> nodeProps = new HashMap<ParseTree, NodeProp>();
	
	/**
	 * Default constructor
	 */
	private NodePropManager()
	{
		// Intentionally left empty
	}
	
	public DijkstraType getType(ParseTree node)
	{
		return getProp(node).getType();
	}	
	
	public void setType(ParseTree node, DijkstraType type)
	{
		getProp(node).setType(type);
	}
	
	public Binding getBinding(ParseTree node)
	{
		return getProp(node).getBinding();
	}
	
	public void setBinding(ParseTree node, Binding b)
	{
		getProp(node).setBinding(b);
	}
	
	/**
	 * This private helper method gets the node property object for the tree node.
	 * If there is no such property object, a default one is created and put into
	 * the collection.
	 * @param node the parse tree node
	 * @return the NodeProp instance
	 */
	private NodeProp getProp(ParseTree node)
	{
		NodeProp np = nodeProps.get(node);
		if (np == null) {
			np = new NodeProp();
			nodeProps.put(node, np);
		}
		return np;
	}
	
	/**
	 * Used for testing
	 */
	public int getNodePropCount()
	{
		return nodeProps.size();
	}
	
	public void reset()
	{
		nodeProps.clear();
	}
}
