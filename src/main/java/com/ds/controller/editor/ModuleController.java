package com.ds.controller.editor;

import java.util.List;
import com.google.appengine.api.datastore.Transaction;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.ds.meta.ModCopyMeta;
import com.ds.meta.ModuleMeta;
import com.ds.model.ModCopy;
import com.ds.model.Module;
import com.ds.model.QuestionState;
import com.ds.model.Session;
import com.ds.model.json.KeyValueMap;
import com.ds.service.QuizProcessorService;
import com.ds.service.ServiceResponse;
import com.ds.util.StaticValues;
import com.ds.util.Utils;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ModuleController extends Controller {

	private QuizProcessorService service = QuizProcessorService.getInstance();
	private static final Logger log = Logger.getLogger(ProcessController.class
			.getName());

	@Override
	public Navigation run() throws Exception {
		log.info("Processing quiz");
		Session session = Utils.getSession(request.getCookies());
		if (session == null) {
			return redirect("../login");
		}
		// based on the opcode process request
		String op = (String) request.getAttribute(StaticValues.QUIZ_NAMESPACE
				+ "op");
		if (!Utils.isValid(op)) {
			throw new ServletException("op cannot be empty");
		}
		if (op.equals("create_mod")) {
			String mname = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "module");
			String description = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "description");
			String parent = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "parent");
			String prevSibling = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "prevSibling");
			Module mod = null;
			if (parent != null && !parent.equals("none")) {
				log.info("calling createModuleAsChild");
				mod = service.createModuleAsChild(mname, description,
						Long.parseLong(parent), session.getUser());
			} else if (prevSibling != null && !prevSibling.equals("none")) {
				log.info("calling createModuleAsSibling");
				mod = service.createModuleAsSibling(mname, description,
						Long.parseLong(prevSibling), session.getUser());
			} else {
				log.info("calling createModule");
				mod = service.createModule(mname, description,
						session.getUser());
			}
			request.setAttribute(StaticValues.QUIZ_NAMESPACE + "modid", mod
					.getKey().getId());
			request.setAttribute(StaticValues.QUIZ_NAMESPACE + "mname",
					mod.getName());
			return forward("module.jsp");
		} else if (op.equals("update_mod")) {
			String mid = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "mid");
			String mname = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
			request.setAttribute(StaticValues.QUIZ_NAMESPACE + "modid", mid);
			request.setAttribute(StaticValues.QUIZ_NAMESPACE + "mname", mname);
			return forward("module.jsp");
		}
		else if (op.equals("delete_mod")) {
			String mid = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "mid");
			String mname = (String) request
					.getAttribute(StaticValues.QUIZ_NAMESPACE + "mname");
			Utils.respond(onDeleteModule(Long.parseLong(mid)), response);
			return null;
		}
		return null;
	}

	private ServiceResponse onDeleteModule(long mid) {
		log.info("onDeleteModule:" + mid);
		ModuleMeta m = ModuleMeta.get();
		Key key = KeyFactory.createKey(m.getKind(), mid);
	    Module module =
	            Datastore
	                .query(m)
	                .filter(m.key.equal(key))
	                .asSingle();
	    if (module == null) {
	        return Utils.sendError("Unable to find module");
	    }
	    if(module.getFirstChild() != null){
	    	removeTree(module.getFirstChild());
	    }
	    //adjust the self.prevSibling, parent.firstChild
	    if(module.getParent() != null){
	    	Key pKey = KeyFactory.createKey(m.getKind(), module.getParent());
	    	Module parent = Datastore.query(m).filter(m.key.equal(pKey)).asSingle();
	    	if(parent != null && (parent.getFirstChild() == module.getKey().getId())){
	    		parent.setFirstChild(module.getNextSibling());
	    		Transaction tx = Datastore.beginTransaction();
	    		Datastore.put(parent);
	    		tx.commit();
	    	}
	    }
	    if(module.getPrevSibling() != null){
	    	Key pKey = KeyFactory.createKey(m.getKind(), module.getPrevSibling());
	    	Module prevSibling = Datastore.query(m).filter(m.key.equal(pKey)).asSingle();
	    	if(prevSibling != null ){
	    		prevSibling.setNextSibling(module.getNextSibling());
	    		Transaction tx = Datastore.beginTransaction();
	    		Datastore.put(prevSibling);
	    		tx.commit();
	    	}
	    }
	    //now delete self
	    Transaction tx = Datastore.beginTransaction();
	    Datastore.delete(key);
	    tx.commit();
	    return Utils.sendSuccess("Successfully deleted module");
	       
	}
	private void removeTree(long mid){
		log.info("removeTree:" + mid);
		ModuleMeta m = ModuleMeta.get();
		Key key = KeyFactory.createKey(m.getKind(), mid);
	    Module module =
	            Datastore
	                .query(m)
	                .filter(m.key.equal(key))
	                .asSingle();
	    if (module == null) {
	    	log.info("Unable to find module");	        
	    }
	    if(module.getFirstChild() != null){
	    	removeTree(module.getFirstChild());
	    }
	    if(module.getNextSibling() != null){
	    	removeTree(module.getNextSibling());
	    }
	    //remove the module
	    Transaction tx = Datastore.beginTransaction();
	    Datastore.delete(key);
	    tx.commit();	    
	}
}
