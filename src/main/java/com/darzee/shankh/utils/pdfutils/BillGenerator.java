package com.darzee.shankh.utils.pdfutils;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.OrderAmountDAO;
import com.darzee.shankh.dao.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class BillGenerator {

    private final TemplateEngine templateEngine;

    @Autowired
    public BillGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        configureTemplateResolver();
    }

    private void configureTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".ftl");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        Set<String> resolvablePatterns = new HashSet<>();
        resolvablePatterns.add("bill_template");
        resolvablePatterns.add("bill_template_v2");
        templateResolver.setResolvablePatterns(resolvablePatterns);
        templateResolver.setCheckExistence(true);
        templateEngine.setTemplateResolver(templateResolver);
    }

    public File generateBill(String customerName, String customerContactNo, OrderDAO order, OrderAmountDAO orderAmount,
                             BoutiqueDAO boutiqueDAO) {
        try {
            Context context = new Context();
            DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            DateTimeFormatter deliveryDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            // Create a JavaScript object and set the dynamic data
            context.setVariable("businessName", boutiqueDAO.getName());
            context.setVariable("customerName", customerName);
            context.setVariable("customerContactNo", customerContactNo);
            context.setVariable("orderId", order.getId());
            context.setVariable("orderInvoiceNo", order.getInvoiceNo());
            context.setVariable("totalAmount", orderAmount.getTotalAmount());
            context.setVariable("amountPaid", orderAmount.getAmountRecieved());
            context.setVariable("balanceDue", orderAmount.getTotalAmount() - orderAmount.getAmountRecieved());

            // Process the HTML template with the Thymeleaf template engine
            String processedHtml = templateEngine.process("bill_template", context);

            // Generate PDF from the processed HTML
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();

            File outputFile = File.createTempFile("bill", ".pdf");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                renderer.createPDF(outputStream);
            }

            return outputFile;

        } catch (IOException | com.lowagie.text.DocumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public File generateBillV2(BoutiqueDAO boutiqueDAO, String customerName, String tailorContactNo,
                               OrderDAO order) {
        try {
            Context context = new Context();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm z");
            OrderAmountDAO orderAmount = order.getOrderAmount();
            // Create a JavaScript object and set the dynamic data
            context.setVariable("businessName", boutiqueDAO.getName());
            context.setVariable("tailorContactNo", tailorContactNo);
            context.setVariable("invoiceNo", order.getInvoiceNo());
            context.setVariable("customerName", customerName);
            context.setVariable("orderId", order.getId());
            context.setVariable("orderCreationDate", order.getCreatedAt().format(dateFormatter));
            ZonedDateTime zonedDateTime = order.getCreatedAt().atZone(ZoneId.of("Asia/Kolkata"));
            String formattedTime = zonedDateTime.format(timeFormatter);
            context.setVariable("orderCreationTime", formattedTime);
            context.setVariable("orderItems", order.getNonDeletedItems());
            context.setVariable("totalAmount", orderAmount.getTotalAmount());
            context.setVariable("amountPaid", orderAmount.getAmountRecieved());
            context.setVariable("balanceDue", orderAmount.getTotalAmount() - orderAmount.getAmountRecieved());
            context.setVariable("orderUpdationDate", Date.from(order.getUpdatedAt().toInstant(ZoneOffset.UTC)));
            // Process the HTML template with the Thymeleaf template engine
            String processedHtml = templateEngine.process("bill_template_v2", context);

            // Generate PDF from the processed HTML
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();

            File outputFile = File.createTempFile("bill", ".pdf");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                renderer.createPDF(outputStream);
            }

            return outputFile;

        } catch (IOException | com.lowagie.text.DocumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
