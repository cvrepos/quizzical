// the base component class
var QUIZZICAL= {
		function PathComponent(name){
			this.setName(name);
		}
		//set the inheritance relationship from BaseComponent
		PathComponent.inherits(BaseComponent);
		
		PathComponent.method("load", function(){
			//look at the existing path
			$("#path").data()
		});		
};