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

package net.sf.groovyMonkey.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublishScriptForWiki extends PublishScript {
	protected String decorateText(String contents) {
		String result = "\n" + super.decorateText(contents);
		Pattern pattern = Pattern.compile("\\n");
		Matcher matcher = pattern.matcher(result);
		result = matcher.replaceAll("\n  ");
		result = result + "\n";
		return result;
	}
}
