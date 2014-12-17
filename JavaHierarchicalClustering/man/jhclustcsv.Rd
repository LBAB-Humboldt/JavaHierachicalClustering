\name{jhclust.csv}
\alias{jhclust.csv}
\alias{jhclust.csv}
\docType{function}
\title{
jhclust.csv
}
\description{
This function runs a hierachical clustering algorithm implemented in Java over a input table representing biodiversity data.
}
\usage{
jhclust.csv(input,distance,clusteringMethod,threads)
jhclust.csv(input)
}
\arguments{
\tabular{ll}{
 input: \tab The input file. It should be a csv table. The first column are the cell ids. Next columns represent species. Each species can be present (1) or absent (0) on each cell. Each csv cell should NOT have quotes.\cr
 distance: \tab The index of beta diversity as defined in Koleff et al. (2003), Table 1. You can use either the subscript of β or the number of the index.\cr
 clusteringMethod: \tab The agglomeration method to be used. This should be "single", "complete", "average" or "simple" (no agglomerative).\cr
 threads: \tab The number of threads to be used on parallel processing. Default is 1.
 }
}
\value{
An object which describes the tree produced by the clustering process. It has the same format of hclust output. The object is a list with components:
\tabular{ll}{
  merge: \tab An n-1 by 2 matrix. Row i of merge describes the merging of clusters at step i of the clustering. If an element j in the row is negative, then observation -j was merged at this stage. If j is positive then the merge was with the cluster formed at the (earlier) stage j of the algorithm. Thus negative entries in merge indicate agglomerations of singletons, and positive entries indicate agglomerations of non-singletons.\cr
  height: \tab A set of n-1 real values (non-decreasing for ultrametric trees). The clustering height: that is, the value of the criterion associated with the clustering method for the particular agglomeration.\cr
  order: \tab A vector giving the permutation of the original observations suitable for plotting, in the sense that a cluster plot using this ordering and matrix merge will not have crossings of the branches.\cr
  labels: \tab Labels for each of the objects being clustered.\cr
  method: \tab The cluster method that has been used.\cr
  dist.method: \tab The distance that has been used to measure distance between areas.
}

}
\keyword{ package }
\examples{
#Load a package with data to test 
library("vegan")
#Load sipoo dataset
data(sipoo)
write.csv("sipoo.csv")
#Apply simple hierachical clustering over sipoo using simpsons metric
jhclust("sipoo.csv")
}
