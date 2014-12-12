package datasets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import clustering.AbstractClustering;
import clustering.ArraySingleLinkClustering;
import clustering.ArrayThread;
import clustering.results.Resultado;
import clustering.utils.Punto;
import clustering.utils.SimilarityIndex;

public class DatasetGenerator {
	private List<Punto> puntos=new ArrayList<Punto>();
	private String[] columnNames;
	private BitSet bs=new BitSet();
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public void addPoint(String rowName,int[] data){
		bs.clear();
		for(int e=0;e<data.length;e++)if(data[e]==1)bs.set(e);
		if(!bs.isEmpty()){
			final long[] apariciones=new long[(data.length-1)/64+1];
			long[] temp = bs.toLongArray();
			System.arraycopy(temp,0, apariciones, 0, temp.length);
			puntos.add(new Punto(rowName, puntos.size()+1, apariciones));
		}
	}
	public int size(){
		return puntos.size();
	}
	public Resultado directClustering(String pointDistanceFunction,int cores){
		if(!SimilarityIndex.isValid(pointDistanceFunction))return null;//"Invalid distante function.";
		SimilarityIndex.setInstance(pointDistanceFunction);
		AbstractClustering<?> at = new ArrayThread();
		at.process(cores,getDataset());
		return at.getResultado();
	}
	
	public Resultado singleLinkAgglomerativeClustering(String pointDistanceFunction,int cores){
		if(!SimilarityIndex.isValid(pointDistanceFunction))return null;//"Invalid distante function.";
		SimilarityIndex.setInstance(pointDistanceFunction);
		AbstractClustering<?> at = new ArraySingleLinkClustering();
		at.process(cores,getDataset());
		return at.getResultado();
	}
	public Dataset getDataset(){
		return new Dataset(columnNames, puntos.toArray(new Punto[0]));
	}
}
