package clustering;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import utils.PointToExtendedConverter;
import utils.Utils;
import clustering.results.ClusteringResult;
import clustering.utils.SimilarityIndex;
import clustering.utils.Punto;
public class ArrayThread extends AbstractClustering<Punto>{
	private Punto[] points;
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
		next.deleted=true;
		if(next.nearestPoint!=null)next.nearestPoint.deleted=true;
		int i=0;
		for(int e=0;e<size;e++){
			if(points[e].deleted)i++;
			else if(i!=0)points[e-i]=points[e];
		}
		for(int e=0;e<i;e++)points[size-e-1]=null;
		size-=i;
	}
	private Callable<Punto>[] actualizador=new Callable[4];
	private Future<Punto>[] futureActualizador=new Future[actualizador.length];
	{for(int e=0;e<actualizador.length;e++)actualizador[e]=new MinCalculator(e);}
	@Override
	public void actualizar()throws Exception{
		//long t =System.currentTimeMillis();
		if(next.nearestPoint!=null)points[size++]=combinado=Utils.combine(next,next.nearestPoint);
		if(size<1000){
			for(int e=0;e<size-1;e++){
				if(points[e].nearestPoint!=null&&points[e].nearestPoint.deleted){
					recalcFutures.add(Utils.EXEC.submit(new RecalcTask(e)));
				}
				combinado.setMin(points[e]);
			}
		}else{
			for(int e=0;e<actualizador.length;e++)futureActualizador[e]=Utils.EXEC.submit(actualizador[e]);
			for(int e=0;e<actualizador.length;e++)if(futureActualizador[e].get()!=null){
				combinado.setMin(futureActualizador[e].get());
			}
		}
		//t=System.currentTimeMillis()-t;
		//if(t>3)System.out.println(size+"\t"+t);
		Utils.join(recalcFutures);
	}
	 
	class MinCalculator implements Callable<Punto>{
		int start;
		public MinCalculator(int start) {
			this.start = start;
		}

		@Override
		public Punto call() throws Exception {
			Punto nearestPoint=null;
			double distanceToNearestPoint=Double.POSITIVE_INFINITY;
			for(int e=start;e<size-1;e+=actualizador.length){
				if(points[e].nearestPoint!=null&&points[e].nearestPoint.deleted){
					recalcFutures.add(Utils.EXEC.submit(new RecalcTask(e)));
				}
				double dist=SimilarityIndex.instance.distance(combinado,points[e]);
				if(dist<distanceToNearestPoint){
					nearestPoint=points[e];
					distanceToNearestPoint=dist;
				}
			}
			return nearestPoint;
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
			for(int e=0;e<i;e++){
				p.setMin(points[e]);
			}
			
			return null;
		}
	}
}
