package com.ds.model;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.ds.model.Question;

@Model(schemaVersion = 1)
public class Subjective extends Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Presentation getPresentation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEditableHtml() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getReadOnlyHtml() {
        // TODO Auto-generated method stub
        return null;
    }

}
