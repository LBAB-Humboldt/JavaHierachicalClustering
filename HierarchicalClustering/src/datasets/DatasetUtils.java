package datasets;

import java.util.BitSet;

import clustering.utils.Punto;

public class DatasetUtils {
	private int celdas;
	private BitSet bs;
	private static long[][] buffer;
	private int bufferPointer=0;
	public DatasetUtils(int tam){
		bs=new BitSet(tam);
		celdas=(tam-1)/64+1;
		buffer=new long[1024][celdas];
	}
	public final Punto createPunto(int pos,String[] data){
		bs.clear();
		for(int e=1;e<data.length;e++)if(data[e].equals("1"))bs.set(e-1);
		if(bs.length()==0)return null;
		final long[] apariciones=getNext();
		long[] temp = bs.toLongArray();
		System.arraycopy(temp,0, apariciones, 0, temp.length);
		return new Punto(data[0],pos,apariciones);
	}
	private long[] getNext(){
		if(bufferPointer==buffer.length){
			buffer=new long[1024][celdas];
			bufferPointer=0;
		}
		return buffer[bufferPointer++];
	}
}
