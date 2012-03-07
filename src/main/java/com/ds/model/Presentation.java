package com.ds.model;

public class Presentation {
    private String name;
    private String html;
    private String previewTemplate;
    
    private String previewFunction;
    private String submitProcessorFunction;
    
    public String getSubmitProcessorFunction() {
        return submitProcessorFunction;
    }
    public void setSubmitProcessorFunction(String submitProcessorFunction) {
        this.submitProcessorFunction = submitProcessorFunction;
    }
    public String getPreviewFunction() {
        return previewFunction;
    }
    public void setPreviewFunction(String previewFunction) {
        this.previewFunction = previewFunction;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getHtml() {
        return html;
    }
    public void setHtml(String html) {
        this.html = html;
    }
    public void setPreviewTemplate(String previewTemplate) {
       this.previewTemplate = previewTemplate;
        
    }
    public String  getPreviewTemplate(){
        return this.previewTemplate;
    }
    

}
