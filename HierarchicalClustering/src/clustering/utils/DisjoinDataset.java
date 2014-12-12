package clustering.utils;

import java.util.HashMap;

public class DisjoinDataset extends HashMap<Integer,Integer>{
	private static final long serialVersionUID = 5786118400618112441L;
	public void link(int id1,int id2){
		id1=get(id1);
		id2=get(id2);
		int min=id1<id2?id1:id2;
		put(id1,min);
		put(id2,min);
	}
	public int get(int id1){
		Integer t=super.get(id1);
		if(t==null)return id1;
		while(t!=id1){
			int temp = id1;
			put(temp,t=get(id1=t));
		}
		return t;
	}
	public boolean sameSet(int id1,int id2){
		return get(id1)==get(id2);
	}
}
