package com.alienvault.model;

public class RepositoryID {
	
	private final String owner;
	private final String name;
	
	public RepositoryID(final String owner, final String name) {
		this.owner = owner;
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	@Override 
	public String toString() {
		return owner + "/" + name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		final RepositoryID other = (RepositoryID) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

}
