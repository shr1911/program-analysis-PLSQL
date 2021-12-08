package com.demo.plsqlprogramanalysis.taintchecker;

import com.demo.plsqlprogramanalysis.cfg.Constants;
import com.demo.plsqlprogramanalysis.cfg.Node;
import com.demo.plsqlprogramanalysis.cfg.StatementChecker;

import java.util.*;

/*
 * This class to apply work-list algorithm on CFG to do taint analysis
 * */
public class TaintChecker {
    public static final String DBMS_ASSERT = "sys.DBMS_ASSERT";

    public ArrayList<Taint> parseGraph(HashMap<Integer, Node> cfgList) {
        System.out.println("Inside parseGraph... ");

        //To track final result to return the total number of taint present.
        ArrayList<Taint> resultTaints = new ArrayList<>();

        //For graph traversal purpose
        ArrayList<Integer> visitedNode = new ArrayList<Integer>();

        //contains list of tainted node and tainted variables in that node
        HashMap<Node, List<String>> taintedVariables = new HashMap<>();

        //To keep track of all parents of tainted node
        HashMap<Node, Node> listOfParentTaint = new HashMap<>();
        
        StatementChecker statementChecker = new StatementChecker();

        //Adding all inputs arguments to the list of taint
        Node functionDefinitionNode = cfgList.get(1);
        findTaintedArguments(functionDefinitionNode, taintedVariables);

        //just for validating
//        for(Map.Entry<Node, List<String>> entry : taintedVariables.entrySet()) {
//            Node key = entry.getKey();
//            List<String> value = entry.getValue();
//            System.out.println("Key is : "+key.getNodeNumber());
//            System.out.println("Value is : " + Arrays.toString(value.toArray()) .replaceAll("[\\[\\]]", ""));
//        }


        //visiting each node in adjacency list
        for(int i=1; i< cfgList.size(); i++){
            Node currentNode = cfgList.get(i);
            Integer nodeToVisit = null;

            ArrayList<Integer> currentNodeChild = currentNode.getChildList();
            //visiting each child for this node
            for(int j = 0; j < currentNodeChild.size(); j++) {
                System.out.println(currentNodeChild.get(j)+ "   ");
                nodeToVisit = currentNodeChild.get(j);
                if(!visitedNode.contains(nodeToVisit)){
                    //System.out.println("visiting node number " + nodeToVisit);

                    //for all statements in this node to be visited
                    for (String temp : cfgList.get(nodeToVisit).getStatements()) {


                        // if assignment statement and not sanitized add if any new taint introduced
                        if(statementChecker.checkStatementType(temp).equals(Constants.ASSIGNMENT_STATEMENT)) {
                            //System.out.println("Checking if assignment statement ?");
                            String[] result = temp.split(":=");
                            //System.out.println("RHS of assignment statement is : " + result[1]);

                            if(checkIfStringContainsTaint(result[1], taintedVariables)
                                    && !isSanitized(result[1])){

                                if(!taintedVariables.containsKey(nodeToVisit)){
                                    //System.out.println("Adding the new taint propogation");
                                    ArrayList<String> tempList = new ArrayList<>();
                                    tempList.add(result[0]);

                                    //start checking parent taints
                                    Node parentTaint = new Node();
                                    for(Map.Entry<Node, List<String>> entry : taintedVariables.entrySet()) {
                                        Node key = entry.getKey();
                                        List<String> values = entry.getValue();

                                        for (String value :values) {
                                            //System.out.println("value: " + value + ",  result[]: "+result[1]);

                                            if(result[1].contains(value)){
                                                parentTaint = key;
                                            }
                                        }
                                    }
                                    //System.out.println("its : " + cfgList.get(nodeToVisit).getStatements() + parentTaint.getStatements());
                                    listOfParentTaint.put(cfgList.get(nodeToVisit), parentTaint);
                                    //end checking parent taints

                                    taintedVariables.put(cfgList.get(nodeToVisit), tempList);


                                }
                            }
                        }

                        // if any taint is getting sinked due to execute immediate say add to hashmap of source and sink
                        if(statementChecker.checkStatementType(temp).equals(Constants.EXECUTE_IMMEDIATE_STATEMENT)){
                            //System.out.println("Checking if execute immediate statement ?");
                            for(Map.Entry<Node, List<String>> entry : taintedVariables.entrySet()) {
                                Node key = entry.getKey();
                                List<String> values = entry.getValue();

                                for (String value :values) {
                                    if(temp.contains(value.trim())){
                                        System.out.println("true: there is one taint");
                                        Taint taint = new Taint();

                                        Node source = key;

                                        //finding topmost parent, that will be my source
                                        while(listOfParentTaint.containsKey(source)){
                                            source = listOfParentTaint.get(source);
                                        }

                                        taint.setSourceMethod(source);
                                        //System.out.println("source: " + source.getStatements());
                                        taint.setSinkMethod(cfgList.get(nodeToVisit));
                                        //System.out.println("sink: " + cfgList.get(nodeToVisit).getStatements());
                                        resultTaints.add(taint);

                                    }
                                }
                            }
                        }
                    }


                    visitedNode.add(nodeToVisit);
                }
            }
            //System.out.println("-----");
        }

        return resultTaints;
    }

    /*
     * This function validates as we visit each statement if they contain taint or not
     * */
    private boolean checkIfStringContainsTaint(String s, HashMap<Node, List<String>> taintedVariables) {
        //System.out.println("checking checkIfStringContainsTaint for this assignment statement");
        boolean taintFlag = false;

        for(Map.Entry<Node, List<String>> entry : taintedVariables.entrySet()) {
            Node key = entry.getKey();
            List<String> value = entry.getValue();
            // System.out.println("Key is : "+key.getNodeNumber());
            //System.out.println("Value is : " + Arrays.toString(value.toArray()) .replaceAll("[\\[\\]]", ""));
            for (String temp :value) {
                if(s.contains(temp)){
                    System.out.println("true");
                    taintFlag = true;
                }
            }
        }
        return taintFlag;
    }

    /*
    * This function is to check the initial arguments which is considered tainted
    * */
    private void findTaintedArguments(Node functionDefinitionNode, HashMap<Node, List<String>> taintedVariables) {
        //System.out.println("Starting findTaintedArguments method... ");
        String createStatement = functionDefinitionNode.getStatements().get(0);
        String params = createStatement.substring(createStatement.indexOf('(')+1,createStatement.indexOf(')'));

        String previous = null;
        List<String > nodeTaintedVariables = new ArrayList<>();
        for (String item : params.split("\\s+")) {
            if(item.equals("IN")){
                nodeTaintedVariables.add(previous);
            }
            previous = item;

        };
        taintedVariables.put(functionDefinitionNode, nodeTaintedVariables);

    }


    /*
    * This function checked if statement is sanitized or not
    * */
    private boolean isSanitized(String statement) {
        StatementChecker statementChecker = new StatementChecker();
        //System.out.println("isSanitized:: " + statementChecker.isMatch("^.*DBMS_ASSERT.*$", statement));
        return statementChecker.isMatch("^.*DBMS_ASSERT.*$", statement);
    }
}
