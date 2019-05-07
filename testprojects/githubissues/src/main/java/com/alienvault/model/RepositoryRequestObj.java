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
}
