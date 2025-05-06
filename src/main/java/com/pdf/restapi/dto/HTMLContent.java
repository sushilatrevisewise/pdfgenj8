package com.pdf.restapi.dto;


import javax.validation.constraints.NotNull;

public class HTMLContent {
    @NotNull
    private String htmlContent;
    @NotNull
    private String pdfFileName;

    private String generatedPDFFileStorageDirectory;

    public String getGeneratedPDFFileStorageDirectory() {
        return generatedPDFFileStorageDirectory;
    }

    public void setGeneratedPDFFileStorageDirectory(String generatedPDFFileStorageDirectory) {
        this.generatedPDFFileStorageDirectory = generatedPDFFileStorageDirectory;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
