package com.pdf.restapi.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import com.pdf.restapi.dto.InvoicePDFDetails;
import com.pdf.restapi.dto.ItemDetails;
import com.pdf.restapi.dto.PdfGenResponse;
import com.pdf.restapi.service.PDFGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Value("${user.name.first}")
    private String firstUserName;
    @Value("${user.pwd.first}")
    private String firstUserPwd;

    @Value("${user.name.second}")
    private String secondUserName;
    @Value("${user.pwd.second}")
    private String secondUserPwd;


    @Value("${htmltopdf.download.url}")
    private String pdfDownloadBaseUrl;

    @Value("${invoice.download.url}")
    private String invoicePDFDownloadBaseUrl;

    //invoice.download.url

    @Value("${storage.htmlpdf.location}")
    private String htmlPdfStorageLocation;

    @Value("${storage.invoices.location}")
    private String invoicesPdfLocation;

    private final PDFGeneratorService pdfGenerator;
    public HomeController(PDFGeneratorService pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request, HttpServletResponse response,Model model) {
        String view = "login";
        String loggedInUserName = (String) request.getSession(true).getAttribute("userName");
        if(loggedInUserName != null && !loggedInUserName.isEmpty()){
            view ="redirect:/home";
        }
        return view;
    }

    @PostMapping("/authenticate")
    public String authenticateUser(HttpServletRequest request, HttpServletResponse response,Model model) {
        String errorMsg = "";
        String view = "login";
        String userName =  request.getParameter("userName");
        log.error("userName: {}", userName);
        String userPassword =  request.getParameter("userPassword");
        log.error("userPassword: {}", userPassword);
        if(userName.isEmpty() || userPassword.isEmpty()){
            log.error("inside if case");
            errorMsg="Username or Password is empty!";
            model.addAttribute("userName", userName);
            model.addAttribute("userPassword", userPassword);
            model.addAttribute("errorMsg", errorMsg);
            log.error("errorMsg: {}", errorMsg);
            return view;
        }else{
            log.error("inside else case");
             if(userName.equalsIgnoreCase(firstUserName) && userPassword.equalsIgnoreCase(firstUserPwd)){
                     request.getSession(true).setAttribute("userName", userName);
             } else if(userName.equalsIgnoreCase(secondUserName) && userPassword.equalsIgnoreCase(secondUserPwd)){
                    request.getSession(true).setAttribute("userName", userName);
             } else{
                 log.error("inside else case 1");
                 view = "login";
                 errorMsg="Invalid Username or Password!";
                 model.addAttribute("userName", userName);
                 model.addAttribute("userPassword", userPassword);
                 model.addAttribute("errorMsg", errorMsg);
                 log.error("errorMsg: {}", errorMsg);
                 return view;
             }

            view ="redirect:/home";
        }
        return view;
    }

    @GetMapping("/home")
    public String homePage(HttpServletRequest request, HttpServletResponse response,Model model) {
        String view = "home";
        String loggedInUserName = (String) request.getSession(true).getAttribute("userName");
        log.error("loggedInUserName: {}", loggedInUserName);
        if(loggedInUserName == null || loggedInUserName.isEmpty()){
            view ="redirect:/login";
        }
        return view;
    }


    @GetMapping("/htmltopdfForm")
    public String htmltopdfForm(HttpServletRequest request, HttpServletResponse response,Model model) {
        String view = "htmltopdfForm";
        String loggedInUserName = (String) request.getSession(true).getAttribute("userName");
        log.error("loggedInUserName: {}", loggedInUserName);
        if(loggedInUserName == null || loggedInUserName.isEmpty()){
            view ="redirect:/login";
            return view;
        }

        return view;
    }


    @PostMapping("/generatepdffromhtml")
    public String generatePDFFromHTML(HttpServletRequest request, HttpServletResponse response,Model model){
        String view = "htmltopdfForm";
        String errorMsg = "";
        String loggedInUserName = (String) request.getSession(true).getAttribute("userName");
        log.error("loggedInUserName: {}", loggedInUserName);
        if(loggedInUserName == null || loggedInUserName.isEmpty()){
            view ="redirect:/login";
            return view;
        }
        String pdfFileName =  request.getParameter("pdfFileName");
        String htmlContent =  request.getParameter("htmlContent");
        if((htmlContent != null && !htmlContent.isEmpty()) && (pdfFileName != null && !pdfFileName.isEmpty())){
            //write code here to generate pdf from html
            String pdfFilePath = htmlPdfStorageLocation + File.separator + pdfFileName;
            try {
                File pdfDest = new File(pdfFilePath);
                HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(pdfDest));
                String downloadUrl=pdfDownloadBaseUrl+pdfFileName;
                String successMsg = "PDF generated successfully! <a href='" + downloadUrl + "'>Download</a>";
                model.addAttribute("successMsg", successMsg);

            }catch(IOException ioex){
                log.error("Exception: {}", ioex.getMessage());
                errorMsg="The creation of the invoice was unsuccessful! Please give it another go later!";
                model.addAttribute("errorMsg", errorMsg);
                return view;
            }

        }else{
            errorMsg="HTML Content and pdf file name are required field!";
            model.addAttribute("errorMsg", errorMsg);
            return view;
        }

        return view;
    }


    @GetMapping("/invoiceForm")
    public String invoiceForm(HttpServletRequest request, HttpServletResponse response,Model model) {
        String view = "invoiceForm";
        String loggedInUserName = (String) request.getSession(true).getAttribute("userName");
        log.error("loggedInUserName: {}", loggedInUserName);
        if(loggedInUserName == null || loggedInUserName.isEmpty()){
            view ="redirect:/login";
            return view;
        }
        model.addAttribute("indx", "1");
        return view;
    }


    @PostMapping("/generateInvoice")
    public String generateInvoicePDF(HttpServletRequest request, HttpServletResponse response,Model model){
        String view = "invoiceForm";
        String errorMsg = "";
        model.addAttribute("indx", "1");
        String loggedInUserName = (String) request.getSession(true).getAttribute("userName");
        log.error("loggedInUserName: {}", loggedInUserName);
        if(loggedInUserName == null || loggedInUserName.isEmpty()){
            view ="redirect:/login";
            return view;
        }
        InvoicePDFDetails invoicePDFDetails = new InvoicePDFDetails();
        String totalNoOfItems =  request.getParameter("totalNoOfItems");
        String customerName =  request.getParameter("customerName");
        invoicePDFDetails.setCustomerName(customerName);
        String invoiceDate =  request.getParameter("invoiceDate");
        invoicePDFDetails.setPurchaseDate(invoiceDate);
        String OrderId =  request.getParameter("OrderId");
        invoicePDFDetails.setOrderNumber(OrderId);
        String invoiceNumber =  request.getParameter("invoiceNumber");

        invoicePDFDetails.setCompanyName("Revisewise Education Solutions Pvt. Ltd.");
        invoicePDFDetails.setContactUs("9953204707");
        invoicePDFDetails.setGstin("09AAFCR8687N1ZA");
        invoicePDFDetails.setWebsiteAddress("www.sofolympiadtrainer.com");

        String customerGSTNo =  request.getParameter("customerGSTNo");
        if(StringUtils.isNotBlank(customerGSTNo)){
            invoicePDFDetails.setCustomerGST(customerGSTNo);
        }
        String customerGSTAddress =  request.getParameter("customerGSTAddress");
        if(StringUtils.isNotBlank(customerGSTAddress)){
            invoicePDFDetails.setCustomerBusinessAddress(customerGSTAddress);
        }

        List<ItemDetails> itemDetailsList = new ArrayList<>();
        if(StringUtils.isNotBlank(totalNoOfItems)){
            int noOfItems  = Integer.parseInt(totalNoOfItems);
            for(int i=1;i<= noOfItems;i++){
                ItemDetails itemDetails = new ItemDetails();
                String bookTitle =  request.getParameter("bookTitle"+i);
                if(StringUtils.isNotBlank(bookTitle)) {
                    itemDetails.setProductName(bookTitle);
                }
                //bookPrice
                String bookPrice =  request.getParameter("bookPrice"+i);
                if(StringUtils.isNotBlank(bookPrice)) {
                    BigDecimal bookPriceBI = new BigDecimal(bookPrice);
                    itemDetails.setPriceAfterCouponDiscount(bookPriceBI);
                }
                //gstpercent
                String gstPercent =  request.getParameter("gstpercent"+i);
                if(StringUtils.isNotBlank(gstPercent)) {
                    itemDetails.setGstPercentage(new BigDecimal(gstPercent));
                }
                //gstAmount
                String gstAmount =  request.getParameter("gstAmount"+i);
                if(StringUtils.isNotBlank(gstAmount)) {
                    itemDetails.setGstAmount(new BigDecimal(gstAmount));
                }
                //subtotal
                String subtotal =  request.getParameter("subtotal"+i);
                if(StringUtils.isNotBlank(subtotal)) {
                    itemDetails.setItemTotalIncludGST(new BigDecimal(subtotal));
                }

                itemDetailsList.add(itemDetails);
            }

        }
        //orderTotal
        String orderTotal =  request.getParameter("orderTotal");
        if(StringUtils.isNotBlank(orderTotal)) {
            invoicePDFDetails.setTotalAmount(new BigDecimal(orderTotal).longValue());
        }
        invoicePDFDetails.setItems(itemDetailsList);

       // String invoiceFileName =  request.getParameter("invoiceFileName");
       // String invoiceFileName =  request.getParameter("invoiceFileName");

        if((invoiceDate != null && !invoiceDate.isEmpty()) && (OrderId != null && !OrderId.isEmpty())){
            //write code here to generate pdf from html
            //String pdfFilePath = htmlPdfStorageLocation + File.separator + invoiceFileName;
            try {
                //File pdfDest = new File(pdfFilePath);
                //HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(pdfDest));

                PdfGenResponse pdfGenResponse = pdfGenerator.generateInvoicePdfDocument(invoicePDFDetails,invoicesPdfLocation);

                String downloadUrl=invoicePDFDownloadBaseUrl+pdfGenResponse.getPdfFileName();
                String successMsg = "PDF generated successfully! <a href='" + downloadUrl + "'>Download</a>";
                model.addAttribute("successMsg", successMsg);

            }catch(Exception ioex){
                log.error("Exception: {}", ioex.getMessage());
                errorMsg="The creation of the invoice was unsuccessful! Please give it another go later!";
                model.addAttribute("errorMsg", errorMsg);
                return view;
            }

        }else{
            errorMsg="All fields are required field!";
            model.addAttribute("errorMsg", errorMsg);
            return view;
        }

        return view;
    }

    @PostMapping("/addMoreItems")
    public String addMoreItems(HttpServletRequest request, HttpServletResponse response,Model model) {
        String view = "addMoreItems";
        String loggedInUserName = (String) request.getSession(true).getAttribute("userName");
        log.error("loggedInUserName: {}", loggedInUserName);
        if(loggedInUserName == null || loggedInUserName.isEmpty()){
            view ="redirect:/login";
            return view;
        }
        String indx =  request.getParameter("indx");
        int indxInt = StringUtils.isNotBlank(indx) ? Integer.parseInt(indx) : 1;
        model.addAttribute("indx", indxInt+1);
        return view;
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,Model model) {
        request.getSession(true).removeAttribute("userName");
        return "redirect:/login";
    }
}
