package com.demo.plsqlprogramanalysis.cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static com.demo.plsqlprogramanalysis.cfg.Constants.*;

public class CFG {
    // Depending on the type of each statement construct graph core logic to construct the graph
    //Need to understand this
    // https://github.com/atiqahammed/Control-Flow-Graph/blob/master/src/statementAnalyzer/Analyser.java

    StatementChecker statementChecker = new StatementChecker();



    public HashMap<Integer, Node> constructCFG(ArrayList<String> lineList) {
        //System.out.println("Starting Control Flow Graph Construction....");

        int nodeNumber = 1;
        HashMap<Integer, Node> graph = new HashMap<>();
        Stack<Node> nodeStack = new Stack<>();
        Stack<Node> nodeOfBlockStack = new Stack<>();
        ArrayList<Node> ifElseLadderNodes = new ArrayList<>();




        int i=0;
        while(i < lineList.size()){
            //System.out.println("---------");

            //for first node i.e. Create or replace node
            if(statementChecker.checkStatementType(lineList.get(i)) == CREATE_OR_REPLACE_STATEMENT){
                ArrayList<String> createOrReplaceStatements = new ArrayList<>();
                createOrReplaceStatements.add(lineList.get(i));
                i++;
                while(BEGIN_STATEMENT != statementChecker.checkStatementType(lineList.get(i))){
                    createOrReplaceStatements.add(lineList.get(i));
                    i++;
                }
                //System.out.println("createOrReplaceStatements node will be..."+createOrReplaceStatements.toString());
                //System.out.println("size is "+ createOrReplaceStatements.size());
                //System.out.println("Here, create a node for createOrReplaceStatements...");

                Node createOrReplaceNode = new Node();
                createOrReplaceNode.nodeNumber = nodeNumber;
                createOrReplaceNode.statements = createOrReplaceStatements;
                createOrReplaceNode.parentNode = null;
                createOrReplaceNode.statementType = CREATE_OR_REPLACE_STATEMENT;
                graph.put(nodeNumber, createOrReplaceNode);
                nodeStack.push(createOrReplaceNode);
                nodeNumber++;
                //System.out.println("node stack is "+nodeStack.size());

            }

            //System.out.println("---------");
            //handling begin statement
            if(statementChecker.checkStatementType(lineList.get(i)) == BEGIN_STATEMENT){
                ArrayList<String> beginStatement = new ArrayList<>();
                ArrayList<Integer> childList = new ArrayList<>();
                beginStatement.add(lineList.get(i));
                System.out.println("beginStatement node will be..."+lineList.get(i));
                System.out.println("creating node for BEGIN_STATEMENT...");
                Node parentNode = nodeStack.pop();
                Node beginNode = new Node();
                beginNode.nodeNumber = nodeNumber;
                beginNode.statements = beginStatement;
                beginNode.getParentNode().add(parentNode);
                beginNode.statementType = BEGIN_STATEMENT;
                graph.put(nodeNumber, beginNode);
                //childList.add(nodeNumber);
                parentNode.getChildList().add(nodeNumber);
                nodeStack.push(beginNode);
                nodeNumber++;
                //System.out.println("node stack is "+nodeStack.size());

            }

            //handling assignment statement
            if(statementChecker.checkStatementType(lineList.get(i)) == ASSIGNMENT_STATEMENT){
                ArrayList<String> assignmentStatement = new ArrayList<>();
                assignmentStatement.add(lineList.get(i));
                System.out.println("assignmentStatement node will be..."+lineList.get(i));
                System.out.println("creating node for ASSIGNMENT_STATEMENT...");

                Node parentNode = nodeStack.peek();
                Node assignmentNode = new Node();
                assignmentNode.nodeNumber = nodeNumber;
                assignmentNode.statements = assignmentStatement;
                assignmentNode.getParentNode().add(parentNode);
                assignmentNode.statementType = ASSIGNMENT_STATEMENT;
                graph.put(nodeNumber, assignmentNode);
                parentNode.getChildList().add(nodeNumber);
                boolean nodeOfBlockStackFlag = false;
                for (Node node : nodeStack) {
                    System.out.println("Assignment statement' nodeStack" + node.statements);
                    if(node.statementType == IF_STATEMENT ||
                            node.statementType == ELSIF_STATEMENT ||
                            node.statementType == ELSE_STATEMENT){
                        System.out.println("nodeOfBlockStackFlag is True");
                        nodeOfBlockStackFlag = true;
                    }

                }

                if(nodeOfBlockStackFlag){
                    nodeOfBlockStack.push(assignmentNode);
                } else{
                    parentNode = nodeStack.pop();
                }
                nodeStack.push(assignmentNode);
                nodeNumber++;
                System.out.println("node stack is "+nodeStack.size());
            }

            //handling if statement
            if(statementChecker.checkStatementType(lineList.get(i)) == IF_STATEMENT){
                ArrayList<String> ifStatement = new ArrayList<>();
                ifStatement.add(lineList.get(i));
                System.out.println("ifStatement node will be..."+lineList.get(i));
                System.out.println("creating node for IF_STATEMENT...");

                Node parentNode = nodeStack.pop();
                Node ifNode = new Node();
                ifNode.nodeNumber = nodeNumber;
                ifNode.statements = ifStatement;
                ifNode.getParentNode().add(parentNode);
                ifNode.statementType = IF_STATEMENT;
                graph.put(nodeNumber, ifNode);
                parentNode.getChildList().add(nodeNumber);
                nodeStack.push(ifNode);
                nodeNumber++;
                System.out.println("node stack is "+nodeStack.size());
            }

            //handling else if statement
            if(statementChecker.checkStatementType(lineList.get(i)) == ELSIF_STATEMENT){
                ArrayList<String> elsifStatement = new ArrayList<>();
                elsifStatement.add(lineList.get(i));
                System.out.println("elsifStatement node will be..."+lineList.get(i));
                System.out.println("creating node for ELSIF_STATEMENT...");
                while(nodeStack.peek().statementType != IF_STATEMENT){
                    nodeStack.pop();
                }
                Node parentNode = nodeStack.pop();
                Node elsifNode = new Node();
                elsifNode.nodeNumber = nodeNumber;
                elsifNode.statements = elsifStatement;
                elsifNode.getParentNode().add(parentNode);
                elsifNode.statementType = ELSIF_STATEMENT;
                graph.put(nodeNumber, elsifNode);
                parentNode.getChildList().add(nodeNumber);
                nodeStack.push(elsifNode);
                nodeNumber++;
                ifElseLadderNodes.add(nodeOfBlockStack.pop());
                while(!nodeOfBlockStack.empty()){
                    nodeOfBlockStack.pop();
                }
                System.out.println("node stack is "+nodeStack.size());

            }

            //handling else statement
            if(statementChecker.checkStatementType(lineList.get(i)) == ELSE_STATEMENT){
                ArrayList<String> elseStatement = new ArrayList<>();
                elseStatement.add(lineList.get(i));
                System.out.println("elseStatement node will be..."+lineList.get(i));
                System.out.println("creating node for ELSE_STATEMENT...");
                System.out.println("node stack is "+nodeStack.size());
                while(nodeStack.peek().statementType != ELSIF_STATEMENT){
                    nodeStack.pop();
                }
                Node parentNode = nodeStack.pop();
                Node elseNode = new Node();
                elseNode.nodeNumber = nodeNumber;
                elseNode.statements = elseStatement;
                elseNode.getParentNode().add(parentNode);
                elseNode.statementType = ELSE_STATEMENT;
                graph.put(nodeNumber, elseNode);
                parentNode.getChildList().add(nodeNumber);
                nodeStack.push(elseNode);
                nodeNumber++;
                ifElseLadderNodes.add(nodeOfBlockStack.pop());
                while(!nodeOfBlockStack.empty()){
                    nodeOfBlockStack.pop();
                }
                System.out.println("node stack is "+nodeStack.toArray().toString());
            }

            //handling end if statement
            if(statementChecker.checkStatementType(lineList.get(i)) == END_IF_STATEMENT){
                ArrayList<String> endIfStatement = new ArrayList<>();
                endIfStatement.add(lineList.get(i));
                System.out.println("endIfStatement node will be..."+lineList.get(i));
                System.out.println("creating node for END_IF_STATEMENT...");
                System.out.println("node stack is "+nodeStack.size());

                Node parentNode = nodeStack.pop();
                Node endIfNode = new Node();
                endIfNode.nodeNumber = nodeNumber;
                endIfNode.statements = endIfStatement;
                endIfNode.getParentNode().add(parentNode);
                endIfNode.statementType = END_IF_STATEMENT;
                graph.put(nodeNumber, endIfNode);
                parentNode.getChildList().add(nodeNumber);
                //ifElseLadderNodes.add(nodeOfBlockStack.pop());
                while(!nodeOfBlockStack.empty()){
                    nodeOfBlockStack.pop();
                }
                for (Node node : ifElseLadderNodes) {
                    node.getChildList().add(nodeNumber);
                    endIfNode.getParentNode().add(node);
                }
                nodeStack.push(endIfNode);
                nodeNumber++;
            }

            //handling execute immediate statement
            if(statementChecker.checkStatementType(lineList.get(i)) == EXECUTE_IMMEDIATE_STATEMENT){
                ArrayList<String> executeImmediateStatement = new ArrayList<>();
                executeImmediateStatement.add(lineList.get(i));
                System.out.println("executeImmediateStatement node will be..."+lineList.get(i));
                System.out.println("creating node for EXECUTE_IMMEDIATE_STATEMENT...");

                Node parentNode = nodeStack.pop();
                Node executeImmediateNode = new Node();
                executeImmediateNode.nodeNumber = nodeNumber;
                executeImmediateNode.statements = executeImmediateStatement;
                executeImmediateNode.getParentNode().add(parentNode);
                executeImmediateNode.statementType = EXECUTE_IMMEDIATE_STATEMENT;
                graph.put(nodeNumber, executeImmediateNode);
                parentNode.getChildList().add(nodeNumber);
                nodeStack.push(executeImmediateNode);
                nodeNumber++;
                System.out.println("node stack is "+nodeStack.size());
            }

            //End of the stored proc statement
            if(statementChecker.checkStatementType(lineList.get(i)) == END_STATEMENT){
                ArrayList<String> endStatement = new ArrayList<>();
                endStatement.add(lineList.get(i));
                System.out.println("endStatement node will be..."+lineList.get(i));
                System.out.println("creating node for END_STATEMENT...");
                Node parentNode = nodeStack.pop();
                Node endNode = new Node();
                endNode.nodeNumber = nodeNumber;
                endNode.statements = endStatement;
                endNode.getParentNode().add(parentNode);
                endNode.statementType = END_STATEMENT;
                graph.put(nodeNumber, endNode);
                parentNode.getChildList().add(nodeNumber);
                nodeStack.push(endNode);
                nodeNumber++;
                System.out.println("node stack is "+nodeStack.size());
            }

            i++;
        }

        return graph;
    }




}
