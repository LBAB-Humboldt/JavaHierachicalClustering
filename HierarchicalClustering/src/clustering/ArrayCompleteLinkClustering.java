package clustering;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import utils.PointToExtendedConverter;
import utils.Utils;
import clustering.results.ClusteringResult;
import clustering.utils.DisjoinDataset;
import clustering.utils.Punto;

public class ArrayCompleteLinkClustering extends AbstractClustering<Punto>{
	private Punto[] points;
	private DisjoinDataset dataset=new DisjoinDataset();
	public void inicializar(Punto[] dataset) {
		totalTime-=System.currentTimeMillis();
		times[0]-=System.currentTimeMillis();
		dataset=PointToExtendedConverter.calcularMinimos(dataset,resultado);
		totalSize=dataset.length;
		points=new Punto[dataset.length+1];
		for(int e=0;e<dataset.length;e++)points[dataset.length-e-1]=dataset[e];
		size=dataset.length;
		times[0]+=System.currentTimeMillis();
	}
	
	@Override
	public ClusteringResult calcularMinimo()throws Exception{
		next=null;
		for(int e=0;e<size;e++)next=min(points[e],next);
		return new ClusteringResult(next.rowPosition,next.nearestPoint.rowPosition,next.distanceToNearestPoint);
	}
	@Override
	public void removerPuntos() {
		if(next.nearestPoint!=null)dataset.link(next.rowPosition,next.nearestPoint.rowPosition);
	}
	private Runnable[] actualizador=new Runnable[4];
	private Future<?>[] futureActualizador=new Future[actualizador.length];
	{for(int e=0;e<actualizador.length;e++)actualizador[e]=new MinCalculator(e);}
	@Override
	public void actualizar()throws Exception{
		if(size<1000){
			for(int e=0;e<size-1;e++){
				if(points[e].nearestPoint!=null&&dataset.sameSet(points[e].rowPosition, points[e].nearestPoint.rowPosition)){
					recalcFutures.add(Utils.EXEC.submit(new RecalcTask(e)));
				}
			}
		}else{
			for(int e=0;e<actualizador.length;e++)futureActualizador[e]=Utils.EXEC.submit(actualizador[e]);
			for(int e=0;e<actualizador.length;e++)if(futureActualizador[e].get()!=null){
				futureActualizador[e].get();
			}
		}
		Utils.join(recalcFutures);
	}
	 
	class MinCalculator implements Runnable{
		int start;
		public MinCalculator(int start) {
			this.start = start;
		}

		@Override
		public void run() {
			for(int e=start;e<size-1;e+=actualizador.length){
				if(points[e].nearestPoint!=null&&dataset.sameSet(points[e].rowPosition, points[e].nearestPoint.rowPosition)){
					recalcFutures.add(Utils.EXEC.submit(new RecalcTask(e)));
				}
			}
		}
		
	}
	class RecalcTask implements Callable<Object>{
		final int i;
		public RecalcTask(int i) {
			this.i = i;
		}
		@Override
		public Object call() throws Exception {
			Punto p=points[i];
			p.resetNearest();
			for(int e=0;e<i;e++)if(!dataset.sameSet(p.rowPosition,points[e].rowPosition)){
				p.setMin(points[e]);
			}
			return null;
		}
	}
}
