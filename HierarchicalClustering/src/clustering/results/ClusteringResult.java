package clustering.results;

public class ClusteringResult {
	public int id1,id2;
	public double dist;
	public ClusteringResult(int id1, int id2, double dist) {
		this.id1 = id1;
		this.id2 = id2;
		this.dist = dist;
	}
	@Override
	public String toString() {
		return "[" + id1 + ", " + id2 + ", dist="+ dist + "]";
	}
	
}
