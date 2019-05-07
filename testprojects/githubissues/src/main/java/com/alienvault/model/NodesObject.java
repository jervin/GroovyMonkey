package com.alienvault.model;

import io.aexp.nodes.graphql.annotations.GraphQLProperty;
import java.util.List;

@GraphQLProperty(name = "nodes")
public class NodesObject 
{
	private int totalCount;
	
	private PageInfo pageInfo;
	
	private List<NodeObject> nodes;

	public List<NodeObject> getNodes() 
	{
		return nodes;
	}
	public void setNodes(final List<NodeObject> nodes) 
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
}
