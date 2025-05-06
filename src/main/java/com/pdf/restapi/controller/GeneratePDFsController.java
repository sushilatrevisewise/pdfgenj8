package com.pdf.restapi.controller;

import com.pdf.restapi.dto.HTMLContent;
import com.pdf.restapi.dto.InvoicePDFDetails;
import com.pdf.restapi.dto.PdfGenResponse;
import com.pdf.restapi.service.PDFGeneratorService;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.io.File;

@RestController
@Slf4j
public class GeneratePDFsController {
    //private static final Logger LOG = LoggerFactory.getLogger(GeneratePDFsController.class);
    private final PDFGeneratorService pdfGenerator;

    @Value("${storage.htmlpdf.location}")
    private String htmlPdfStorageLocation;
    @Value("${storage.invoices.location}")
    private String invoicesPdfLocation;

    public GeneratePDFsController(PDFGeneratorService pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    @PostMapping("/genpdffromhtml")
    public ResponseEntity<PdfGenResponse> generatePdfFromHTML(@RequestBody @Valid HTMLContent htmlContent) {
        log.error("inside generatePdfFromHTML !");
        String generatedPdfTempDir = (htmlContent.getGeneratedPDFFileStorageDirectory() != null && !htmlContent.getGeneratedPDFFileStorageDirectory().isEmpty()) ? htmlContent.getGeneratedPDFFileStorageDirectory() : htmlPdfStorageLocation;

        String pdfFilePath = generatedPdfTempDir + File.separator + htmlContent.getPdfFileName();

        PdfGenResponse pdfGenResponse = new PdfGenResponse();
        //log.error("htmlContent received : "+htmlContent.toString());
        try {
            pdfGenResponse = pdfGenerator.generatePdfFromHtml(HtmlUtils.htmlUnescape(htmlContent.getHtmlContent()), pdfFilePath);
            pdfGenResponse.setPdfFileName(htmlContent.getPdfFileName());
            pdfGenResponse.setPdfFileLocation(htmlPdfStorageLocation);
            pdfGenResponse.setStatus(Boolean.TRUE);
            pdfGenResponse.setMessage("success");
        } catch (Exception exp) {
            log.error("Exception: {}", exp.getMessage());
            pdfGenResponse.setStatus(Boolean.FALSE);
            pdfGenResponse.setMessage("Some server error encountered!");
        }
        log.error("pdfGenResponse: {}", pdfGenResponse.toString());
        return ResponseEntity.ok(pdfGenResponse);
    }

    @PostMapping("/geninvoicepdf")
    public ResponseEntity<PdfGenResponse> generateInvoicePdfDocument(@RequestBody @Valid InvoicePDFDetails invoicePDFDetails) {
        PdfGenResponse pdfGenResponse = new PdfGenResponse();
        //String pdfFilePath = invoicesPdfLocation;
        try {
            pdfGenResponse = pdfGenerator.generateInvoicePdfDocument(invoicePDFDetails,invoicesPdfLocation);
            //pdfGenResponse.setPdfFileName(htmlContent.getPdfFileName());
            pdfGenResponse.setPdfFileLocation(invoicesPdfLocation);
            pdfGenResponse.setStatus(Boolean.TRUE);
            pdfGenResponse.setMessage("success");
        } catch (Exception exp) {
            pdfGenResponse.setStatus(Boolean.FALSE);
            pdfGenResponse.setMessage("Some server error encountered!");
            log.error("Exception: {}", exp.getMessage());
        }
        log.error("pdfGenResponse:{}", pdfGenResponse.toString());
        return ResponseEntity.ok(pdfGenResponse);
    }


}
