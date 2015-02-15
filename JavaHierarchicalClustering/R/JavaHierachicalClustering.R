.onLoad <- function(libname, pkgname) {
  .jpackage(pkgname, lib.loc = libname);
}
jhclust<-function(table,distance="sim",clusteringMethod="simple",threads=1){
  generator <- .jnew("datasets.DatasetGenerator");
  rowNames = rownames(table);
  .jcall(generator,"V","setColumnNames",colnames(table));
  for(i in 1:nrow(table)){
    .jcall(generator,"V","addPoint",rowNames[i],as.integer(table[i,]))
  }
  
  if(clusteringMethod=="simple"){
    resultado <- .jcall(generator,"Lclustering/results/Resultado;","directClustering",distance,as.integer(threads))
    return(procesarResultado(resultado,distance,clusteringMethod));
  }
  if(clusteringMethod=="single"){
    resultado <- .jcall(generator,"Lclustering/results/Resultado;","singleLinkAgglomerativeClustering",distance,as.integer(threads))
    return(procesarResultado(resultado,distance,clusteringMethod));
  }
}
jhclust.csv<-function(input,distance="sim",clusteringMethod="simple",threads=1){
  obj <- .jnew("main.Main");
  
  if(clusteringMethod=="simple"){
    resultado = .jcall(obj,"Lclustering/results/Resultado;","directClustering",input,distance,as.integer(threads));
    return(procesarResultado(resultado,distance,clusteringMethod));
  }
  if(clusteringMethod=="single"){
    resultado = .jcall(obj,"Lclustering/results/Resultado;","singleLinkAgglomerativeClustering",input,distance,as.integer(threads));
    return(procesarResultado(resultado,distance,clusteringMethod))
  }
}
procesarResultado <- function(resultado,distFunction,method){
  merge = .jcall(resultado,"Lclustering/results/ResultadoMerge;","getMerge");
  ids1 = .jcall(merge,"[I","getId1");
  ids2 = .jcall(merge,"[I","getId2");
  size = .jcall(resultado,"I","size")
  retorno<-list();
  retorno$height = .jcall(resultado,"[D","getDistances");
  retorno$labels = .jcall(resultado,"[S","getRowLabels")
  retorno$dist.method=distFunction;
  retorno$method=method;
  retorno$merge = matrix(nrow = size, ncol = 2)
  for(i in 1:size){
    retorno$merge[i,1]=ids1[i];
    retorno$merge[i,2]=ids2[i];
  }
  return(retorno);
}