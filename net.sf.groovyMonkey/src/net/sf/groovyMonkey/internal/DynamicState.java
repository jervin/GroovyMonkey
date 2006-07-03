/*******************************************************************************
 * Copyright (c) 2005 Eclipse Foundation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bjorn Freeman-Benson - initial implementation
 *     Ward Cunningham - initial implementation
 *******************************************************************************/

package net.sf.groovyMonkey.internal;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import net.sf.groovyMonkey.dom.IDynamicState;

public class DynamicState implements IDynamicState {
	private static final String CONTEXT_ID_KEY = "The jaws that bite, the claws that catch!";

	Stack context_stack = null;

	@SuppressWarnings("unchecked")
    public DynamicState() {
		context_stack = new Stack();
		context_stack.push(new Hashtable());
	}

	@SuppressWarnings("unchecked")
    public void set(String name, Object value) {
		top().put(name, value);
	}

	private Map top() {
		return ((Map) (context_stack.lastElement()));
	}

	public Object get(String name) {
		return top().get(name);
	}

	@SuppressWarnings("unchecked")
    public void begin(Object id) {
		Map oldtop = top();
		Map top = new Hashtable();
		for (Iterator iter = oldtop.keySet().iterator(); iter.hasNext();) {
			Object key = (Object) iter.next();
			top.put(key, oldtop.get(key));
		}
		top.put(CONTEXT_ID_KEY, id);
		context_stack.add(top);
	}

	public void end(Object id) {
		Object to_remove = null;
		for (Iterator iter = context_stack.iterator(); iter.hasNext();) {
			Map element = (Map) iter.next();
			if (element.get(CONTEXT_ID_KEY) == id)
				to_remove = element;
		}
		if (to_remove != null) {
			Object x;
			do {
				x = context_stack.pop();
			} while (x != to_remove);
		}
	}

}
