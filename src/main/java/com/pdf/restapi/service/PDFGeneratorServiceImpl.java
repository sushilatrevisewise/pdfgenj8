package com.pdf.restapi.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.pdf.restapi.dto.InvoicePDFDetails;
import com.pdf.restapi.dto.ItemDetails;
import com.pdf.restapi.dto.PdfGenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class PDFGeneratorServiceImpl implements PDFGeneratorService {


    @Override
    public PdfGenResponse generatePdfFromHtml(String htmlContent, String pdfFilePath) throws Exception {

        File pdfDest = new File(pdfFilePath);
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(pdfDest));
        PdfGenResponse pdfGenResponse = new PdfGenResponse();
        log.error("HTML pdfFilePath="+pdfFilePath+" PDF file generated!");
        return pdfGenResponse;
    }

    @Override
    public PdfGenResponse generateInvoicePdfDocument(InvoicePDFDetails invoicePDFDetails,String pdfStorageLocation) {
        PdfGenResponse pdfGenResponse = new PdfGenResponse();

        String pdfFileName = "";
        String invoicePdfFileName = "";
        try {

            String transactionId = invoicePDFDetails.getOrderNumber();
            invoicePdfFileName = "invoice_" + transactionId + ".pdf";
            pdfFileName = pdfStorageLocation.concat(File.separator).concat(invoicePdfFileName);
            File file = new File(pdfFileName);
            file.getParentFile().mkdirs();

            NumberFormat nf = new DecimalFormat("####.00");
            Document document = new Document();

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{0.5f, 6, 1, 1, 1, 1});

            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell("No.");
            table.addCell("Title");
            table.addCell("Price");
            table.addCell("GST(%)");
            table.addCell("GST Amount");
            table.addCell("Subtotal");

            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j = 0; j < cells.length; j++) {
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }

            int count = 1;
            for (ItemDetails itmdtl : invoicePDFDetails.getItems()) {
                table.addCell("" + count);
                String productName = itmdtl.getProductName();
                if (itmdtl.getSelectedBooksTitles() != null && !itmdtl.getSelectedBooksTitles().isEmpty()) {
                    for (String btitle : itmdtl.getSelectedBooksTitles()) {
                                productName = productName.concat("\n - " + btitle);
                            }
//                    for (Map.Entry<Long, List<String>> entry : itmdtl.getSelectedBooksTitles().entrySet()) {
//                        if (entry.getKey().longValue() == itmdtl.getProductId().longValue()) {
//                            for (String btitle : entry.getValue()) {
//                                productName = productName.concat("\n - " + btitle);
//                            }
//                        }
//                    }

                }
                Font fontProductName = new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));
                table.addCell(new PdfPCell(new Phrase(productName, fontProductName)));
                table.addCell(new PdfPCell(new Phrase(itmdtl.getPriceAfterCouponDiscount().toString(), fontProductName)));
                table.addCell(new PdfPCell(new Phrase(itmdtl.getGstPercentage().toString(), fontProductName)));
                table.addCell(new PdfPCell(new Phrase(itmdtl.getGstAmount().toString(), fontProductName)));
                table.addCell(new PdfPCell(new Phrase(itmdtl.getItemTotalIncludGST().toString(), fontProductName)));
                count++;
            }

            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            Paragraph p1 = new Paragraph("Retail Invoice", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK));
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);
            document.add(Chunk.NEWLINE);
            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph p = new Paragraph(invoicePDFDetails.getCompanyName() + "\n" + "GSTIN:  " + invoicePDFDetails.getGstin());
            p.add(new Chunk(glue));
            p.add("Invoice Number: " + transactionId);
            document.add(p);

            //add paragraph for customer GST details
            if((invoicePDFDetails.getCustomerGST() != null && !invoicePDFDetails.getCustomerGST().isEmpty())
            && (invoicePDFDetails.getCustomerBusinessAddress() != null && !invoicePDFDetails.getCustomerBusinessAddress().isEmpty())) {
                document.add(Chunk.NEWLINE);
                Paragraph cgstparag = new Paragraph("Customer GSTIN:  " + invoicePDFDetails.getCustomerGST()
                        + "\n" + invoicePDFDetails.getCustomerBusinessAddress());
                cgstparag.add(new Chunk(glue));
                document.add(cgstparag);
            }
            //customer gst end
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));

            Paragraph p11 = new Paragraph("Hi " + invoicePDFDetails.getCustomerName() + ",\nThank you for your Order on " + invoicePDFDetails.getWebsiteAddress() + ". We are pleased to \ninform you that your transaction on "
                    + invoicePDFDetails.getWebsiteAddress() + " is successful and the Invoice Number is " + transactionId + ".", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, BaseColor.BLACK));
            p11.setAlignment(Element.ALIGN_LEFT);
            document.add(p11);

            Paragraph p4 = new Paragraph("Invoice Date: " + invoicePDFDetails.getPurchaseDate() + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK));
            p4.setAlignment(Element.ALIGN_LEFT);
            document.add(p4);

            Paragraph p5 = new Paragraph("Total Items", FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, Font.BOLD, BaseColor.BLACK));
            p5.setAlignment(Element.ALIGN_LEFT);
            document.add(p5);

            document.add(new Chunk());
            document.add(table);


            Paragraph p8 = new Paragraph("Order Total: Rs. " + nf.format(invoicePDFDetails.getTotalAmount()) + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK));
            p8.setAlignment(Element.ALIGN_RIGHT);
            document.add(p8);

            LineSeparator ls1 = new LineSeparator();
            document.add(new Chunk(ls1));

            Paragraph p12 = new Paragraph("Contact Us: " + invoicePDFDetails.getContactUs(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.BLACK));
            p12.setAlignment(Element.ALIGN_LEFT);
            document.add(p12);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception : "+e.getMessage());
        }
        pdfGenResponse.setPdfFileName(invoicePdfFileName);
        pdfGenResponse.setPdfFileLocation(pdfStorageLocation);
        log.error("Invoice PDF file generated!");
        return pdfGenResponse;
    }
}
