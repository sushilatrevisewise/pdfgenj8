package com.pdf.restapi.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ItemDetails {

    //Map<Long, List<String>> selectedBooksTitles;
    List<String> selectedBooksTitles;
    private Long productId;
    private String productName;
    private BigDecimal gstPercentage;
    private BigDecimal gstAmount;
    private BigDecimal priceAfterCouponDiscount;//final prize of item /or after coupon apply
    private BigDecimal itemTotalIncludGST;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getGstPercentage() {
        return gstPercentage;
    }

    public void setGstPercentage(BigDecimal gstPercentage) {
        this.gstPercentage = gstPercentage;
    }

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    public BigDecimal getPriceAfterCouponDiscount() {
        return priceAfterCouponDiscount;
    }

    public void setPriceAfterCouponDiscount(BigDecimal priceAfterCouponDiscount) {
        this.priceAfterCouponDiscount = priceAfterCouponDiscount;
    }

    public BigDecimal getItemTotalIncludGST() {
        return itemTotalIncludGST;
    }

    public void setItemTotalIncludGST(BigDecimal itemTotalIncludGST) {
        this.itemTotalIncludGST = itemTotalIncludGST;
    }

    public List<String> getSelectedBooksTitles() {
        return selectedBooksTitles;
    }

    public void setSelectedBooksTitles(List<String> selectedBooksTitles) {
        this.selectedBooksTitles = selectedBooksTitles;
    }
}
