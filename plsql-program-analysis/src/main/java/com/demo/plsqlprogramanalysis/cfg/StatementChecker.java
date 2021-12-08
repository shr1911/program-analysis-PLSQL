package com.demo.plsqlprogramanalysis.cfg;

import java.util.regex.Pattern;

import static com.demo.plsqlprogramanalysis.cfg.Constants.*;

/*
* This class contains statement check of each PL/SQ statement by using regular expression pattern matching (using already done work)
* */
public class StatementChecker {

    private Pattern MY_PATTERN;


    public String checkStatementType(String statement) {
        if(isCreateOrReplaceStatement(statement)){
            return CREATE_OR_REPLACE_STATEMENT;
        } if(isBeginStatement(statement)){
            return BEGIN_STATEMENT;
        } if(isAssignmentStatement(statement)){
            return ASSIGNMENT_STATEMENT;
        } if(isIfStatement(statement)){
            return IF_STATEMENT;
        } if(isElsifStatement(statement)){
            return ELSIF_STATEMENT;
        } if(isElseStatement(statement)){
            return ELSE_STATEMENT;
        } if(isEndIfStatement(statement)){
            return END_IF_STATEMENT;
        } if(isExecuteImmediateStatement(statement)){
            return EXECUTE_IMMEDIATE_STATEMENT;
        } if(isEndStatement(statement)){
            return END_STATEMENT;
        }
        return NOT_MATCHED;
    }

    private boolean isEndStatement(String statement) {
        return isMatch("^(\\s)*END;$", statement);
    }

    private boolean isExecuteImmediateStatement(String statement) {
        return isMatch("^(\\s)*EXECUTE\\sIMMEDIATE", statement);
    }

    private boolean isEndIfStatement(String statement) {
        return isMatch("^(\\s)*END IF", statement);
    }

    private boolean isElseStatement(String statement) {
        return isMatch("^(\\s)*ELSE", statement);
    }

    private boolean isElsifStatement(String statement) {
        return isMatch("^(\\s)*ELSIF", statement);
    }

    private boolean isIfStatement(String statement) {
        return isMatch("^(\\s)*IF", statement);
    }

    private boolean isAssignmentStatement(String statement) {
        return isMatch("^([a-zA-z]|\\s|[0-9])*:=", statement);
    }

    private boolean isBeginStatement(String statement) {
        return isMatch("^BEGIN", statement);
    }
    private boolean isCreateOrReplaceStatement(String statement) {
        return isMatch("^(\\s)*CREATE", statement);
    }



    public boolean isMatch(String pattern,  String testString) {
        MY_PATTERN = Pattern.compile(pattern);
        java.util.regex.Matcher matcher = MY_PATTERN.matcher(testString);
        if (matcher.find())
            return true;
        return false;
    }
}
