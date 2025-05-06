package com.pdf.restapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;

public class InvoicePDFDetails {
    @NotBlank(message = "[GSTIN] cannot be blank")
    private String gstin; //"10BBFCR4547N9AZ"
    @NotBlank(message = "[customerName] cannot be blank")
    private String customerName;
    @NotBlank(message = "[companyName] cannot be blank")
    private String companyName;
    @NotBlank(message = "[websiteAddress] cannot be blank")
    private String websiteAddress;
    @NotBlank(message = "[contactUs] cannot be blank")
    private String contactUs;
    @NotBlank(message = "[pdfFileName] cannot be blank")
    private String pdfFileName; //invoice_2456537.pdf
    @NotBlank(message = "[orderNumber] cannot be blank")
    private String orderNumber;

    //private String transactionId;
    private List<ItemDetails> items;
    @NotNull(message = "[subTotal] cannot be empty")
    private Long subTotal;
    @NotNull(message = "[stax] cannot be empty")
    private Long stax;
    @NotNull(message = "[totalAmount] cannot be empty")
    private Long totalAmount;
    @NotBlank(message = "[purchaseDate] cannot be empty")
    private String purchaseDate;

    private String customerGST;

    private String customerBusinessAddress;

    public String getCustomerGST() {
        return customerGST;
    }

    public void setCustomerGST(String customerGST) {
        this.customerGST = customerGST;
    }

    public String getCustomerBusinessAddress() {
        return customerBusinessAddress;
    }

    public void setCustomerBusinessAddress(String customerBusinessAddress) {
        this.customerBusinessAddress = customerBusinessAddress;
    }

    public String getContactUs() {
        return contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
    }

    public String getWebsiteAddress() {
        return websiteAddress;
    }

    public void setWebsiteAddress(String websiteAddress) {
        this.websiteAddress = websiteAddress;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<ItemDetails> getItems() {
        return items;
    }

    public void setItems(List<ItemDetails> items) {
        this.items = items;
    }

    public Long getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Long subTotal) {
        this.subTotal = subTotal;
    }

    public Long getStax() {
        return stax;
    }

    public void setStax(Long stax) {
        this.stax = stax;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
