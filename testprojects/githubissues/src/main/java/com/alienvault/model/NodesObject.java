package com.alienvault.model;

import io.aexp.nodes.graphql.annotations.GraphQLProperty;
import java.util.List;

@GraphQLProperty(name = "nodes")
public class NodesObject 
{
	private int totalCount;
	
	private PageInfo pageInfo;
	
	private List<Issue> nodes;

	public List<Issue> getNodes() 
	{
		return nodes;
	}
	public void setNodes(final List<Issue> nodes) 
	{
		this.nodes = nodes;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(final int totalCount) {
		this.totalCount = totalCount;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(final PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	@Override public String toString() { return "totalCount: " + totalCount + " pageInfo: " + pageInfo + " nodes: " + nodes; }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		result = prime * result + ((pageInfo == null) ? 0 : pageInfo.hashCode());
		result = prime * result + totalCount;
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
		final NodesObject other = (NodesObject) obj;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		if (pageInfo == null) {
			if (other.pageInfo != null)
				return false;
		} else if (!pageInfo.equals(other.pageInfo))
			return false;
		if (totalCount != other.totalCount)
			return false;
		return true;
	}
	
	
}
