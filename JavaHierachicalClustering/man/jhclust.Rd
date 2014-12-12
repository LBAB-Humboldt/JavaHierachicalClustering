\name{jhclust}
\alias{jhclust}
\alias{jhclust}
\docType{function}
\title{
jhclust
}
\description{
This function runs a hierachical clustering algorithm implemented in Java over a input table representing biodiversity data.
}
\usage{
jhclust(table,distance,clusteringMethod,threads)
jhclust(table)
}
\arguments{
  \tabular{ll}{
    input: \tab The input table. It should be numeric table. Rows represents areas, columns represents species. On row i column j should apper an 1 if sp j appears on area i and o otherwise.\cr
    distance: \tab The index of beta diversity as defined in Koleff et al. (2003), Table 1. You can use either the subscript of β or the number of the index.\cr
    clusteringMethod: \tab The agglomeration method to be used. This should be "single", "complete", "average" or "simple" (no agglomerative).\cr
    threads: \tab The number of threads to be used on parallel processing. Default is 1.
  }
}
\value{
An object which describes the tree produced by the clustering process. It has the same format of hclust output. The object is a list with components:
\tabular{ll}{
  merge: \tab an n-1 by 2 matrix. Row i of merge describes the merging of clusters at step i of the clustering. If an element j in the row is negative, then observation -j was merged at this stage. If j is positive then the merge was with the cluster formed at the (earlier) stage j of the algorithm. Thus negative entries in merge indicate agglomerations of singletons, and positive entries indicate agglomerations of non-singletons.\cr
  height: \tab a set of n-1 real values (non-decreasing for ultrametric trees). The clustering height: that is, the value of the criterion associated with the clustering method for the particular agglomeration.\cr
  order: \tab a vector giving the permutation of the original observations suitable for plotting, in the sense that a cluster plot using this ordering and matrix merge will not have crossings of the branches.\cr
  labels: \tab labels for each of the objects being clustered.\cr
  method: \tab the cluster method that has been used.\cr
  dist.method: \tab the distance that has been used to measure distance between areas.
}

}
\keyword{ package }
\seealso{
~~ Optional links to other man pages, e.g. ~~
~~ \code{\link[<pkg>:<pkg>-package]{<pkg>}} ~~
}
\examples{
#Load a package with data to test 
library("vegan")
#Load sipoo dataset
data(sipoo)
#Apply simple hierachical clustering over sipoo using simpsons metric
jhclust(sipoo)
}
