package CS230.pom_parser;

import java.util.ArrayList;

public class ApiStorage {

	String fullyQualifiedName;
	String accessModifier;
	ArrayList<String> paramters;
	String returnType;
	
	public ApiStorage(String fullyQualifiedName, String accessModifier, ArrayList<String> paramters, String returntype){
		this.fullyQualifiedName = fullyQualifiedName;
		this.accessModifier = accessModifier;
		this.paramters = paramters;
		this.returnType = returntype;
	}
	
	public boolean equals(ApiStorage b) {
        boolean fqn = this.fullyQualifiedName != null && b.fullyQualifiedName != null ? this.fullyQualifiedName.equals(b.fullyQualifiedName) : false;
        boolean am = this.accessModifier != null && b.accessModifier != null ? this.accessModifier.equals(b.accessModifier) : false;
        boolean param = false;
        if(this.paramters != null && b.paramters != null ){
        	param =  this.paramters.size() == b.paramters.size();
    		for(int i = 0; i < this.paramters.size() && param; i++){
    			if( !this.paramters.get(i).equals("*") && !b.paramters.get(i).equals("*")){
    				param = param && this.paramters.get(i).equals(b.paramters.get(i));
    			}
    		}
        }
        boolean rt = this.returnType != null && b.returnType != null ? this.returnType.equals(b.returnType) : false;
        return fqn && am && param && rt;
    }
	
	public int hashCode(){
//		hashcodes must be same if equals return true
		int h1 = this.fullyQualifiedName != null ? this.fullyQualifiedName.hashCode() : 0;
		int h2 = this.accessModifier != null ? this.accessModifier.hashCode() : 0;
		int h3 = this.returnType != null ? this.returnType.hashCode() : 0;
		// not considereing since they may be differnt even if equal gives same ans
		int h4 = this.paramters != null ? this.paramters.hashCode() : 0;
		
		return ((((((31 + h1)*31)+h2)*31)+h3)*31);
	}
	
	 
}
