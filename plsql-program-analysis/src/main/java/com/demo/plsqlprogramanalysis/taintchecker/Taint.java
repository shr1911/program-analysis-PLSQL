package com.demo.plsqlprogramanalysis.taintchecker;

import com.demo.plsqlprogramanalysis.cfg.Node;

public class Taint {
    Node sourceMethod;
    Node sinkMethod;

    public Node getSourceMethod() {
        return sourceMethod;
    }

    public void setSourceMethod(Node sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    public Node getSinkMethod() {
        return sinkMethod;
    }

    public void setSinkMethod(Node sinkMethod) {
        this.sinkMethod = sinkMethod;
    }

}
