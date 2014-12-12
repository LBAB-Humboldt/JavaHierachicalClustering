package datasets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import clustering.utils.Punto;

public class Dataset {
	String[] columnNames;
	public Punto[] puntos;
	public Dataset(String[] columnNames, Punto[] puntos) {
		this.columnNames = columnNames;
		this.puntos = puntos;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public String[] getRowNames(){
		String[] rowNames = new String[puntos.length];
		for(int e=0;e<rowNames.length;e++)rowNames[e]=puntos[e].rowName;
		return rowNames;
	}
	public Punto[] getPuntos() {
		return puntos;
	}
	public static Dataset fromCsv(File csv){
		return fromCsv(csv,Integer.MAX_VALUE);
	}
	public static Dataset fromCsv(File csv,int limit){
		Dataset ret=null;
		List<Punto> puntos=new ArrayList<Punto>();
		try{
			BufferedReader br=new BufferedReader(new FileReader(csv));
			String columnames[]=br.readLine().split(",|;");
			DatasetUtils dsg=new DatasetUtils(columnames.length-1);
			int pos=1;
			for(String h,data[];limit-->0&&(h=br.readLine())!=null;){
				data=h.split(",|;");
				Punto nuevo=dsg.createPunto(pos,data);
				if(nuevo!=null){
					puntos.add(nuevo);
					pos++;
				}
			}
			br.close();
			ret=new Dataset(columnames, puntos.toArray(new Punto[0]));
		}catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
