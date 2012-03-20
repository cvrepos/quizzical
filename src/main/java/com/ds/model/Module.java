package com.ds.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum State{
    	DRAFT,
    	PUBLISHED,
    	INREVIEW
    }
    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    private String owner;
    private String name;
    //navigation
    private Long parent = null;
    private Long prevSibling = null;
    private Long firstChild = null;    
    private Long nextSibling = null;
    
    private long questionCount = 0;
    private long cloneCount = 0;
    private int  ratings = 1;
    private String description;    
    private State state = State.DRAFT;
    
    
    public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getPrevSibling() {
		return prevSibling;
	}

	public void setPrevSibling(Long prevSibling) {
		this.prevSibling = prevSibling;
	}

	public Long getFirstChild() {
		return firstChild;
	}

	public void setFirstChild(Long firstChild) {
		this.firstChild = firstChild;
	}

	public Long getNextSibling() {
		return nextSibling;
	}

	public void setNextSibling(Long nextSibling) {
		this.nextSibling = nextSibling;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public long getCloneCount() {
		return cloneCount;
	}

	public void setCloneCount(long cloneCount) {
		this.cloneCount = cloneCount;
	}

	public int getRatings() {
		return ratings;
	}

	public void setRatings(int ratings) {
		this.ratings = ratings;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Module other = (Module) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

	
}
