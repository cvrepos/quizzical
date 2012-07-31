// the base component class
var QUIZZICAL= {
		function Component(name){
			this.setName(name);
		}
		Component.method("setName", function(name){
			this.name = name;
		});
		
		Component.method("load", function(){
			alert("base load");
		});		
};