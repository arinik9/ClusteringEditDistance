
firstClusteringString="7,6,1,1,7,3,7,2,2,5,4,8,3,9,1,9,5,1,3,6,9,7,8,4,5,1,3,6"
secondClusteringString="2,7,8,1,2,3,8,5,11,6,4,9,3,4,11,6,1,6,3,7,10,8,9,10,6,10,3,7"
ant -DisBatchMode=false -DfirstClusteringString="$firstClusteringString" -DsecondClusteringString="$secondClusteringString" -DisRelativeMembershipAsOutput=false run

firstClusteringString="7,6,1,1,7,3,7,2,2,5,4,8,3,9,1,9,5,1,3,6,9,7,8,4,5,1,3,6"
secondClusteringString="2,7,8,1,2,3,8,5,11,6,4,9,3,4,11,6,1,6,3,7,10,8,9,10,6,10,3,7"
ant -DisBatchMode=false -DfirstClusteringString="$firstClusteringString" -DsecondClusteringString="$secondClusteringString" -DisRelativeMembershipAsOutput=true run


inputDirPath="in"
outputDirPath="out"
ant -DisBatchMode=true -DinputDirPath="$inputDirPath" -DoutputDirPath="$outputDirPath" run
