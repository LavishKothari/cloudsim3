//package com.ridderware.jfuzzy.examples.mkg;

package org.cloudbus.cloudsim.mkg.fuzzylogic;

public class DataVariables {

    private String name;
    private double contribution;
    private boolean inverseCalculation;

    public boolean isInverseCalculation() {
        return inverseCalculation;
    }

    public void setInverseCalculation(boolean inverseCalculation) {
        this.inverseCalculation = inverseCalculation;
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
