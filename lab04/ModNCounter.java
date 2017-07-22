/**
 * Created by yunan on 6/23/17.
 */
public class ModNCounter extends Counter {

    int div;
    public ModNCounter(int n){
        super();
        this.div=n;
    }
    public void increment(){
	super.increment();
        if(super.value()>=div){
            super.reset();
}
        
            
        

    }
}
