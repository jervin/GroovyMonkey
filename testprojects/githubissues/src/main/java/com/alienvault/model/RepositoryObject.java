package com.alienvault.model;

import io.aexp.nodes.graphql.annotations.GraphQLProperty;

@GraphQLProperty(name="repository")
public class RepositoryObject {

	private String nameWithOwner;

	public String getNameWithOwner() {
		return nameWithOwner;
	}

	public void setNameWithOwner(final String nameWithOwner) {
		this.nameWithOwner = nameWithOwner;
	}

	@Override
	public String toString() {
		return "RepositoryObject [nameWithOwner=" + nameWithOwner + "]";
	}
	
}
