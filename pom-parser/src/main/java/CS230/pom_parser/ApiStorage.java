package CS230.pom_parser;

import java.io.Serializable;
import java.util.ArrayList;

public class ApiStorage implements Serializable {

	String fullyQualifiedName;
	//String accessModifier;
	ArrayList<String> paramters;
	//String returnType;
	public static ApiStorage total=new ApiStorage("totalCount", null);
	
	public ApiStorage(String fullyQualifiedName, String accessModifier, ArrayList<String> paramters, String returntype){
		this.fullyQualifiedName = fullyQualifiedName;
		//this.accessModifier = accessModifier;
		this.paramters = paramters;
		//this.returnType = returntype;
	}
	
	public ApiStorage(String fullyQualifiedName, ArrayList<String> paramters){
		this.fullyQualifiedName = fullyQualifiedName;
		this.paramters = paramters;
	}
	
	public static ApiStorage getInstance(){
		return total;
	}
	
	public boolean equals(Object a) {
		ApiStorage b = (ApiStorage) (a);
        boolean fqn = this.fullyQualifiedName != null && b.fullyQualifiedName != null ? this.fullyQualifiedName.equals(b.fullyQualifiedName) : false;
//        boolean am = this.accessModifier != null && b.accessModifier != null ? this.accessModifier.equals(b.accessModifier) : false;
        boolean param = true;
        if(this.paramters != null && b.paramters != null ){
        	param =  this.paramters.size() == b.paramters.size();
    		for(int i = 0; i < this.paramters.size() && param; i++){
    			if( !this.paramters.get(i).equals("*") && !b.paramters.get(i).equals("*")){
    				if(!this.paramters.get(i).equals(b.paramters.get(i))){
    					param=false;
    					try {
    						Class c1=Class.forName(this.paramters.get(i));
    						Class c2=Class.forName(b.paramters.get(i));
    						param=(c1.isAssignableFrom(c2)||c2.isAssignableFrom(c1));
    					} catch (ClassNotFoundException e) {
    						// TODO Auto-generated catch block
    						//e.printStackTrace();
    					}
        				
    				}
    				//param = param && this.paramters.get(i).equals(b.paramters.get(i));   //check if they are compatible
    				
    				
    			}
    		}
        }
//        boolean rt = this.returnType != null && b.returnType != null ? this.returnType.equals(b.returnType) : false;
        return fqn && param;
    }
	
	public int hashCode(){
//		hashcodes must be same if equals return true
		int h1 = this.fullyQualifiedName != null ? this.fullyQualifiedName.hashCode() : 0;
//		int h2 = this.accessModifier != null ? this.accessModifier.hashCode() : 0;
//		int h3 = this.returnType != null ? this.returnType.hashCode() : 0;
		// not considereing since they may be differnt even if equal gives same ans
		int h4 = this.paramters != null ? this.paramters.size() : 0;
		
		return (((31 + h1)*31)+h4);
	}
	
	public String toString(){
		return fullyQualifiedName;
	}
	
}
