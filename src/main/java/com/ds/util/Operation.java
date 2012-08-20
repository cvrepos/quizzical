package com.ds.util;

import java.util.logging.Logger;



public enum Operation {
		
	LOGIN(ConstStrings.LOGIN),
	LOGOUT(ConstStrings.LOGOUT),
	ADDUSER(ConstStrings.ADDUSER),
	DELETEUSER(ConstStrings.DELETEUSER),
	INVALID("invalid");
	private final String altName;
	private final static Logger log = Logger.getLogger(Operation.class.getName());
	Operation(String altname){		
		this.altName = altname;
	}
	public static Operation getOperation(String name){	
		log.info("getOperation for :" + name);
		if(name.equals(LOGIN.altName)){
			return LOGIN;
		}else if(name.equals(LOGOUT.altName)){
			return LOGOUT;
		}else if(name.equals(ADDUSER.altName)){
			return ADDUSER;
		}else if(name.equals(DELETEUSER.altName)){
			return DELETEUSER;
		}else{
			return INVALID;
		}
	}
	@Override 
	public String toString(){
		return altName;
	}

}
