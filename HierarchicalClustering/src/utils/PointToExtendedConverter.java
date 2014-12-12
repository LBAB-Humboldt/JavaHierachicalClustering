package utils;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.concurrent.RecursiveAction;

import clustering.results.ClusteringResult;
import clustering.results.Resultado;
import clustering.utils.Punto;

public class PointToExtendedConverter {
	public static Punto[] calcularMinimos(Punto[] puntos,Resultado resultado){
		PrecalculatedDataset precalculated=processDatasetWithIndex(puntos, resultado);
		return precalculated.puntos;
	}
	public static PrecalculatedDataset processDatasetWithIndex(Punto[] puntos,Resultado resultado){
		TreeMap<long[], Punto> mapPuntos=new TreeMap<long[], Punto>(LONG_ARRAY_COMPARATOR);
		for(int e=0;e<puntos.length;e++){
			Punto p = mapPuntos.put(puntos[e].apariciones,puntos[e]);
			if(p!=null){
				resultado.add(new ClusteringResult(p.rowPosition,puntos[e].rowPosition, 0));
			}
		}
		puntos=mapPuntos.values().toArray(new Punto[0]);
		Punto[]ret=new Punto[puntos.length];
		for(int e=0;e<puntos.length;e++)ret[e]=new Punto(puntos[e]);

		for(int e=0;e<puntos.length;e++)ret[e]=new Punto(puntos[e]);
		PrecalculatedDataset precalculated=new PrecalculatedDataset(ret);
		Utils.FORK_JOIN.invoke(new Precalculate(precalculated, 0, puntos.length));
		return precalculated;
	}
	private static class Precalculate extends RecursiveAction{
		PrecalculatedDataset precalculated;
		private static final long serialVersionUID = 1432423L;
		static final int thredshold=5000;
		int start,end;
		public Precalculate(PrecalculatedDataset precalculated, int s, int e) {
			this.precalculated=precalculated;
			this.start = s;
			this.end = e;
		}
		@Override
		protected void compute(){
			if(end-start<thredshold){
				for(int e=start;e<end;e++){
					for(int i=e+1;i<precalculated.puntos.length;i++){
						if(precalculated.puntos[e].setMin(precalculated.puntos[i])){
							precalculated.minimos[e]=i;
						}
					}
				}
				System.out.println(start+" to "+end+" preprocessed");
			}else{
				int m=(end+start)/2;
				invokeAll(new Precalculate(precalculated,start,m),new Precalculate(precalculated,m,end));
			}
		}
	}
	public static int bitCount(long[] apps){
		int a=0;
		for(long e:apps)a+=Long.bitCount(e);
		return a;
	}
	private static final Comparator<long[]> LONG_ARRAY_COMPARATOR=new Comparator<long[]>() {
		@Override
		public int compare(long[] o1, long[] o2) {
			int a = bitCount(o1) - bitCount(o2);
			if(a!=0)return -a;
			for(int e=0;e<o1.length;e++)if(o1[e]!=o2[e])return Long.compare(o1[e],o2[e]);
			return 0;
		}
	}; 
}
