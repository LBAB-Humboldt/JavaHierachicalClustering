package clustering;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Future;

import utils.Utils;
import clustering.results.ClusteringResult;
import clustering.results.Resultado;
import clustering.utils.Cluster;
import clustering.utils.Punto;
import datasets.Dataset;

public abstract class AbstractClustering<E>{
	
	protected E next,combinado;
	protected int totalSize,size;
	protected long times[]=new long[4],totalTime;
	protected List<Future<?>> recalcFutures=new Vector<Future<?>>();
	public abstract void inicializar(Punto[] dataset);
	public Resultado resultado;
	private DecimalFormat df=new DecimalFormat("0.00");
	public void process(int cores,Dataset dataset){
		Utils.init(cores);
		resultado=new Resultado(dataset);
		try{
			inicializar(dataset.puntos);
			final int unoPorCiento=totalSize-2<100?1:((totalSize-1)/100);
			for(int e=0,next=unoPorCiento;e<totalSize-1;e++){
				if(e==next){
					System.out.println(df.format((100.d*e)/(totalSize-1))+"% procesado");
					next+=unoPorCiento;
				}
				times[1]-=System.currentTimeMillis();
				ClusteringResult res=calcularMinimo();
				resultado.add(res);
				times[1]+=System.currentTimeMillis();
				times[2]-=System.currentTimeMillis();
				removerPuntos();
				times[2]+=System.currentTimeMillis();
				times[3]-=System.currentTimeMillis();
				actualizar();
				times[3]+=System.currentTimeMillis();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		totalTime+=System.currentTimeMillis();
		Utils.terminate();
	}
	public final Resultado getResultado() {
		return resultado;
	}
	public abstract ClusteringResult calcularMinimo()throws Exception;
	public abstract void removerPuntos();
	public abstract void actualizar()throws Exception;
	public String getName(){
		return this.getClass().getSimpleName();
	}
	public String getTimes(){
		String h="";
		for(int e=0;e<times.length;e++)h+=(h.isEmpty()?"":"/")+times[e];
		return h+":"+totalTime;
	}
	public long getTotalTime() {
		return totalTime;
	}
	public static Punto min(Punto p1,Punto p2){
		if(p1==null)return p2;
		if(p2==null)return p1;
		int comp=Double.compare(p1.distanceToNearestPoint,p2.distanceToNearestPoint);
		if(comp<0)return p1;
		if(comp==0)return p1.rowPosition<p2.rowPosition?p1:p2;
		return p2;
	}
	public static Cluster min(Cluster p1,Cluster p2){
		if(p1==null)return p2;
		if(p2==null)return p1;
		int comp=Double.compare(p1.distToNearestCluster,p2.distToNearestCluster);
		if(comp<0)return p1;
		if(comp>0)return p2;
		int cp1=Math.min(p1.canonicalId,p1.nearestCluster.canonicalId);
		int cp2=Math.min(p2.canonicalId,p2.nearestCluster.canonicalId);
		return cp1<cp2?p1:p2;
	}
}
