# program-analysis-PLSQL-CMPUT-500
This git repository has been made for maintaining the project work for the course CMPUT 500 - Program Analysis. The code here contains the taint analysis study for the PL/SQL database codebase.

## Student Details
|Student name| CCID |
|------------|------|
|student 1   |  smakwana    |


## List of items in README
This file contains
- [ ] Summary of project
- [ ] Project Structure
- [ ] Execution Instructions to reproduce the result 
- [ ] Acknowledgement for all resources consulted (discussions, texts, urls, etc.) while working on the project. 


## Summary of Project

## Structure
- `src` 
    - `java`
        - `cfg`: This module contains control-flow-graph creation for PL/SQL stored procedure
        - `taintchecker`: This module coontains the core logic for taint analysis on the Intermediate Representation created    
    - `resource`: contains the various types of input example that has been given to the source code.
- `visualization`: This folder contains the control flow graph in .dot file format for visualization on Gephi tool.


## Execution Instructions to reproduce the result
    Following are the steps to run the project and produce results for any project.

### 1. Pre-requisite setup to run the code
       - The machine needs java 8 to be installed in the machine where the code is ran
       - IDE like Intellij or Eclipse
       - For dependency management Maven needs to be there (apache-maven-3.8.3 used)

### 2. Run the tainanalyzer for various input code
       
From the above code structure resource folder contains various types examples for PL/SQL stored procedure which has be to ran one after the other to reproduce the evaluations and results.
       
Following are the examples to be ran from resource folder:
   1. tainted-example.txt  -> Example for simple PL/SQL stored procedure for one tainted value 
   2. sanitized-example.txt -> Example for santitized input where no taints gets propogated
   3. tainted-high-loc-example.txt -> Example for tainted input with Stored Procedure for higher number of lines fo code
   4. two-taint-present-example.txt -> Example that detects 2 taints present for the given code
   5. multiple-taint-present-example.txt -> Example for more than 2 taints present for gven code
   6. dos-attack-without-sanitization.txt -> Example for producing taint which leads to denial of service (DoS) attack
   7. dos-attack-wit-sanitization.txt  -> Example that checks sanitization for denio of service (Dos) attack
        
In order to run each example follow below steps -
   1. Go to file `src` -> `main` -> `java` -> `com.demo.plsqlprogramanalysis.cfg` -> `InputCodeReader.java`
   2. On line number 18 for InputStream object, put the name of the example source code file
   3. Run PlsqlProgramAnalysisApplication.java file, which has the main method for spring boot application

### 3. Visualization setup to view the control flow graph

1. Install Gephi tool from https://gephi.org/users/install/
2. On line number 78 in PlsqlProgramAnalysisApplication.java, need to provide the filename for .dot file
3. `visualization` folder will contain the saved .dot file, open them into Gephi to view the  


## Acknowledgment and Resources



