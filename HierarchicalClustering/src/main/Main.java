package main;

import java.io.File;

import clustering.AbstractClustering;
import clustering.ArraySingleLinkClustering;
import clustering.ArrayThread;
import clustering.results.Resultado;
import clustering.utils.SimilarityIndex;
import datasets.Dataset;

public class Main{
	public Resultado directClustering(String inputTPA,String pointDistanceFunction,int cores){
		if(!SimilarityIndex.isValid(pointDistanceFunction))return null;//"Invalid distante function.";
		SimilarityIndex.setInstance(pointDistanceFunction);
		return processClustering(new ArrayThread(), inputTPA, cores);
	}
	
	public Resultado singleLinkAgglomerativeClustering(String inputTPA,String pointDistanceFunction,int cores){
		if(!SimilarityIndex.isValid(pointDistanceFunction))return null;//"Invalid distante function.";
		SimilarityIndex.setInstance(pointDistanceFunction);
		return processClustering(new ArraySingleLinkClustering(), inputTPA, cores);
	}
	private static Resultado processClustering(AbstractClustering<?> at,String inputTPA,int cores){
		if(!new File(inputTPA).exists())return null;//"Error reading input file: File not found";
		Dataset d=Dataset.fromCsv(new File(inputTPA));
		at.process(cores,d);
		return at.getResultado();
	}
}
