package com.han.esper.httpserver;

public class SupportBean {
    private String stringProp;
    private int intProp;

    public SupportBean() {
    }

    public SupportBean(String stringProp, int intProp) {
        this.stringProp = stringProp;
        this.intProp = intProp;
    }

    public String getStringProp() {
        return stringProp;
    }

    public int getIntProp() {
        return intProp;
    }

    public void setStringProp(String stringProp) {
        this.stringProp = stringProp;
    }

    public void setIntProp(int intProp) {
        this.intProp = intProp;
    }
}
