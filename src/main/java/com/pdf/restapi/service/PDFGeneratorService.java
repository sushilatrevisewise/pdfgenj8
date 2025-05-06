package com.pdf.restapi.service;

import com.pdf.restapi.dto.InvoicePDFDetails;
import com.pdf.restapi.dto.PdfGenResponse;

public interface PDFGeneratorService {
    public PdfGenResponse generatePdfFromHtml(String htmlContent, String pdfStorageLocation) throws Exception;

    public PdfGenResponse generateInvoicePdfDocument(InvoicePDFDetails invoicePDFDetails,String pdfStorageLocation);
}
