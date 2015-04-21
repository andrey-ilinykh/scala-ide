/*
 * Copyright (c) 2014 - 2015 Contributor. All rights reserved.
 */
package org.scalaide.debug.internal.expression
package proxies.phases

import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox

/**
 * Searches for all unbound variables in scope.
 *
 * Does not transform the tree in any way.
 *
 * This phase runs before `typecheck`.
 *
 * @param typesContext context to which unbound variables are registered
 * @param localVariablesNames list of names for `val`s and `var`s defined in current method
 */
class SearchForUnboundVariables(val toolbox: ToolBox[universe.type], typesContext: TypesContext, localVariablesNames: => Set[String])
    extends TransformationPhase
    with UnboundValuesSupport {

  import toolbox.u._

  override def transform(tree: universe.Tree): universe.Tree = {
    val unboundNames = new VariableProxyTraverser(tree, _ => None, localVariablesNames).findUnboundVariables()
    typesContext.addUnboundVariables(unboundNames)
    tree
  }

}
