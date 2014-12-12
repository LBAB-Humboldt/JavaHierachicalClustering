package clustering.utils;

import java.util.ArrayList;

public class Cluster extends ArrayList<Punto>{
	private static final long serialVersionUID = -5621413500564387589L;
	public int canonicalId;
	public boolean deleted=false;
	
	public Cluster nearestCluster=null;
	public double distToNearestCluster=Double.POSITIVE_INFINITY;
	
	
	public Cluster(int id,Punto p){
		canonicalId=id;
		add(p);
	}
	public Cluster(Cluster c1,Cluster c2) {
		canonicalId=Math.min(c1.canonicalId,c2.canonicalId);
		addAll(c1);
		addAll(c2);
	}
	public void resetNearest(){
		nearestCluster=null;
		distToNearestCluster=Double.POSITIVE_INFINITY;
	}
	public void setMin(Cluster c){
		double dist=ClusterDistanceFunctions.instance.calculateDistance(this, c);
		if(dist<distToNearestCluster){
			distToNearestCluster=dist;
			nearestCluster=c;
		}
	}
	
	//"ward (solo para distancias euclidianas)","median (solo para distancias euclidianas)","centroid (solo para distancias euclidianas)"
	/*public double distance(Cluster c){
		return DistanceFunctions.SINGLE_LINK.calculateDistance(this,c);
	}*/
}
