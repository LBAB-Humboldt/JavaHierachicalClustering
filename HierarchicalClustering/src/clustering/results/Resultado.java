package clustering.results;

import java.util.ArrayList;

import clustering.utils.DisjoinDataset;
import clustering.utils.Punto;
import datasets.Dataset;

public class Resultado extends ArrayList<ClusteringResult>{
	Dataset dataset;
	public Resultado(Dataset dataset){
		this.dataset=dataset;
	}
	private static final long serialVersionUID = -4884022996597646245L;
	public ResultadoMerge getMerge(){
		ResultadoMerge ret =new ResultadoMerge(size());
		int[] grupos=new int[size()+2];
		DisjoinDataset dataset=new DisjoinDataset();
		for(int e=0;e<ret.length;e++){
			int id1=dataset.get(get(e).id1);
			int id2=dataset.get(get(e).id2);
			dataset.link(get(e).id1, get(e).id2);
			ret.merge[0][e]=grupos[id1]==0?-id1:grupos[id1];
			ret.merge[1][e]=grupos[id2]==0?-id2:grupos[id2];
			grupos[id1<id2?id1:id2]=e+1;
		}
		return ret;
	}
	public double[] getDistances(){
		double[] ret =new double[size()];
		for(int e=0;e<ret.length;e++)ret[e]=get(e).dist;
		return ret;
	}
	public String[] getRowLabels(){
		return dataset.getRowNames();
	}
}
