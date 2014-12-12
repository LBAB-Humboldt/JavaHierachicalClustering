package clustering.results;

public class ResultadoMerge {
	public int[][] merge;
	public final int length;
	public ResultadoMerge(int size) {
		merge=new int[2][size];
		length=size;
	}
	public int[] getId1(){
		return merge[0];
	}
	public int[] getId2(){
		return merge[1];
	}
}
