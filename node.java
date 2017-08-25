import java.util.ArrayList;
import java.util.List;

public class node implements Comparable<node>{
  public List<car> state;
  public int ES;
  public int depth;
  node(List<car> s, int e, int d){
	  state=new ArrayList<>(s); state.clear();
	  listcopy(state,s);
	  ES=e;
	  depth=d;
  }
  public void listcopy(List<car> dest,List<car> src){
		for(car c: src)
			dest.add(new car(c.id,c.row,c.col,c.len,c.Ori));
  }
  public int compareTo(node x) {   //min heap
      return this.ES < x.ES ? -1 : 1;  
  }  
}
