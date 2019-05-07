package com.alienvault.model.issue;

import org.apache.commons.lang3.StringUtils;

public class Issue 
{
	private String id;
	private String state;
	private String createdAt;
	private String title;

	private RepositoryObject repository;
	
	public String getId() {
		return id;
	}
	public void setId(final String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(final String state) {
		this.state = state;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(final String createdAt) {
		this.createdAt = createdAt;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public RepositoryObject getRepository() {
		return repository;
	}
	public void setRepository(final RepositoryObject repository) {
		this.repository = repository;
	}
	@Override
	public String toString() {
		return "NodeObject [id=" + id + ", state=" + state + ", createdAt=" + createdAt + ", repository=" + repository
				+ "]";
	}
	
	public String toDay() {
		// Taking advantage of the fact that the time format appears fixed.  This would need to be something to test 
		//  if some of this went to production.
		return StringUtils.split(createdAt, "T")[0];
	}
	
	public String toJSON(final String indent) {
		// Principle here is to be generous with what you accept, also note if the structure was more complex
		//  with more nested types, using recursion would be a better option here.
		final String indentation = StringUtils.isBlank(indent) && indent != null ? indent : "";
		final String doubleIndent = indentation + indentation;
		 //StringBuilder is a bit faster than string concatenation, though it does feel like overkill here.
		final StringBuilder builder = new StringBuilder();
		builder.append(indentation).append("{\n");
		builder.append(doubleIndent).append("\"id\": ").append("\"").append(id).append("\"\n");
		builder.append(doubleIndent).append("\"state\": ").append("\"").append(state).append("\"\n");
		builder.append(doubleIndent).append("\"title\": ").append("\"").append(title).append("\"\n");
		builder.append(doubleIndent).append("\"repository\": ").append("\"").append(repository.getNameWithOwner()).append("\"\n");
		builder.append(doubleIndent).append("\"created_at\": ").append("\"").append(createdAt).append("\"\n");
		builder.append(indentation).append("}\n");
		return builder.toString();
	}
}
