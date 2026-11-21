package com.yjj.accountbook.entity.dto;

import java.math.BigDecimal;

public class RecordStatistics {
    private BigDecimal income;    // 收入合计
    private BigDecimal expense;   // 支出合计
    private BigDecimal balance;   // 结余

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getBalance() {
        BigDecimal in=income==null?BigDecimal.ZERO:income;
        BigDecimal ex=expense==null?BigDecimal.ZERO:expense;
        return in.subtract(ex);
    }


    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
