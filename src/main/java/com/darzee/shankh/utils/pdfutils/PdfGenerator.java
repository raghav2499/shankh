package com.darzee.shankh.utils.pdfutils;

import com.darzee.shankh.dao.BoutiqueDAO;
import com.darzee.shankh.dao.OrderAmountDAO;
import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.OrderItemDAO;
import com.darzee.shankh.enums.Language;
import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.response.InnerMeasurementDetails;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import com.darzee.shankh.service.OutfitTypeObjectService;
import com.darzee.shankh.service.OutfitTypeService;
import com.darzee.shankh.service.translator.MeasurementDetailsTranslator;
import com.darzee.shankh.service.translator.OrderStitchOptionsTranslator;
import com.darzee.shankh.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PdfGenerator {

    @Autowired
    private OutfitTypeObjectService outfitTypeObjectService;

    @Autowired
    private OrderStitchOptionsTranslator orderStitchOptionsTranslator;

    @Autowired
    private MeasurementDetailsTranslator measurementDetailsTranslator;

    private final TemplateEngine templateEngine;

    @Autowired
    public PdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        configureTemplateResolver();
    }

    private void configureTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        Set<String> resolvablePatterns = new HashSet<>();
//        resolvablePatterns.add("bill_template");
        resolvablePatterns.add("bill_template_v2");
        for(Language language : Language.values()) {
            resolvablePatterns.add(getItemDetailsFileName(language));
        }
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
            DateTimeFormatter createdAtDateFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");
            OrderAmountDAO orderAmount = order.getOrderAmount();
            // Create a JavaScript object and set the dynamic data
            LocalDateTime createdAt = TimeUtils.convertUTCToIST(order.getCreatedAt());
            context.setVariable("businessName", boutiqueDAO.getName());
            context.setVariable("tailorContactNo", tailorContactNo);
            context.setVariable("invoiceNo", order.getInvoiceNo());
            context.setVariable("customerName", customerName);
            context.setVariable("orderId", Optional.ofNullable(order.getBoutiqueOrderId()).orElse(order.getId()));
            context.setVariable("orderCreationDate", createdAt.format(createdAtDateFormatter));
            String updationDate = order.getUpdatedAt() == null
                    ? createdAt.format(createdAtDateFormatter)
                    : TimeUtils.convertUTCToIST(order.getUpdatedAt()).format(createdAtDateFormatter);
            String formattedCreationTime = createdAt.format(timeFormatter);
            List<OrderItemDAO> orderItems = order.getNonDeletedItems();
            for (OrderItemDAO orderItem : orderItems) {
                orderItem.setFormattedDeliveryDate();
            }
            context.setVariable("orderCreationTime", formattedCreationTime);
            context.setVariable("orderItems", orderItems);
            context.setVariable("totalAmount", orderAmount.getTotalAmount());
            context.setVariable("amountPaid", orderAmount.getAmountRecieved());
            context.setVariable("balanceDue", orderAmount.getTotalAmount() - orderAmount.getAmountRecieved());
            context.setVariable("orderUpdationDate", updationDate);
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

    public File generateItemPdf(Long orderNo, String boutiqueName,
                                Map<String, List<OrderStitchOptionDetail>> groupedStitchOptions,
                                List<InnerMeasurementDetails> measurementDetails,
                                List<String> clothImages,
                                List<String> audioInstructionLinks,
                                OrderItemDAO orderItem) throws Exception {
        Context context = new Context();
        String outfitType = orderItem.getOutfitType().getDisplayString();
        Integer outfitPieces = orderItem.getOutfitType().getPieces();
        OutfitTypeService outfitTypeService = outfitTypeObjectService.getOutfitTypeObject(orderItem.getOutfitType());
        String outfitImageLink = outfitTypeService.getOutfitImageLink();
        String specialInstructions = Optional.ofNullable(orderItem.getSpecialInstructions()).orElse("");
        String inspiration = Optional.ofNullable(orderItem.getInspiration()).orElse("");

        // Create a JavaScript object and set the dynamic data
        context.setVariable("businessName", boutiqueName);
        context.setVariable("orderNo", orderNo);
        context.setVariable("outfitType", outfitType);
        context.setVariable("outfitImageLink", outfitImageLink);
        context.setVariable("outfitPieces", outfitPieces);
        context.setVariable("measurementDetails", measurementDetails);
        context.setVariable("groupedStitchOptions", groupedStitchOptions);
        context.setVariable("specialInstructions", specialInstructions);
        context.setVariable("inspiration", inspiration);
        context.setVariable("clothImages", clothImages);
        context.setVariable("audioInstructions", audioInstructionLinks);
        String processedHtml = templateEngine.process("item-details", context);

        // Generate PDF from the processed HTML
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml);
        renderer.layout();

        File outputFile = File.createTempFile("item_details", ".pdf");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            renderer.createPDF(outputStream);
        }

        return outputFile;
    }

    private String getItemDetailsFileName(Language language) {
        switch (language) {
            case ENGLISH:
                return "item-details";
            case HINDI:
                return "item-details-hi";
            case PUNJABI:
                return "item-details-pa";
            case GUJARATI:
                return "item-details-pa";
            case MARATHI:
                return "item-details-mr";
            case TELUGU:
                return "item-details-te";
            case BENGALI:
                return "item-details-bn";
            case KANNADA:
                return "item-details-kn";
            case MALYALAM:
                return "item-details-ml";
            case ODIA:
                return "item-details-or";
            case ASSAMESE:
                return "item-details-as";
            case TAMIL:
                return "item-details-ta";
            case URDU:
                return "item-details-ur";
            default:
                return "item-details";
        }
    }
}
