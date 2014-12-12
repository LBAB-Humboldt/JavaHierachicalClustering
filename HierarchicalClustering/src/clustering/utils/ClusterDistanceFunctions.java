package clustering.utils;

public enum ClusterDistanceFunctions {
	SINGLE_LINK{
		@Override
		public double calculateDistance(Cluster c1, Cluster c2) {
			double dist=Double.POSITIVE_INFINITY;
			for(Punto p1:c1)for(Punto p2:c2)dist=Math.min(SimilarityIndex.instance.distance(p1, p2),dist);
			return dist;
		}
	},
	COMPLETE_LINK{
		@Override
		public double calculateDistance(Cluster c1, Cluster c2) {
			double dist=Double.NEGATIVE_INFINITY;
			for(Punto p1:c1)for(Punto p2:c2)dist=Math.max(SimilarityIndex.instance.distance(p1, p2),dist);
			return dist;
		}
	},
	AVERAGE_LINK{
		@Override
		public double calculateDistance(Cluster c1, Cluster c2) {
			double dist=0;
			for(Punto p1:c1)for(Punto p2:c2)dist+=SimilarityIndex.instance.distance(p1, p2);
			return dist/(c1.size()*c2.size());
		}
	};
	public static ClusterDistanceFunctions instance=SINGLE_LINK; 
	public abstract double calculateDistance(Cluster c1, Cluster c2);
	public static void setInstance(String name){
		if(name.toLowerCase().equals("average"))instance=AVERAGE_LINK;
		else if(name.toLowerCase().equals("single"))instance=SINGLE_LINK;
		else if(name.toLowerCase().equals("complete"))instance=COMPLETE_LINK;
		else instance=SINGLE_LINK;
	}
}
