# ClusteringEditDistance
Calculation of edit distance between membership vectors.

* Copyright 2020-21 Nejat Arınık

*ClusteringEditDistance* is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation. For source availability and license information see the file `LICENCE`

* GitHub repo: https://github.com/arinik9/ClusteringEditDistance
* Contact: Nejat Arınık <arinik9@gmail.com>


-----------------------------------------------------------------------

# Description
This program aims at calculating the edit distance between two or more membership vectors. It can be done in two different ways: pair vs. batch mode. In pair mode, we compare two membership vectors. In batch mode, we calculate the edit distance between multiple partitions. There is no output file for the former, just writing in console output. The latter is performed either by specifying a folder containing partitions or a file path indicating the relative paths of the solutions (relative to *inputDirPath*). In the end, a distance matrix file is created in the output directory.

Moreover, this program also allows through the option *isRelativeMembershipAsOutput* to transform the second membership vector in input into another membership vector, so that node clustering labels are much more in common with those of the first clustering. The reason of proposing this option is described as follows. The cost of an edit operation is not always minimal. This is because two membership vectors, i.e. the cluster labels of two partitions, can be very different, but essentially suggest very similar cluster assignments for the vertices. Before calculating the edit distance between two membership vectors, we need to select one of two membership vectors as a reference vector in order to adapt the module assignments of the other membership vector based on the reference one. Hence, the edit distance is calculated between the reference vector and this newly changed one, that we call relative vector.



### Input parameters

 * **isBatchMode:** Whether the calculation of edit distance is done in a batch mode or not. No default value, it must be specified.

 * **isRelativeMembershipAsOutput:** Whether we are interested in only obtaining the relative membership vector for second clustering parameter based on the first clustering. Used only in the pair mode.

 * **inputDirPath:** The path of an input directory containing partitions. We require the names of the membership vectors to have the prefix *membership*. Used only in the batch mode.

 * **solutionsFilePath:** The file path containing the relative paths of the solutions (relative to *inputDirPath*), for which the edit distance will be calculated. We require the names of the membership vectors to have the prefix *membership*. Used only in the batch mode.

 * **outputDirPath** The output directory, where the distance matrix file will be created. Used only in the batch mode.

 * **firstClusteringString:** The first membership vector in string format, where cluster ids are separated by a comma. Used only in the pair mode.

 * **secondClusteringString**  The second membership vector in string format,  where cluster ids are separated by a comma. Used only in the pair mode.

   


# Use
Check the scripts`run.sh` and `run2.sh` to see how to run *ClusteringEditDistance*.



# Output

In the pair mode, there is no output file for the former, just writing in console output. 

In the batch mode, a distance matrix file is created in the output directory.
