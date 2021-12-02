package com.demo.plsqlprogramanalysis;

import com.demo.plsqlprogramanalysis.cfg.CFG;
import com.demo.plsqlprogramanalysis.cfg.InputCodeReader;
import com.demo.plsqlprogramanalysis.cfg.Node;
import com.demo.plsqlprogramanalysis.taintchecker.TaintChecker;
import com.demo.plsqlprogramanalysis.taintchecker.Taint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class PlsqlProgramAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlsqlProgramAnalysisApplication.class, args);

		System.out.println("\n\nString Control Flow Graph Creation of PL/SQL block... ");

		long startCFGTime = System.currentTimeMillis();
		//Reading input file to be analyzed
		InputCodeReader inputCodeReader = new InputCodeReader();
		ArrayList<String> lineList = inputCodeReader.inputFileRead();

		//Creating CFG of the given code
		CFG cfg = new CFG();
		HashMap<Integer, Node> cfgList = cfg.constructCFG(lineList);
		long endCFGTime = System.currentTimeMillis();
		long timeElapsedCFG = endCFGTime - startCFGTime;
		System.out.println("\nTotal time taken for CFG creation is (in MilliSeconds): " + timeElapsedCFG);

		System.out.println("\n++++++++++++++++++++++++Printing Graph START++++++++++++++++++++++++\n");
		System.out.println("Displaying Control Flow Graph... ");
		for(Map.Entry<Integer, Node> entry : cfgList.entrySet()) {
			int key = entry.getKey().intValue();
			Node value = entry.getValue();
			value.printChild();
			System.out.println("-----");
		}

		System.out.println("++++++++++++++++++++++++Printing Graph END++++++++++++++++++++++++\n");

		System.out.println("Starting to find taints using CFG....");
		long startTaintTime = System.currentTimeMillis();
		TaintChecker cfgParser = new TaintChecker();
		ArrayList<Taint> taints = cfgParser.parseGraph(cfgList);
		long endTaintTime = System.currentTimeMillis();
		long timeElapsedTaint = endTaintTime - startTaintTime;
		System.out.println("\nTotal time taken for Taint Detection (in MilliSeconds): " + timeElapsedTaint);


		System.out.println("\n++++++++++++++++++++++++START Printing Total Taints++++++++++++++++++++++++\n");

		for (Taint taint:taints) {
			System.out.println("SOURCE : " + taint.getSourceMethod().getStatements());
			System.out.println("SINK : " + taint.getSinkMethod().getStatements());
		}

		System.out.println("\n++++++++++++++++++++++++END Printing Total Taints++++++++++++++++++++++++\n\n\n");

		System.out.println("++++++++++++++++++++++++TIME COMPLEXITY - START ++++++++++++++++++++++++\n");
		System.out.println("Total time taken for CFG creation is (in MilliSeconds): " + timeElapsedCFG);
		System.out.println("Total time taken for Taint Detection (in MilliSeconds): " + timeElapsedTaint);
		System.out.println("\nOverall time taken (in MilliSeconds): " + (timeElapsedCFG + timeElapsedTaint));



		System.out.println("\n++++++++++++++++++++++++TIME COMPLEXITY - END ++++++++++++++++++++++++\n\n\n");


		//Performing Visualization
		System.out.println("Start creating visualization for java CFG... ");
		try {
			FileWriter myWriter = new FileWriter("example2.dot");
			myWriter.write("digraph G {\n");
			for(Map.Entry<Integer, Node> entry : cfgList.entrySet()) {
				int nodeNumber = entry.getKey().intValue();
				Node node = entry.getValue();
				myWriter.write("\t\"" + nodeNumber + ":" + node.getStatements() + "\";\n");
			}
			for(Map.Entry<Integer, Node> entry : cfgList.entrySet()) {
				int nodeNumber = entry.getKey().intValue();
				Node node = entry.getValue();
				for (Integer childNodeNumber :node.getChildList()) {
					myWriter.write("\t\"" + nodeNumber + ":" + node.getStatements() + "\" -> \""
							+ childNodeNumber  + ":" + cfgList.get(childNodeNumber).getStatements() + "\";\n");
				}

			}
			myWriter.write("}");
			myWriter.close();
			System.out.println("Successfully created .dot file for CFG visualization!");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}


	}

}
