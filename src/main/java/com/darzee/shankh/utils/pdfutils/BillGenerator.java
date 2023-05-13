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
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;

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
        templateResolver.setResolvablePatterns(new HashSet<>(Collections.singletonList("bill_template")));
        templateResolver.setCheckExistence(true);
        templateEngine.setTemplateResolver(templateResolver);
    }

    public File generateBill(String customerName, String customerContactNo, OrderDAO order, OrderAmountDAO orderAmount,
                             BoutiqueDAO boutiqueDAO) {
        try {
            // Create a Thymeleaf context and set the dynamic data
            Context context = new Context();

            DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String orderCreatedAt = order.getCreatedAt().format(createdAtFormatter);

            DateTimeFormatter deliveryDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String deliveryDate = order.getDeliveryDate().format(deliveryDateFormatter);

            // Create a JavaScript object and set the dynamic data
            context.setVariable("businessName", boutiqueDAO.getName());
            context.setVariable("customerName", customerName);
            context.setVariable("customerContactNo", customerContactNo);
            context.setVariable("orderCreationDate", orderCreatedAt);
            context.setVariable("expectedDeliveryDate", deliveryDate);
            context.setVariable("outfitType", order.getOutfitType().getDisplayString());
            context.setVariable("orderId", order.getId());
            context.setVariable("orderInvoiceNo", order.getInvoiceNo());
            context.setVariable("totalAmount", orderAmount.getTotalAmount());
            context.setVariable("amountPaid", orderAmount.getAmountRecieved());
            context.setVariable("balanceDue", orderAmount.getTotalAmount()  - orderAmount.getAmountRecieved());

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
}
