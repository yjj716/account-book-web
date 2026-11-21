package com.yjj.accountbook.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecordCreateRequest {
    @NotNull(message="金额不能为空")
    @Positive(message="金额必须大于0")
    private BigDecimal amount;
    @NotNull(message = "类型不能个为空")
    private Integer type;
    @NotBlank(message = "分类不能为空")
    @Size(max=20,message = "分类最多20个字")
    private String category;
            @Size(max=200,message="备注最多200个字")
    private String note;
    @NotNull(message = "日期不能为空")
    private LocalDate recordDate;



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
