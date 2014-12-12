package clustering;

import java.util.concurrent.Callable;

import utils.PointToExtendedConverter;
import utils.PrecalculatedDataset;
import utils.Utils;
import clustering.results.ClusteringResult;
import clustering.utils.Cluster;
import clustering.utils.Punto;

public class GenericAgglomerativeClustering extends AbstractClustering<Cluster>{
	private Cluster[] clusters;
	
	@Override
	public void inicializar(Punto[] dataset) {
		PrecalculatedDataset precalculated=PointToExtendedConverter.processDatasetWithIndex(dataset,resultado);
		clusters=new Cluster[dataset.length+1];
		size=dataset.length;
		for(int e=0;e<dataset.length;e++)clusters[dataset.length-e-1]=new Cluster(dataset[e].rowPosition,dataset[e]);
		for(int e=0;e<dataset.length;e++)clusters[dataset.length-e-1].setMin(clusters[precalculated.minimos[e]]);
	}
	
	@Override
	public ClusteringResult calcularMinimo()throws Exception{
		next=null;
		for(int e=0;e<size;e++)next=min(clusters[e],next);
		return new ClusteringResult(next.canonicalId,next.nearestCluster.canonicalId,next.distToNearestCluster);
	}
	@Override
	public void removerPuntos(){
		next.deleted=true;
		if(next.nearestCluster!=null)next.nearestCluster.deleted=true;
		int i=0;
		for(int e=0;e<size;e++){
			if(clusters[e].deleted)i++;
			else if(i!=0)clusters[e-i]=clusters[e];
		}
		for(int e=0;e<i;e++)clusters[size-e-1]=null;
		size-=i;
	}
	@Override
	public void actualizar()throws Exception{
		System.out.print("actualizar "+size+" ");
		long t=System.currentTimeMillis();
		if(next.nearestCluster!=null){
			clusters[size++]=combinado=new Cluster(next,next.nearestCluster);
			for(int e=0;e<size-1;e++){
				if(clusters[e].nearestCluster==null||clusters[e].nearestCluster.deleted){
					recalcFutures.add(Utils.EXEC.submit(new RecalcTask(e)));
				}
				combinado.setMin(clusters[e]);
			}
		}
		System.out.print(recalcFutures.size()+" ");
		Utils.join(recalcFutures);
		System.out.println(System.currentTimeMillis()-t);
	}
	class RecalcTask implements Callable<Object>{
		final int i;
		public RecalcTask(int i) {
			this.i = i;
		}
		@Override
		public Object call() throws Exception {
			Cluster p=clusters[i];
			p.resetNearest();
			for(int e=0;e<i;e++){
				p.setMin(clusters[e]);
			}
			return null;
		}
	}
}
