package com.demo.plsqlprogramanalysis.cfg;

import java.util.ArrayList;

public class Node {
    //This class is the type of each node in CFG
    //ArrayList<String> statements -> will contain list of statements that belongs to the statement type

    int nodeNumber;
    ArrayList<Integer> childList = new ArrayList<>();
    ArrayList<String> statements = new ArrayList<>();
    ArrayList<Node> parentNode = new ArrayList<>();
    String statementType;


    public ArrayList<Integer> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<Integer> childList) {
        this.childList = childList;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public ArrayList<String> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<String> statements) {
        this.statements = statements;
    }

    public ArrayList<Node> getParentNode() {
        return parentNode;
    }

    public void setParentNode(ArrayList<Node> parentNode) {
        this.parentNode = parentNode;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public boolean isChild(int childNumber) {

        for(int i = 0;i < childList.size(); i++)
            if(childList.get(i) == childNumber)
                return true;


        return false;
    }

    //Printing each statement within that block
    public void printStatement() {
        System.out.println(statements.size());
        for(int i = 0; i < statements.size(); i++)
            System.out.println(statements.get(i));
    }

    public void printChild() {
        System.out.print("Node number:  " + nodeNumber + "("+statementType+")   >>>   ");
        for(int i = 0; i < childList.size(); i++) {
            System.out.print(childList.get(i)+ "   ");
        }
        System.out.println();
    }
}
