
import java.util.*; 

public class MyCGraph {
	public int n;
    public Clustering c;
    public double[][] weightSumInClusters;

    
	public MyCGraph(int n_, Clustering c_){
		n = n_;
		c = c_;
		weightSumInClusters = new double[c.n][c.getNbCluster()+1];
		for(int i=0; i<c.n; i++){
    		for(int j=1; j<=(c.getNbCluster()+1); j++){ // +1 for a new cluster
    			weightSumInClusters[i][j-1] = 0.0;
    		}
		}

	}

    
    public void fillInNodeToWeightedDegreeSum(double[][] d){
    	//ArrayList<ArrayList<Integer>> clusters = c.getClustersInArrayFormat();
    	for(int i=0; i<n; i++){
    		for(int j=0; j<n ; j++){
    			if(i!=j){
	    			int clu_j = c.membership[j];
	    			weightSumInClusters[i][clu_j-1] += d[i][j];
    			}
    		}
    	}
    }
	    
    
    // A utility function to print nodeToWeightedDegreeSumInClusters
    public void printNodeToWeightedDegreeSumInClusters() 
    { 
    	for(int i=0; i<n; i++){
    		System.out.println("\nnode id:" + i + " -> "); 
    		System.out.print("\t"); 
    		for(int clusterId=1; clusterId<=c.nbCluster; clusterId++){
    			System.out.print("c:" + clusterId + ", sum:" + weightSumInClusters[i][clusterId-1]+" -- "); 
    		}
    	}
    	System.out.print("\n"); 
        
    } 
	  
} 