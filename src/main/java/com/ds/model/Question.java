package com.ds.model;

import java.io.Serializable;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.datastore.Key;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public abstract class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String question;
    
    private String createdBy;
    
    private Date createdDate;
    
    private List<String> tags = new LinkedList<String>();
    
    private String hints;
    
    private String metaData;
    
    private String source;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
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
        Question other = (Question) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }
    public abstract Presentation getPresentation();
    
    public abstract String getEditableHtml();
    public abstract String getReadOnlyHtml();
    
    public void setVals(Map<String, Object> input){
        if(input.containsKey("question")){
            this.question = (String)input.get("question");            
        }
        if(input.containsKey("createdBy")){
            this.createdBy = (String)input.get("createdBy");            
        }
        if(input.containsKey("hints")){
            this.hints = (String)input.get("hints");            
        }
        if(input.containsKey("metaData")){
            this.metaData = (String)input.get("metaData");            
        }
        this.createdDate = new Date();                
        if(input.containsKey("tags")){
            String tagString = (String)input.get("tags");
            Set<String> s = new HashSet<String>();
            for(String tag: tagString.split(",")){                
                s.add(tag.trim().toLowerCase());                
            }
            if(!s.isEmpty()){
                tags.addAll(s);
            }
        }
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
