package utils;

import clustering.utils.Punto;

public class PrecalculatedDataset {
	public Punto[] puntos;
	public int[] minimos;
	public PrecalculatedDataset(Punto[] puntos) {
		this.puntos = puntos;
		minimos=new int[puntos.length];
	}
	
}
