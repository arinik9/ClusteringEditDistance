
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
* This program aims at calculating the edit distance between two or more membership vectors. 
* 	It can be done in two different ways: pair vs. batch mode.
* In pair mode, we compare two membership vectors.
* In batch mode, we calculate the edit distance between multiple partitions.
* 	There is no output file for the former, just writing in console output.
* 	The latter is performed either by specifying a folder containing partitions
* 		 or a file path indicating the relative paths of the solutions (relative to 'inputDirPath').
* 		In the end, a distance matrix file is created in the output directory.
* 
* Moreover, this program also allows through the option 'isRelativeMembershipAsOutput' to transform 
* 	the second membership vector in input into another membership vector,
* 	so that node clustering labels are much more in common with those of the first clustering. 
* 	The reason of proposing this option is described below:
* 			The cost of an edit operation is not always minimal. This is because two membership vectors, 
* 			i.e. the cluster labels of two partitions, can be very different, but essentially suggest very similar
*  			cluster assignments for the vertices. Before calculating the edit distance between two membership vectors, 
*  			we need to select one of two membership vectors as a reference vector in order to adapt the module assignments 
*  			of the other membership vector based on the reference one. Hence, the edit distance is 
*  			calculated between the reference vector and this newly changed one, that we call relative vector.
*  
* 
* Example for the pair mode:
* 	firstClusteringString="7,6,1,1,7,3,7,2,2,5,4,8,3,9,1,9,5,1,3,6,9,7,8,4,5,1,3,6"
*	secondClusteringString="2,7,8,1,2,3,8,5,11,6,4,9,3,4,11,6,1,6,3,7,10,8,9,10,6,10,3,7"
* 	ant -DisBatchMode=false -DfirstClusteringString="$firstClusteringString" 
* 		-DsecondClusteringString="$secondClusteringString" -DisRelativeMembershipAsOutput=false run
* 
* Example 2 for the pair mode:
* 	firstClusteringString="7,6,1,1,7,3,7,2,2,5,4,8,3,9,1,9,5,1,3,6,9,7,8,4,5,1,3,6"
*	secondClusteringString="2,7,8,1,2,3,8,5,11,6,4,9,3,4,11,6,1,6,3,7,10,8,9,10,6,10,3,7"
* 	ant -DisBatchMode=false -DfirstClusteringString="$firstClusteringString" 
* 		-DsecondClusteringString="$secondClusteringString" -DisRelativeMembershipAsOutput=true run
* 
* 
* Example for the batch mode:
* inputDirPath="in"
* outputDirPath="out"
* ant -DisBatchMode=true -DsolutionsFilePath="in/allResults.txt" -DoutputDirPath="$outputDirPath" run
* 
* Example 2 for the batch mode:
* inputDirPath="in"
* outputDirPath="out"
* ant -DisBatchMode=true -DinputDirPath="$inputDirPath" -DoutputDirPath="$outputDirPath" run
* <p>
* 
*
*/
public class Main {

    
	static String tempFile = "temp.txt";
	
	/**
	 * 
	 * Input parameters:
	 * <ul>
	 * <li> isBatchMode (boolean): Whether the calculation of edit distance is done in a batch mode or not. 
	 * 		No default value, it must be specified. </li>
	 * <li> isRelativeMembershipAsOutput (boolean): Whether we are interested in only obtaining
	 * 												 the relative membership vector for second clustering parameter 
	 * 												based on the first clustering. Used only in the pair mode. </li>
	 * <li> inputDirPath (String): The path of an input directory containing partitions. 
	 * 								We require the names of the membership vectors to have the prefix 'membership'.
	 * 								Used only in the batch mode. </li>
	 * <li> solutionsFilePath (String): The file path containing the relative paths of the solutions (relative to 'inputDirPath'), 
	 * 									for which the edit distance will be calculated.
	 * 									We require the names of the membership vectors to have the prefix 'membership'.
	 * 									Used only in the batch mode. </li>
	 * <li> outputDirPath (String): The output directory, where the distance matrix file will be created.
	 * 								Used only in the batch mode. </li>
	 * <li> firstClusteringString (String): The first membership vector in string format, 
	 * 											where cluster ids are separated by a comma.
	 * 										Used only in the pair mode. </li>
	 * <li> secondClusteringString (String): The second membership vector in string format,  
	 * 											where cluster ids are separated by a comma.
	 * 										 Used only in the pair mode. </li>
	 * </ul>
	 * 
	 * 
	 * @param args  (Not used in this program. Instead, user parameters are obtained
	 * 	 through ant properties. See the buil.xml for more details).
	 * 
	 * @throws FileNotFoundException.
	 */
	public static void main(String[] args) throws FileNotFoundException {
//		String inputDirPath = "";
		String firstClusteringString = "";
		String secondClusteringString = "";
		String inputDirPath = ".";
		String outputDirPath = ".";
		String solutionsFilePath = "";

		boolean isBatchMode = true;
		boolean isRelativeMembershipAsOutput = false; // this parameter is used only when isBatchMode = false
		
		if(!System.getProperty("isBatchMode").equals("${isBatchMode}") ){
			isBatchMode = Boolean.valueOf(System.getProperty("isBatchMode"));
		} else {
			System.out.println("isBatchMode is not specified, after firstClusteringString. Exit");
			return;
		}
		
		
		if(!isBatchMode){
			if(!System.getProperty("firstClusteringString").equals("${firstClusteringString}") ){
				firstClusteringString = System.getProperty("firstClusteringString");
			}
			else {
				System.out.println("firstClusteringString is not specified. Exit");
				return;
			}
			if(!System.getProperty("secondClusteringString").equals("${secondClusteringString}") ){
				secondClusteringString = System.getProperty("secondClusteringString");
			}
			else {
				System.out.println("secondClusteringString is not specified. Exit");
				return;
			}
			
			if(!System.getProperty("isRelativeMembershipAsOutput").equals("${isRelativeMembershipAsOutput}") ){
				isRelativeMembershipAsOutput = Boolean.valueOf(System.getProperty("isRelativeMembershipAsOutput"));
			}
		}
	
		
		if(isBatchMode){
			if( !System.getProperty("inputDirPath").contains("${inputDirPath}") && !System.getProperty("inputDirPath").equals("")){
				inputDirPath = System.getProperty("inputDirPath");
			}
			else {
				if( !System.getProperty("solutionsFilePath").contains("${solutionsFilePath}") && !System.getProperty("solutionsFilePath").equals("") ){
					solutionsFilePath = System.getProperty("solutionsFilePath");
				}
				else {
					System.out.println("inputDirPath or solutionsFilePath is not specified. Exit");
					return;
				}
			}
			if(!System.getProperty("outputDirPath").equals("${outputDirPath}") ){
				outputDirPath = System.getProperty("outputDirPath");
			} else {
				System.out.println("outputDirPath is not specified. Exit");
				return;
			}
		}
		
		
		

		
//		System.out.println("===============================================");
//		System.out.println("isBatchMode: " + isBatchMode);
//		System.out.println("firstClusteringString: " + firstClusteringString);
//		System.out.println("secondClusteringString: " + secondClusteringString);
//		System.out.println("inputDirPath: " + inputDirPath);
//		System.out.println("solutionsFilePath: " + solutionsFilePath);
//		System.out.println("outputDirPath: " + outputDirPath);
//		System.out.println("===============================================");
		
		
		if(isBatchMode){
			List<String> pathnames2;
			int n=-1;

			if(!solutionsFilePath.equals("")){
				pathnames2 = new ArrayList<>();
				
				try{
					InputStream  ips = new FileInputStream(solutionsFilePath);
					InputStreamReader ipsr=new InputStreamReader(ips);
					BufferedReader br = new BufferedReader(ipsr);
					String line;
					  
					while ((line = br.readLine()) != null) {
						pathnames2.add(line);
					}
					
					br.close();
					
					try {
						n = getNbLinesInFile(inputDirPath+"/"+pathnames2.get(0));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}catch(Exception e){
				  System.out.println(e.toString());
				}
			} else {
			
				// This filter will only include files ending with .txt
				FilenameFilter filter = new FilenameFilter() {
				        @Override
				        public boolean accept(File f, String name) {
				            return name.startsWith("membership");
				        }
				    };
				    
				File f = new File(inputDirPath);
				String[] pathnames = f.list(filter);
				pathnames2 = Arrays.asList(pathnames);
				
				try {
					n = getNbLinesInFile(inputDirPath+"/"+pathnames[0]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			pathnames2.sort(NaturalOrderComparators.createNaturalOrderRegexComparator());
			int m = pathnames2.size();

			// ====================

			
			ArrayList<Clustering> clusterings = new ArrayList<Clustering>();
			for(String path : pathnames2){

				int[] membership = readMembership(inputDirPath+"/"+path, n);
				Clustering c = new Clustering(membership, -1);
				clusterings.add(c);
			}
			
			int[][] mtrx = new int[m][m];
			for(int i=0; i<m; i++){
				mtrx[i][i] = 0;
				//System.out.println(i);
				for(int j=0; j<i; j++){
					EditDistance eDist = new EditDistance();
					int nbEdit = eDist.calculateEditDistance(clusterings.get(i), clusterings.get(j));
					mtrx[i][j] = nbEdit;
					mtrx[j][i] = nbEdit;
				}
			}
			
			String content = ",";
			for(int i=0; i<(m-1); i++){
				content += "sol"+i+",";
			}
			content += "sol"+(m-1)+"\n";
			
			for(int i=0; i<m; i++){
				content += "sol"+i+",";
				
				for(int j=0; j<m; j++){
					int nbEdit = mtrx[i][j];
					
					if(j==0)
						content += nbEdit;
					else
						content += ","+nbEdit;
				}
				content += "\n";
			}
			
			
			try{
				 BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirPath+"/dist-matrix-Edit.csv"));
				 writer.write(content);
				 writer.close();
			 } catch(IOException ioe){
			     System.out.print("Erreur in writing output file: ");
			     ioe.printStackTrace();
			 }
			
			
		} else { // if(!isBatchMode)
			int[] membership1 = readStringMembership(firstClusteringString);
			Clustering c1 = new Clustering(membership1, -1);
			
			int[] membership2 = readStringMembership(secondClusteringString);
			Clustering c2 = new Clustering(membership2, -1);
			
			EditDistance eDist = new EditDistance();
			
			if(isRelativeMembershipAsOutput){
				int[] relMem = eDist.obtainRelativeMembership(c1, c2);
				String content = relMem[0]+"";
				for(int i=1; i<relMem.length; i++){
					content += "," + relMem[i];
				}
				System.out.print(content);
				
			} else {
				int nbEdit = eDist.calculateEditDistance(c1, c2);
				System.out.print(nbEdit);
			}
			
			
		}
	
		
	}
	
	
	

	/**
	 * read a solution from file
	 * 
	 */
	public static int[] readStringMembership(String clusteringString){
		int[] membership;
		
		String[] labels = clusteringString.split(","); 
		
		int n = labels.length;
		membership = new int[n];
		  
		for(int i=0; i<n; i++){ // for each node
			membership[i] = Integer.parseInt(labels[i]);	
		}
		
		return(membership);
	}
	
	
	/**
	 * read a solution from file
	 * 
	 */
	public static int[] readMembership(String filepath, int n){
		int[] membership = new int[n];
		
		try{
			InputStream  ips = new FileInputStream(filepath);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			  
			for(int i=0; i<n; i++){ // for each node
				line = br.readLine();
				membership[i] = Integer.parseInt(line);	
			}
			
			line = br.readLine();
			br.close();
			
			// verify that the file we just read corresponds to a correct nb node
			if(line != null){
				return(null);
			}
		
		}catch(Exception e){
		  System.out.println(e.toString());
		  return(null);
		}
		
		return(membership);
	}
	
	
	
	public static int getNbLinesInFile(String filepath) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		return(lines);
	}
	
	
	
	

}
