package com.alienvault.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class Day {
	private final Map<String, Integer> occurrances = Maps.newLinkedHashMap();
	private final String day;
	private int totalCount = 0;

	public Day(String day) {
		super();
		this.day = day;
	}
	
	public Map<String, Integer> getOccurances() {
		// I would consider sending either a defensive copy or an unmodifiable map wrapper, but 
		//  for this it seems overkill right now.
		return occurrances;
	}

	public Day addOccurrance(final String repoID, final int count) {
		if (!occurrances.containsKey(repoID)) {
			occurrances.put(repoID, 0);
		}
		occurrances.put(repoID, occurrances.get(repoID) + count);
		this.totalCount += count;
		return this;
	}
	
	public String getDay() {
		return day;
	}

	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public String toString() {
		return "Day [occurrances=" + occurrances + ", day=" + day + ", totalCount=" + totalCount + "]";
	}
	public String toJSON(final String indent) {
		// Principle here is to be generous with what you accept, also note if the structure was more complex
		//  with more nested types, using recursion would be a better option here.
		final String indentation = StringUtils.isBlank(indent) && indent != null ? indent : "";
		final String doubleIndent = indentation + indentation;
		final String tripleIndent = doubleIndent + indentation;
		 //StringBuilder is a bit faster than string concatenation, though it does feel like overkill here.
		final StringBuilder builder = new StringBuilder();
		builder.append(doubleIndent).append("\"day\": ").append("\"").append(day).append("\"\n");
		builder.append(doubleIndent).append("\"occurrances\": {\n");
		final Set<String> keySet = occurrances.keySet();
		final Iterator<String> keyIterator = keySet.iterator();
		// Using an iterator here because I want to know when the comma can be omitted.
		while(keyIterator.hasNext()) {
			final String repoID = keyIterator.next();
			builder.append(tripleIndent).append("\"").append(repoID).append("\": ").append(occurrances.get(repoID));
			if (keyIterator.hasNext()) {
				builder.append(",");
			}
			builder.append("\n");
		}
		builder.append(doubleIndent).append("}\n");
		return builder.toString();
	}
}
