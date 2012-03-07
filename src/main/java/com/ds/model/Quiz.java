package com.ds.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String question;
    private String answer;
    private String wrong_a;
    private String wrong_b;
    private String wrong_c;
    private String hints;
    private List<String> tags;
    private String createdBy;
    private Date   createdDate;
    
    
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getWrong_a() {
        return wrong_a;
    }

    public void setWrong_a(String wrong_a) {
        this.wrong_a = wrong_a;
    }

    public String getWrong_b() {
        return wrong_b;
    }

    public void setWrong_b(String wrong_b) {
        this.wrong_b = wrong_b;
    }

    public String getWrong_c() {
        return wrong_c;
    }

    public void setWrong_c(String wrong_c) {
        this.wrong_c = wrong_c;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
        Quiz other = (Quiz) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }
    private static String getHtml(){
        String html = 
        "<table>" + 
        "<tr>" + 
        "    <td>Question:</td>"+
        "<td><textarea name='question'></textarea></td>" +
        "</tr>" +
        "<tr>"+
        "    <td>Answer:</td> "+
        "    <td><textarea name='answer'></textarea></td>"+
        "</tr>" +
        "<tr>" +
        "    <td>WrongA:</td>" +
        "    <td><textarea name='wrong_a'></textarea></td>"+
        "</tr>"+
        "<tr>" +
        "    <td>WrongB:</td>" +
        "    <td><textarea name='wrong_b'></textarea></td>" +
        "</tr>" +
        "<tr>" +
        "    <td>WrongC:</td>" +
        "    <td><textarea name='wrong_c'></textarea></td>" +
        "</tr>" +
        "<tr>" +
        "    <td>Tags:</td>" +
        "    <td><textarea name='tags'></textarea></td>" +
        "</tr>" +
        "<tr>" +
        "    <td>Hints:</td>" +
        "    <td><textarea name='hints'></textarea></td>" +
        "</tr>" +
                
        "</table>";
        return html;
    }
    public static Presentation getQuizType(){
        Presentation type = new Presentation();
        type.setName("test");
        type.setHtml(getHtml());
        type.setPreviewFunction("function(){}");
        type.setSubmitProcessorFunction("function(){}");
        return type;
    }
}
