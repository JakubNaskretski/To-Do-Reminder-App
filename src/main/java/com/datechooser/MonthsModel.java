package com.datechooser;

public class MonthsModel {

    private int currentMonth;

    private String[] monthsTable = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};

    public MonthsModel(int currentMonth) {
//        Lowering given month due to the arrays numeration
        this.currentMonth = (currentMonth-1);
    }

    public String[] getMonthsTable() {
        return monthsTable;
    }

    public String getPreviousMonth(){
        if (currentMonth != 0){
            currentMonth -= 1;
        } else {
            currentMonth = 11;
        }
        return monthsTable[currentMonth];
    }

    public String getNextMonth(){
        if (currentMonth != 11){
            currentMonth += 1;
        } else {
            currentMonth = 0;
        }
        return monthsTable[currentMonth];
    }

    public String getCurrentMonth(){
        return monthsTable[currentMonth];
    }

    public int getCurrentMonthNo(){
        return currentMonth;
    }


}
