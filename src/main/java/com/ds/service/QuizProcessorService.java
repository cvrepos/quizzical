
package com.ds.service;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.mortbay.log.Log;
import org.slim3.controller.Controller;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Filter;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.InMemoryFilterCriterion;
import org.slim3.datastore.S3QueryResultList;


import com.ds.exception.UserManagementException;
import com.ds.meta.ModuleMeta;
import com.ds.meta.ObjectiveMeta;
import com.ds.meta.QuestionMeta;
import com.ds.meta.SessionMeta;
import com.ds.meta.TagMeta;
import com.ds.meta.UserMeta;
import com.ds.model.Module;
import com.ds.model.Objective;
import com.ds.model.Option;
import com.ds.model.Quiz;
import com.ds.model.Presentation;
import com.ds.model.Question;
import com.ds.model.Session;
import com.ds.model.Subjective;
import com.ds.model.Tag;
import com.ds.model.User;
import com.ds.model.json.BasicSearch;
import com.ds.model.json.KeyValueMap;
import com.ds.model.json.MatchSearch;
import com.ds.util.Utils;


import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.gson.Gson;


public class QuizProcessorService {


	private static final Logger log = Logger.getLogger(QuizProcessorService.class.getName());
	private static final String ADMIN = "admin";
    private static QuizProcessorService object = null;
    public static QuizProcessorService getInstance(){
        if(object == null){
            object = new QuizProcessorService();
            //init the class
            object.init();
        }
        return object;
    }
    private QuizProcessorService(){}
    
    private Map<String, Question > quizTypeMap = new HashMap<String, Question>();
    
    public void init()
    {
    	
        quizTypeMap.put("objective", new Objective());
        //quizTypeMap.put("subjective", new Subjective());
    }
    
    public void process(Map<String, Object> input) {
        //check type of the quiz
        String type = null;
        if(input.containsKey("type")){
            type = (String)input.get("type");
        }else{
            System.err.println("Invalid quiz");
            return;
        }
        Object quiz = null;
        if(type.equals("objective")){
            quiz = new Objective();
            ((Objective)quiz).setVals(input);            
        }
        //process tag to categorize 
        if(quiz instanceof Question){
            Question q = (Question)quiz;
            TagMeta m = TagMeta.get();
            for(String tag: q.getTags()){
                Tag storedTag = Datastore.query(m).filter(m.name.equal(tag)).asSingle();
                Transaction tx = Datastore.beginTransaction();
                if(storedTag != null){                    
                    storedTag.setCount(storedTag.getCount() +1 );
                    System.out.println("Updating tag:"+ tag);
                }else{
                    storedTag = new Tag();
                    storedTag.setAdmin(false);
                    storedTag.setCount(1);
                    storedTag.setName(tag);
                    storedTag.setWeight(1);
                    System.out.println("Added tag:"+ tag);
                }
                Datastore.put(storedTag);
                tx.commit();
                
            }
            
        }
        if(quiz != null){
            Transaction tx = Datastore.beginTransaction();
            Datastore.put(quiz);
            tx.commit();     
        }else{
            System.err.println("Quiz is null");
        }
    }
    public List<Presentation> getQuizTypes(){
        List<Presentation> types = new LinkedList<Presentation>();
        Question q = null;
        Iterator<Question> it = this.quizTypeMap.values().iterator();
        while(it.hasNext())
        {
            q = it.next();        
            types.add(q.getPresentation());         
        }
        return types;
    }
    private QuestionMeta t = new QuestionMeta();
    public List<Question> getAllQuiz() {
        return Datastore.query(t).sort(t.createdDate.desc).asList();
    }
    public List<Question> getQuizByKey(String id) {
        Key k = KeyFactory.stringToKey(id);    
        return Datastore.query(t).filter(t.key.equal(k)).asList();
    }
    public Question getQuizByKey(Long id) {
        Key k = KeyFactory.createKey(QuestionMeta.get().getKind(), id);
        return Datastore.query(t).filter(t.key.equal(k)).asSingle();
    }
    public Iterator<Question> getQuizByTag(String tag) {        
        return Datastore.query(t).filterInMemory(t.tags.contains(tag)).asList().iterator();
    }
    public Iterator<Question> getQuizByTags(String[] tags) {        
        
        Set<Question> qs = new HashSet<Question>();
        for(String tag:tags){
            qs.addAll(Datastore.query(t).filterInMemory(t.tags.contains(tag)).asList());
        }
        return qs.iterator();
    }

    public Iterator<Question> getQuizByTagCursored(String tag,
            Map<String, String> cursorMap, int step) {

        if (cursorMap.isEmpty()) {
            S3QueryResultList<Question> results =
                Datastore.query(Question.class).limit(step).asQueryResultList();
            cursorMap.put("cursor", results.getEncodedCursor());
            cursorMap.put("filters", results.getEncodedFilters());
            cursorMap.put("sorts", results.getEncodedSorts());            
            return results.iterator();
        } else {
            // Store the encodedCursor...

            S3QueryResultList<Question> results =
                Datastore
                    .query(Question.class)
                    .encodedCursor(cursorMap.get("cursor"))
                    .encodedFilters(cursorMap.get("filters"))
                    .encodedSorts(cursorMap.get("sorts"))
                    .limit(step)
                    .asQueryResultList();
                cursorMap.put("cursor", results.getEncodedCursor());
                cursorMap.put("filters", results.getEncodedFilters());
                cursorMap.put("sorts", results.getEncodedSorts());  
            return results.iterator();
        }
    }
    public Iterator<Tag> getTags(int size) {        
        TagMeta m = TagMeta.get();
        return Datastore.query(m).sort(m.count.desc).limit(size).asIterator();
        
    }
    public Iterator<Question> getQuizes(String question, List<String> tags,
            Date start, Date end, String user) 
    {
        QuestionMeta m = QuestionMeta.get();
        List<FilterCriterion> flist = new LinkedList<FilterCriterion>();
        List<InMemoryFilterCriterion> iflist = new LinkedList<InMemoryFilterCriterion>();
        if(question != null){
            iflist.add(m.question.contains(question));
        }        
        if(!tags.isEmpty()){
            for(String tag:tags){
                iflist.add(m.tags.contains(tag));
            }
        }        
        if(start != null){
            flist.add(m.createdDate.greaterThan(start));
        }
        if(end != null){
            flist.add(m.createdDate.lessThan(end));
        }
        if(user != null){
            flist.add(m.createdBy.equal(user));
        }
        FilterCriterion[] array = (FilterCriterion[])flist.toArray(new FilterCriterion[flist.size()]);
        InMemoryFilterCriterion[] iarray = (InMemoryFilterCriterion[])iflist.toArray(new InMemoryFilterCriterion[iflist.size()]);
        return Datastore.query(m).filter(array).filterInMemory(iarray).asList().iterator();

    }
    public boolean isValidUser(String username, String password) {
        
        UserMeta u = UserMeta.get();
        User user = Datastore.query(u).filter(u.username.equal(username.trim()), u.password.equal(password.trim())).asSingle();
        if(user == null){
            return false;
        }else{
            return true;
        }
    }
    
    private boolean isExistinUser(String username){
        UserMeta u = UserMeta.get();
        User user = Datastore.query(u).filter(u.username.equal(username)).asSingle();
        if(user == null){
            return false;
        }else{
            return true;
        }
    }
    
    public void addUser(User user) throws UserManagementException{
        if(isExistinUser(user.getUsername())){
            throw new UserManagementException("already exists");
        }
        Transaction tx = Datastore.beginTransaction();        
        Datastore.put(user);
        tx.commit();
    }
    public String generateSessionCookie(String username) {
        // TODO Auto-generated method stub
        Transaction tx = Datastore.beginTransaction();
        Session session = new Session();
        session.setUser(username);               
        Key key = Datastore.put(session);        
        tx.commit();
        return KeyFactory.keyToString(key);
    }
    public boolean isValidSession(String sessionInfo) {
        Key key = KeyFactory.stringToKey(sessionInfo);
        if(Datastore.get(key) != null){
            return true;
        }else{
            return false;
        }
    }
    public Session getSession(String sessionInfo) {
        Key key = KeyFactory.stringToKey(sessionInfo);
        SessionMeta s = SessionMeta.get();        
        return Datastore.query(s).filter(s.key.equal(key)).asSingle();
    }
    public void addTag(String parent, String childs, boolean increment) {
        
        Tag tag  = getTag(parent);
        if(tag != null ){
            if(increment){
                tag.setCount(tag.getCount() +1);                
            }
        }else{
            tag = new Tag();
        
            tag.setAdmin(false);
            tag.setCount(0);
            tag.setName(parent.trim().toLowerCase());
        }
        
        if(Utils.isValid(childs)){
            List<String> clist = new LinkedList<String>();
            for(String c:childs.split(";")){            
                clist.add(c.trim().toLowerCase());
                //check if the tag exists - if not add it too
                addTag(c, null, increment);
            }
            tag.getChilds().addAll(clist);
        }
        Transaction tx = Datastore.beginTransaction();                     
        Datastore.put(tag);        
        tx.commit();        
    }
    private Tag getTag(String name){
        TagMeta t = TagMeta.get();
        return Datastore.query(t).filter(t.name.equal(name.trim().toLowerCase())).asSingle();
    }
    public ServiceResponse addQuestion(String type, Long id, String question, Session session, String mid) throws ServletException {
        //check type of the quiz
        if(!this.quizTypeMap.containsKey(type)){
            throw new ServletException("type is invalid");
        }
        ServiceResponse response = new ServiceResponse();
        Question qt = this.quizTypeMap.get(type);
        Gson gson = new Gson();
        System.err.println(question);
        Question quiz = (Question) gson.fromJson(question, qt.getClass());
        if(id != null){
            Key key = KeyFactory.createKey(QuestionMeta.get().getKind(), id);
            quiz.setKey(key);
        }
        quiz.setCreatedBy(session.getUser());        
        //process tag to categorize 
        int count = 0;
        if(quiz instanceof Question){            
            TagMeta m = TagMeta.get();
            for(String tag: quiz.getTags()){
                Tag storedTag = Datastore.query(m).filter(m.name.equal(tag)).asSingle();                
                Transaction tx = Datastore.beginTransaction();
                if(storedTag != null){                    
                    storedTag.setCount(storedTag.getCount() +1 );                    
                    System.out.println("Updating tag:"+ tag);
                }else{
                    storedTag = new Tag();
                    storedTag.setAdmin(false);
                    storedTag.setCount(1);
                    storedTag.setName(tag);
                    storedTag.setWeight(1);
                    System.out.println("Added tag:"+ tag);
                }
                if(storedTag.getName().equals(mid)){
                	count = storedTag.getCount();
                }
                Datastore.put(storedTag);
                tx.commit();
                
            }            
        }                
        if(quiz != null){
            System.err.println(quiz.toString());
            Transaction tx = Datastore.beginTransaction();
            Datastore.put(quiz);
            tx.commit();     
        }
        response.setStatus(true);
        KeyValueMap map = new KeyValueMap();
        map.put("code", "ADDED");
        if(mid != null){
			map.put("total", Integer.toString(count));
			ModuleMeta m = ModuleMeta.get();
			Key modKey = KeyFactory.createKey(m.getKind(), Long.parseLong(mid));
			Module module = Datastore.query(m).filter(m.key.equal(modKey))
					.asSingle();
			module.setQuestionCount(count);
			Transaction tx = Datastore.beginTransaction();
			Datastore.put(module);
			tx.commit();  
        }
        response.setMetaData(map.toJson());
        return response;
        
        
    }
    public ServiceResponse deleteQuestion(String id, Session session) {
        QuestionMeta m = QuestionMeta.get();
        ServiceResponse result = new ServiceResponse();
        Key key = KeyFactory.createKey(m.getKind(), Long.parseLong(id));
        Question q = Datastore.query(m).filter(m.key.equal(key)).asSingle();
        if(q == null){
            result.setStatus(false);
            result.setMetaData("invalid key");
        }else{
            Transaction tx = Datastore.beginTransaction();
            Datastore.delete(key);
            tx.commit();  
            result.setStatus(true);
            result.setMetaData("deleted question");
            
        }
        // TODO Auto-generated method stub
        return result;
    }
    public ServiceResponse updateQuestion(String id, String question,
            Session session) throws ServletException {
        QuestionMeta m = QuestionMeta.get();
        ServiceResponse result = new ServiceResponse();
        long lid = Long.parseLong(id);
        Key key = KeyFactory.createKey(m.getKind(), lid);
        Question q = Datastore.query(m).filter(m.key.equal(key)).asSingle();
        if(q == null){
            result.setStatus(false);
            result.setMetaData("invalid key");
        }else{
            if(q instanceof Objective){
                return this.addQuestion("objective", lid, question, session, null);
            }            
            result.setStatus(false);
            result.setMetaData("cannot update");
        }
        return result;
    }
    public ServiceResponse basicSearch(String val, Session session) {
        Gson gson = new Gson();
        BasicSearch search = gson.fromJson(val, BasicSearch.class);
        Iterator<Question> quizes = getQuizes(search, session);
        List<String> questions = new LinkedList<String>();
        ServiceResponse response = new ServiceResponse();
        while(quizes.hasNext()){
            Question q = quizes.next();
            if(!search.isForUpdate()){
                if(q instanceof Objective){
                    Objective o = (Objective)q;
                    for(Option op: o.getAnswers()){
                        op.setCorrect(false);
                    }
                }
            }
            questions.add(gson.toJson(q, q.getClass()));            
        }
        response.setStatus(true);
        String qstring = gson.toJson(questions); 
        response.setMetaData(qstring);
        return response;
    }
    public ServiceResponse matchSearch(String val, Session session) {
        Gson gson = new Gson();
        MatchSearch search = gson.fromJson(val, MatchSearch.class);
        ServiceResponse response = new ServiceResponse();
        List<Option> options = new LinkedList<Option>();
        boolean result = matchAnswers(search, session, options);        
        response.setStatus(result);
        String soption = gson.toJson(options); 
        response.setMetaData(soption);
        return response;
    }    

    public boolean matchAnswers(MatchSearch search, Session session,
            List<Option> options) 
    {
        ObjectiveMeta m = ObjectiveMeta.get();        
        Key key = KeyFactory.createKey(m.getKind(), search.getId());
        Objective o = Datastore.query(m).filter(m.key.equal(key)).asSingle();
        if(o == null){
            return false;
        }
        boolean failed = false;
        for (Option answer : search.getAnswers()) {
            if (!failed) {
                for (Option option : o.getAnswers()) {
                    if (answer.getId() == option.getId()) {
                        if (answer.isCorrect() != option.isCorrect()) {
                            failed = true;
                            break;
                        }
                    }
                }
            } else {
                break;
            }

        }
        if(options != null){
            options.addAll(o.getAnswers());
        }
        return failed;
    }
    public Iterator<Question> getQuizes(BasicSearch search, Session session) {
        QuestionMeta m = QuestionMeta.get();
        List<FilterCriterion> flist = new LinkedList<FilterCriterion>();
        List<InMemoryFilterCriterion> iflist =
            new LinkedList<InMemoryFilterCriterion>();
        if (search.getQuestion() != null) {
            iflist.add(m.question.contains(search.getQuestion()));
        }
        if(search.getId() != null){
            Key key = KeyFactory.createKey(m.getKind(), search.getId());
            flist.add(m.key.equal(key));
        }
        if (!search.getTags().isEmpty()) {
            for (String tag : search.getTags()) {
                iflist.add(m.tags.contains(tag));
            }
        }
        if (search.getStartTime() != null) {
            flist.add(m.createdDate.greaterThan(search.getStartTime()));
        }
        if (search.getEndTime() != null) {
            flist.add(m.createdDate.lessThan(search.getEndTime()));
        }
        if (search.isForUpdate()) {
            if (session.getUser() != null && session.getUser() != ADMIN) {
                flist.add(m.createdBy.equal(session.getUser()));
            }
        }        
        //check if there is a last id value for this 
        if(search.getLastId() != null){
            Key key = KeyFactory.createKey(m.getKind(), search.getLastId());
            flist.add(m.key.greaterThan(key));
        }
        FilterCriterion[] array =
            (FilterCriterion[]) flist
                .toArray(new FilterCriterion[flist.size()]);
        InMemoryFilterCriterion[] iarray =
            (InMemoryFilterCriterion[]) iflist
                .toArray(new InMemoryFilterCriterion[iflist.size()]);
        List<Question> list =  Datastore
            .query(m)
            .filter(array)
            .filterInMemory(iarray)
            .sort(m.key.desc)
            .limit(search.getLimit())
            .asList();       
       return list.iterator();

    }
    public ServiceResponse search(String type, String query, Session session) {
        if(type.equals("basic")){
            return this.basicSearch(query, session);
        } else if(type.equals("match")){
            return this.matchSearch(query, session);
        } else{
            ServiceResponse response = new ServiceResponse();
            response.setStatus(false);
            response.setMetaData("Invalid query type");
            return response;
        }
        
    }
    public void removeSession(Session session) {       
        Transaction tx = Datastore.beginTransaction();                               
        Datastore.delete(session.getKey());        
        tx.commit();        
        
    }

	public Module createModule(String mname, String user) {
		log.info("Creating a module with name:" +mname);
		Module module = new Module();
		module.setName(mname);
		module.setOwner(user);
        Transaction tx = Datastore.beginTransaction();                               
        Datastore.put(module);        
        tx.commit();        
        return module;
	}
    
}
