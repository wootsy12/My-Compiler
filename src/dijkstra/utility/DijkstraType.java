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

/**
 * The symbol types for symbols in this version of the Dijkstra language.
 * @version Feb 16, 2017
 */
public enum DijkstraType
{
	tUNDEFINED, tINT, tBOOLEAN, tFLOAT; 
	
	/**
	 * @param a string that represents a type.
	 * @return the DijkstraType corresponding to the string or 
	 * 	tUNDEFINED if there is no matching type.
	 */
	public static DijkstraType getType(String s)
	{
		switch (s) {
			case "int": return tINT;
			case "boolean" : return tBOOLEAN;
			case "float" : return tFLOAT;
		}
		return tUNDEFINED;
	}
}
