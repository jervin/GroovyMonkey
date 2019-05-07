package com.alienvault.model;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;

@GraphQLProperty(name="repository", arguments= {@GraphQLArgument(name="owner"), @GraphQLArgument(name="name")})
public class RepositoryRequestObj 
{
	private String createdAt;

	@GraphQLProperty(name = "issues", arguments = {@GraphQLArgument(name="orderBy"), @GraphQLArgument(name="last")})
	private NodesObject issues;
	
	public String getCreatedAt() { return createdAt; }
	public void setCreatedAt(final String createdAt) { this.createdAt = createdAt; }
	public NodesObject getIssues() { return issues; }
	
	@Override
	public String toString() {
		return "RepositoryRequestObj [createdAt=" + createdAt + ", issues=" + issues + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((issues == null) ? 0 : issues.hashCode());
		return result;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RepositoryRequestObj other = (RepositoryRequestObj) obj;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (issues == null) {
			if (other.issues != null)
				return false;
		} else if (!issues.equals(other.issues))
			return false;
		return true;
	}
	
	
}
