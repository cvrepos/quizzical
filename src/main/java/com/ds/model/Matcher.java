package com.ds.model;

import java.io.Serializable;
import java.util.Map;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.ds.model.Question;

@Model(schemaVersion = 1)
public class Matcher extends Question implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Attribute(lob = true)
    private Map<String, String> options;

    

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public Map<String, String> getOptions() {
        return options;
    }
    @Override
    public void setVals(Map<String, Object> input) {
        
        super.setVals(input);
        for(int i=1; i<= 4; i++){
            String sideA = "sidea"+ Integer.toString(i);
            if(input.containsKey(sideA)){
                String partA = (String)input.get(sideA);
                String sideB = "sideb"+ Integer.toString(i); 
                String partB = input.containsKey(sideB) ? (String)input.get(sideB) : "";                
                options.put(partA, partB);
            }
        }
    }

    @Override
    public Presentation getPresentation() {
        Presentation type = new Presentation();
        type.setName("matcher");
        type.setHtml(getHtml());
        type.setPreviewTemplate(getPreviewTemplate());
        type.setPreviewFunction(getPreviewFunction());
        type.setSubmitProcessorFunction(getSubmitFunction());
        return type;
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
            "  <span class='formentry'>" +
            "   <label for='question' style='display:block'>Question<font color='red'>*</font>:</label>"+
            "   <textarea name='question' id='question' class='required' title='Which of the following planets has its moons named after characters from the works of William Shakespeare?'></textarea>" +
            "  </span>" +
            
            "  <span class='formentry'>" +
            "   <label for='answer1' style='display:block' >Option 1<font color='red'>*</font>:</label> "+            
            "   <input name='answer1' id='answer1' style='display:block' class='required' title='Mars' x-webkit-speech></input>"+
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
            "  </span>" ;
            
           
                    
            
        return html;
    }
    private static String getPreviewFunction(){
        String jsFunction = 
            "function(event){ \n" +
            "$('#preview').addClass('ui-state-highlight');" +
            "$('#pquestion').html('<b>Q</b>: ' + $('#question').val());\n" +                                                      
            "$('#panswer1').html('(A) ' + $('#answer1').val());\n" +
            "if($('#isCorrect1').is(':checked')){ \n" +
            "   $('#panswer1').css('color', 'green');\n" +
            "}\n"+
            "$('#panswer2').html('(B) ' + $('#answer2').val());\n" +
            "if($('#isCorrect2').is(':checked')){ \n" +
            "   $('#panswer2').css('color', 'green');\n" +
            "}\n" +
            "$('#panswer3').html('(C) ' + $('#answer3').val());\n" +
            "if($('#isCorrect3').is(':checked')){ \n" +
            "   $('#panswer3').css('color', 'green');\n" +
            "}\n" +
            "$('#panswer4').html('(D) ' + $('#answer4').val());\n"+ 
            "if($('#isCorrect4').is(':checked')){\n" +
            "   $('#panswer4').css('color', 'green');\n" +
            " }\n" + 
            "}\n";
        return jsFunction;

    }
    private static String getSubmitFunction(){
        String jsFunction = 
            "function(){ \n" +
            "var dataString;\n" +            
            "dataString  = 'type=' + $('#type').val() + '&';\n" +
            "dataString += 'question=' + $('#question').val() + '&';\n" +                                                      
            "dataString += 'answer1=' + $('#answer1').val() + '&';\n" +
            "if($('#isCorrect1').is(':checked')){ \n" +
            "   dataString += 'isCorrect1=on&';\n" +
            "}\n"+
            "dataString += 'answer2=' + $('#answer2').val() + '&';\n" +
            "if($('#isCorrect2').is(':checked')){ \n" +
            "   dataString += 'isCorrect2=on&';\n" +
            "}\n"+
            "dataString += 'answer3=' + $('#answer3').val() + '&';\n" +
            "if($('#isCorrect3').is(':checked')){ \n" +
            "   dataString += 'isCorrect3=on&';\n" +
            "}\n"+
            "dataString += 'answer4=' + $('#answer4').val() + '&';\n" +
            "if($('#isCorrect1').is(':checked')){ \n" +
            "   dataString += 'isCorrect4=on&';\n" +
            "}\n"+
            "dataString += 'hints=' + $('#hints').val() + '&';\n" +
            "dataString += 'tags=' + $('#tags').val() + '&';\n" +
            "return dataString; }\n";
        return jsFunction;

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
