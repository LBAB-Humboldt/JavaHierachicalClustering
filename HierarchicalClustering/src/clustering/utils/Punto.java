package clustering.utils;

public class Punto{
	private static int ID_GENERATOR=0;
	public final int genID=ID_GENERATOR++;
	
	public String rowName;
	public int rowPosition;
	public long[] apariciones;
	public Punto(String rowId,int rowPosition,long[] aps){
		this.rowPosition=rowPosition;
		this.rowName=rowId;
		apariciones=aps;
	}
	
	public boolean deleted=false;
	public Punto nearestPoint=null;
	public double distanceToNearestPoint=Double.POSITIVE_INFINITY;
	
	/**
	 * Aquellos puntos que apuntan a este como su elemento mínimo
	 */
	//public LinkedList<ExtendedPoint> inversePointers=new LinkedList<ExtendedPoint>();
	public Punto(Punto p) {
		this(p.rowName,p.rowPosition,p.apariciones);
	}
	public boolean setMin(Punto candidato){
		double dist=SimilarityIndex.instance.distance(this,candidato);
		if(dist<distanceToNearestPoint){
			//if(nearestPoint!=null)nearestPoint.inversePointers.remove(this);
			nearestPoint=candidato;
			//nearestPoint.inversePointers.add(this);
			distanceToNearestPoint=dist;
			return true;
		}
		return false;
	}
	public Punto evalMin(Punto candidato){
		double dist=SimilarityIndex.instance.distance(this,candidato);
		return distanceToNearestPoint>dist?candidato:nearestPoint;
	}
	public void resetNearest(){
		nearestPoint=null;
		distanceToNearestPoint=Double.POSITIVE_INFINITY;
	}
	@Override
	public String toString() {
		String h="Punto "+rowPosition+" "+rowName+":";
		for(long l:apariciones)h+=new StringBuffer(pad(Long.toBinaryString(l),64)).reverse();
		return h;
	}
	String[] ceros=new String[64];
	{
		ceros[0]="";
		for(int e=1;e<ceros.length;e++)ceros[e]="0"+ceros[e-1];
	}
	public String pad(String h,int tam){
		if(h.length()<tam)return ceros[tam-h.length()]+h;
		return h;
	}
	
}
