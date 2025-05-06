package com.pdf.restapi.dto;

import java.util.Objects;

public class PdfGenResponse {
    private boolean status ;//1 --> success, 0 --> failure
    private String message;
    private String pdfFileName;
    private String pdfFileLocation;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public String getPdfFileLocation() {
        return pdfFileLocation;
    }

    public void setPdfFileLocation(String pdfFileLocation) {
        this.pdfFileLocation = pdfFileLocation;
    }

    @Override
    public String toString() {
        return "PdfGenResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", pdfFileName='" + pdfFileName + '\'' +
                ", pdfFileLocation='" + pdfFileLocation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfGenResponse that = (PdfGenResponse) o;
        return status == that.status && Objects.equals(message, that.message) && Objects.equals(pdfFileName, that.pdfFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, pdfFileName);
    }
}
