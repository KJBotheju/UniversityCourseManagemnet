package com.erp.universitycoursemanagement.model;

public enum Grade {
    A_PLUS("A+", 4.0), A("A", 4.0), A_MINUS("A-", 3.7),
    B_PLUS("B+", 3.3), B("B", 3.0), B_MINUS("B-", 2.7),
    C_PLUS("C+", 2.3), C("C", 2.0), C_MINUS("C-", 1.7),
    D_PLUS("D+", 1.3), D("D", 1.0), E("E", 0.0);

    private final String symbol;
    private final double points;

    Grade(String symbol, double points) { this.symbol = symbol; this.points = points; }
    public String getSymbol() { return symbol; }
    public double getPoints() { return points; }
}