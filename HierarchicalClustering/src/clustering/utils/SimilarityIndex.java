package clustering.utils;

public enum SimilarityIndex {
	WHITTAKER("W"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (2.0*(a+b+c))/(2.0*a+b+c);
		}
	},HARRISON("-1"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (2.0*(a+b+c))/(2.0*a+b+c)-1;
		}
	},CODY("c"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (b+c)/2.0;
		}
	},WEIHER_BOYLEN("wb"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return b+c;
		}
	},ROUTLEDGE("r"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			double cua=(a+b+c);
			cua*=cua;
			return cua/(cua-2*b*c);
		}
	},WILSON("t"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (b+c)/(2.0*a+b+c);
		}
	},MOURELLE("me"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (b+c)/(2.0*a+b+c);
		}
	},JACCARD("j"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (double)a/(a+b+c);
		}
	},SORENSEN("sor"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (2.0*a)/(2.0*a+b+c);
		}
	},HARRISON_2("-2"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (double)Math.min(b, c)/(Math.max(b, c)+a);
		}
	},CODY_2("co"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return 1-(a*(2.0*a+b+c))/(2.0*(a+b)*(a+c));
		}
	},COLWELL("cc"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (double)(b+c)/(a+b+c);
		}
	},WILLIAMS("-3"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return (double)Math.min(b,c)/(a+b+c);
		}
	},SIMPSONS("sim"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			double min=Math.min(b,c);
			return min/(a+min);
		}
	},LENNON("gl"){
		@Override
		protected double applyMetric(int a, int b, int c) {
			return 2.0*Math.abs(b-c)/(2.0*a+b+c);
		}
	};
	private String subscript;
	private SimilarityIndex(String subscript) {
		this.subscript=subscript;
	}
	public static SimilarityIndex instance=SIMPSONS; 
	public final double distance(Punto p1, Punto p2) {
		int a=0,b=0,c=0;
		for(int e=0;e<p1.apariciones.length;e++){
			a+=Long.bitCount(p1.apariciones[e]&p2.apariciones[e]);
			b+=Long.bitCount(p1.apariciones[e]&(~p2.apariciones[e]));
			c+=Long.bitCount((~p1.apariciones[e])&p2.apariciones[e]);
		}
		return applyMetric(a, b, c);
	}
	protected abstract double applyMetric(int a,int b,int c);
	public static void setInstance(String name){
		name=name.toLowerCase();
		for(SimilarityIndex si:values())if(si.subscript.equals(name)){
			instance=si;
			return;
		}
		instance = JACCARD;
	}
	public static boolean isValid(String name){
		name=name.toLowerCase();
		for(SimilarityIndex si:values()){
			if(si.subscript.equals(name))return true;
		}
		return false;
	}
}
