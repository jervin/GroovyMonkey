package com.alienvault.model;

public class NodeObject 
{
	private String id;
	private String state;
	private String createdAt;
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
}
