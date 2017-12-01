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

package dijkstra.typecheck;

import static dijkstra.utility.DijkstraType.*;
import java.util.function.*;
import dijkstra.utility.*;

/**
 * This class simply contains the rules to apply during type checking.
 * We make use of Java functional capabilities.
 * @version Feb 25, 2017
 */
public class TypeCheckRules
{
	public static BiPredicate<DijkstraType, DijkstraType> exactTypeMatchRule =
			(type1, type2) -> { return type1 == type2; };
			
	public static BiPredicate<DijkstraType, DijkstraType> numerableTypeMatchRule =
			(type1, type2) -> { return (type1 == tINT || type1 == tFLOAT) && (type2 == tINT || type2 == tFLOAT); };
			
	public static BiPredicate<DijkstraType, DijkstraType> relationalTypeRule =
			(left, right) -> { return left == tINT && right == tINT; };
			
	public static BiPredicate<DijkstraType, DijkstraType> equalityTypeRule =
			(left, right) -> { return left == right && left != tUNDEFINED; } ;
}
