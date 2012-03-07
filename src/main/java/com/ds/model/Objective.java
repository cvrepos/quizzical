package com.ds.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.KeyFactory;

@Model(schemaVersion = 1)
public class Objective extends Question implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Attribute(lob = true) 
    private List<Option> answers = new ArrayList<Option>(4);

    public List<Option> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Option> answers) {
        this.answers = answers;
    }

    private static String getPreviewTemplate(){
        String html = "<div class='preview_question'>" +
                      "   <ul>" +
        		      "<li id='pquestion'></li>" +
                      "<li id='panswer1'></li>" +
                      "<li id='panswer2'></li>" +
                      "<li id='panswer3'></li>" +
                      "<li id='panswer4'></li>" +
                      "  </ul>" + 
                      "</div>" ;
        return html;
    }
    private static String getHtml(){

        String html = 
            "<form class='questionform' >" +
            "<fieldset>" +
            "  <span class='formentry'>" +
            "   <label for='question' style='display:block'>Question<font color='red'>*</font>:</label>"+
            "   <textarea name='question' id='question' class='required' title='Which of the following planets has its moons named after characters from the works of William Shakespeare?'></textarea>" +
            "  </span>" +
            
            "  <span class='formentry'>" +
            "   <label for='answer1' style='display:block' >Option 1<font color='red'>*</font>:</label> "+            
            "   <textarea name='answer1' id='answer1' style='display:block' class='required' title='Mars' x-webkit-speech></textarea>"+
            "    Is the above option correct?<input type='checkbox' id='isCorrect1' name='isCorrect1' ></input>" +
            "  </span>" +
            
            "  <span class='formentry'>" +
            "   <label for='answer2' style='display:block' >Option 2:</label> "+            
            "   <textarea name='answer2' id='answer2' style='display:block' title='Uranus' ></textarea>"+
            "    Is the above option correct?<input type='checkbox' id='isCorrect2'  name='isCorrect2' ></input>" +
            "  </span>" +
            
            "  <span class='formentry'>" +
            "   <label for='answer3' style='display:block' >Option 3:</label> "+            
            "   <textarea name='answer3' id='answer3' style='display:block' title='Venus'></textarea>"+
            "    Is the above option correct?<input type='checkbox' id='isCorrect3'  name='isCorrect3' ></input>" +
            "  </span>" +
            
            "  <span class='formentry'>" +
            "   <label for='answer4' style='display:block' >Option 4:</font></label> "+            
            "   <textarea name='answer4'  id='answer4' style='display:block' title='Jupitar'></textarea>"+
            "    Is the above option correct?<input type='checkbox' id='isCorrect4'  name='isCorrect4' ></input>" +
            "  </span>" +
            
            "  <span class='formentry'>" +
            "   <label for='tags' >Tags:</label> "+            
            "   <textarea name='tags' id='tags' style='display:block' title='Solar System, Moons, Astronomy'></textarea>"+
            "  </span>" +
            
            "   <input type='hidden' name='type' id='type' value='objective'/>" + 
            
            "  <span class='formentry'>" +
            "   <label for='hints' >Hints:</label> "+            
            "   <textarea name='hints' id='hints' style='display:block' title='Miranda,Titania are two of the moons of this planet.'></textarea>" +
            "  </span>" +
            "   <input type='submit' value='submit' class='submit-button'></input>" +
            "   <input type='button' class='preview-button' name='preview' value='preview'></input>"+
            "</fieldset></form>";
           
                    
            
        return html;
    }
    
    public String getEditableHtml() {

        long id = this.getKey().getId();
        String html =
            "<form class='questionform' id='form"+id+"' >" +
            "<fieldset>" +
            "<legend>Question#" + this.getKey().getId() + "</legend>"+
            "  <span class='formentry'>"
                + "   <label for='question' style='display:block'>Question<font color='red'>*</font>:</label>"
                + "   <textarea name='question' id='question"+ id +"' class='required' >"
                + this.getQuestion()
                + "</textarea>"
                + "  </span>";
        for (int i = 0; i < this.getAnswers().size(); i++) {
            html +=
                "  <span class='formentry'>"
                    + "   <label for='answer"
                    + (i+1) 
                    + "' style='display:block' >Option "
                    + (i+1) 
                    + "<font color='red'>*</font>:</label> "
                    + "   <textarea name='answer"
                    + (i+1) 
                    + "' id='answer"+(i+1)+id+"' style='display:block'>"
                    + this.getAnswers().get(i).getAns()
                    + "</textarea>";
            if (this.getAnswers().get(i).isCorrect()) {
                html +=
                    "    Is the above option correct?<input type='checkbox' id='isCorrect"
                        + (i+1) + id
                        + "' name='isCorrect"
                        + (i+1) 
                        + "' checked></input>"
                        + "  </span>";
            } else {
                html +=
                    "    Is the above option correct?<input type='checkbox' id='isCorrect"
                        + (i+1) + id
                        + "' name='isCorrect"
                        + (i+1) 
                        + "'></input>"
                        + "  </span>";
            }
        }

        String tagList = null;
        for (String tag : this.getTags()) {
            if(tagList == null){
                tagList = tag;
            }else{                
                tagList += "," + tag;
            }
        }
        html +=
            "  <span class='formentry'>"
                + "   <label for='tags' >Tags:</label> "
                + "   <textarea name='tags' id='tags"+id+"' style='display:block' >"
                + tagList
                + "</textarea>"
                + "  </span>"
                +

                "   <input type='hidden' name='type' id='type"+id+"' value='objective'/>"
                +

                "  <span class='formentry'>"
                + "   <label for='hints' >Hints:</label> "
                + "   <textarea name='hints' id='hints"+id+"' style='display:block' >"
                + this.getHints()
                + "</textarea>"
                + "  </span>" +
                "    <input type='button' value='update' class='update-button objective' id="+id+"></input>" +
                "   <input type='button' class='preview-button objective' name='preview' value='preview' id="+id+"></input>" +
                "    <input type='button' value='delete' class='delete-button objective' id="+id+"></input>" +
                "</fieldset>" +
                "</form>";

        return html;
    }
    private static String getPreviewFunction(){
        String jsFunction = 
            "function(event, key){ \n" +
            "$('#preview').addClass('ui-state-highlight');" +
            "$('#pquestion').html('<b>Q</b>: ' + $('#question'+key).val());\n" +                                                      
            "$('#panswer1').html('(A) ' + $('#answer1'+key).val());\n" +
            "if($('#isCorrect1'+key).is(':checked')){ \n" +
            "   $('#panswer1').css('color', 'green');\n" +
            "}\n"+
            "$('#panswer2').html('(B) ' + $('#answer2'+key).val());\n" +
            "if($('#isCorrect2'+key).is(':checked')){ \n" +
            "   $('#panswer2').css('color', 'green');\n" +
            "}\n" +
            "$('#panswer3').html('(C) ' + $('#answer3'+key).val());\n" +
            "if($('#isCorrect3'+key).is(':checked')){ \n" +
            "   $('#panswer3').css('color', 'green');\n" +
            "}\n" +
            "$('#panswer4').html('(D) ' + $('#answer4'+key).val());\n"+ 
            "if($('#isCorrect4'+key).is(':checked')){\n" +
            "   $('#panswer4').css('color', 'green');\n" +
            " }\n" + 
            "}\n";
        return jsFunction;

    }
//    private static String getSubmitFunction(){
//        String jsFunction = 
//            "function(operation,key){ \n" +
//            "var dataString;\n" +            
//            "dataString  = 'type=' + $('#type'+key).val() + '&';\n" +
//            "dataString += 'question=' + $('#question'+key).val() + '&';\n" +                                                      
//            "dataString += 'answer1=' + $('#answer1'+key).val() + '&';\n" +
//            "if($('#isCorrect1'+key).is(':checked')){ \n" +
//            "   dataString += 'isCorrect1=on&';\n" +
//            "}\n"+
//            "dataString += 'answer2=' + $('#answer2'+key).val() + '&';\n" +
//            "if($('#isCorrect2'+key).is(':checked')){ \n" +
//            "   dataString += 'isCorrect2=on&';\n" +
//            "}\n"+
//            "dataString += 'answer3=' + $('#answer3'+key).val() + '&';\n" +
//            "if($('#isCorrect3'+key).is(':checked')){ \n" +
//            "   dataString += 'isCorrect3=on&';\n" +
//            "}\n"+
//            "dataString += 'answer4=' + $('#answer4'+key).val() + '&';\n" +
//            "if($('#isCorrect1'+key).is(':checked')){ \n" +
//            "   dataString += 'isCorrect4=on&';\n" +
//            "}\n"+
//            "dataString += 'hints=' + $('#hints'+key).val() + '&';\n" +
//            "dataString += 'tags=' + $('#tags'+key).val() + '&';\n" +
//            "dataString += 'op=' + operation+'&';\n" +
//            "return dataString; }\n";
//        return jsFunction;
//
//    }
    private static String getSubmitFunction(){
        String jsFunction = 
            "function(operation, key){ \n" +
            "var dataString;\n" +            
            "dataString  = 'type=' + $('#type'+key).val() + '&';\n" +
            "var question = new Object();\n"+
            "question.question =  $('#question'+key).val() ;\n" +
            "var answers = new Array();\n"+
            "answer = new Object();\n"+            
            "answer.ans = $('#answer1'+key).val() ;\n" +
            "if($('#isCorrect1'+key).is(':checked')){ \n" +
            "   answer.isCorrect = true;\n" +
            "}else{" +
            "   answer.isCorrect = false;\n" +
            "}\n"+
            "answer.id = 1;\n" +
            "answers.push(answer);\n"+
            "answer = new Object();\n"+            
            "answer.ans = $('#answer2'+key).val() ;\n" +
            "if($('#isCorrect2'+key).is(':checked')){ \n" +
            "   answer.isCorrect = true;\n" +
            "}else{" +
            "   answer.isCorrect = false;\n" +
            "}\n"+
            "answer.id = 2;\n" +
            "answers.push(answer);\n"+
            "answer = new Object();\n"+            
            "answer.ans = $('#answer3'+key).val() ;\n" +
            "if($('#isCorrect3'+key).is(':checked')){ \n" +
            "   answer.isCorrect = true;\n" +
            "}else{" +
            "   answer.isCorrect = false;\n" +
            "}\n"+
            "answer.id = 3;\n" +
            "answers.push(answer);\n" +
            "answer = new Object();\n"+            
            "answer.ans = $('#answer4'+key).val() ;\n" +
            "if($('#isCorrect4'+key).is(':checked')){ \n" +
            "   answer.isCorrect = true;\n" +
            "}else{" +
            "   answer.isCorrect = false;\n" +
            "}\n"+
            "answer.id = 4;\n" +
            "answers.push(answer);\n"+  
            "question.answers = answers;\n"+
            "question.hints = $('#hints'+key).val();\n" +            
            "question.tags =  $('#tags'+key).val().split(',') ;\n" +
            "dataString += 'op=' + operation+'&';\n" +
            "dataString += 'key=' + key+'&';\n" +
            "dataString += 'question=' + JSON.stringify(question, null, 2); \n" +
            "return dataString; }\n";
        return jsFunction;

    }

    
    @Override
    public void setVals(Map<String, Object> input) {
        
        super.setVals(input);
        for(int i=1; i<= 4; i++){
            String answerKey = "answer"+ Integer.toString(i);
            if(input.containsKey(answerKey)){
                String answer = (String)input.get(answerKey);
                String isCorrectKey = "isCorrect"+ Integer.toString(i);
                boolean isCorrect = false;
                if(input.containsKey(isCorrectKey)){
                    String val = (String)input.get(isCorrectKey);                    
                    isCorrect = val.equals("on") ? true :false;
                }
                answers.add(new Option(answer, isCorrect));
            }
        }
    }

    @Override
    public Presentation getPresentation() {
        Presentation type = new Presentation();
        type.setName("objective");
        type.setHtml(getHtml());
        type.setPreviewTemplate(getPreviewTemplate());
        type.setPreviewFunction(getPreviewFunction());
        type.setSubmitProcessorFunction(getSubmitFunction());
        return type;
    }
    @Override
    public String toString(){
        String html = "<div class='preview_question'>" +
        "   <ul>" +
        "<li id='pquestion'>Q:" + this.getQuestion() + "</li>" +
        "<li id='panswer1'> <input type='checkbox' id=ans1>(A)" + answers.get(0).getAns() + "</li>" +
        "<li id='panswer2'> <input type='checkbox' id=ans2>(B)" + answers.get(1).getAns() + "</li>" +
        "<li id='panswer3'> <input type='checkbox' id=ans3>(C)" + answers.get(2).getAns() + "</li>" +
        "<li id='panswer4'> <input type='checkbox' id=ans4>(D)" + answers.get(3).getAns() + "</li>" +        
        "  </ul>" + 
        "</div>" ;
            return html;
    }
    
    
    @Override
    public String getReadOnlyHtml() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
