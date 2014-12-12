package utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import clustering.utils.Punto;

public class Utils {
	public static ExecutorService EXEC;
	public static ForkJoinPool FORK_JOIN;
	public static void init(int threadPoolSize){
		EXEC = Executors.newFixedThreadPool(threadPoolSize);
		FORK_JOIN=new ForkJoinPool(threadPoolSize);
	}
	public static void terminate(){
		EXEC.shutdown();
		FORK_JOIN.shutdown();;
	}
	public static Punto combine(Punto p1,Punto p2){
		 long aparariciones[] = new long[Math.max(p1.apariciones.length,p2.apariciones.length)];
		 for(int e=0;e<p1.apariciones.length;e++)aparariciones[e]|=p1.apariciones[e];
		 for(int e=0;e<p2.apariciones.length;e++)aparariciones[e]|=p2.apariciones[e];
		 return new Punto(null,Math.min(p1.rowPosition,p2.rowPosition),aparariciones);
	}
	public static void join(List<Future<?>> futures)throws Exception{
		for(int e=0;e<futures.size();e++){
			futures.get(e).get();
		}
		futures.clear();
	}
}
