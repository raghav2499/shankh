package com.darzee.shankh.mapper;

import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.*;
import com.darzee.shankh.response.TailorLoginResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DaoEntityMapper {

    BoutiqueDAO boutiqueObjectToDao(Boutique boutique, @Context CycleAvoidingMappingContext context);

    Boutique boutiqueDaoToObject(BoutiqueDAO boutique, @Context CycleAvoidingMappingContext context);

    TailorDAO tailorObjectToDao(Tailor tailor, @Context CycleAvoidingMappingContext context);

    Tailor tailorDaoToObject(TailorDAO tailoDAO, @Context CycleAvoidingMappingContext context);

    OrderDAO orderObjectToDao(Order order, @Context CycleAvoidingMappingContext context);

    SampleImageReferenceDAO sampleImageRefToDAO(SampleImageReference sampleImageReference);

    Order orderaDaoToObject(OrderDAO orderDAO, @Context CycleAvoidingMappingContext context);

    CustomerDAO customerObjectToDao(Customer customer, @Context CycleAvoidingMappingContext context);

    Customer customerDaoToObject(CustomerDAO customerDAO, @Context CycleAvoidingMappingContext context);

    BoutiqueLedgerDAO boutiqueLedgerObjectToDAO(BoutiqueLedger boutiqueLedger, @Context CycleAvoidingMappingContext context);

    BoutiqueLedger boutiqueLedgerDAOToObject(BoutiqueLedgerDAO boutiqueLedgerDAO, @Context CycleAvoidingMappingContext context);

    ObjectImages objectImageDAOToObjectImage(ObjectImagesDAO objectImages);

    OrderItemDAO orderItemToOrderItemDAO(OrderItem orderItem, @Context CycleAvoidingMappingContext context);

    OrderItem orderItemDAOToOrderItem(OrderItemDAO orderItemDAO, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "tailorDAO.id", target = "tailorId")
    @Mapping(source = "tailorDAO.name", target = "tailorName")
    @Mapping(source = "tailorDAO.boutique.id", target = "boutiqueId")
    TailorLoginResponse tailorDAOToLoginResponse(TailorDAO tailorDAO, String token);

    ObjectImages boutiqueImagesImagesDAOToBoutiqueImages(ObjectImagesDAO ObjectImagesDAO);

    ImageReferenceDAO imageReferenceToImageReferenceDAO(ImageReference imageReference);

    ImageReference imageReferenceDAOToImageReference(ImageReferenceDAO imageReferenceDAO);

    OrderAmountDAO orderAmountObjectToOrderAmountDao(OrderAmount orderAmount, @Context CycleAvoidingMappingContext context);

    OrderAmount orderAmountDaoToOrderAmountObject(OrderAmountDAO orderAmount, @Context CycleAvoidingMappingContext context);

    ObjectImagesDAO objectImagesToObjectImagesDAO(ObjectImages objectImages);

    Payment paymentDAOToPayment(PaymentDAO paymentDAO, @Context CycleAvoidingMappingContext context);

    PaymentDAO paymentToPaymentDAO(Payment payment, @Context CycleAvoidingMappingContext context);

    BoutiqueLedgerSnapshot boutiqueLedgerSnapshotDAOToSnapshot(BoutiqueLedgerSnapshotDAO boutiqueLedgerSnapshotDAO);

    BoutiqueLedgerSnapshotDAO boutiqueLedgerSnapshotToSnapshotDAO(BoutiqueLedgerSnapshot boutiqueLedgerSnapshotDAO);

    Portfolio portfolioDAOToPortfolio(PortfolioDAO portfolioDAO, @Context CycleAvoidingMappingContext context);

    PortfolioDAO portfolioToPortfolioDAO(Portfolio portfolio, @Context CycleAvoidingMappingContext context);

    PortfolioOutfits portfolioOutfitsDAOToPortfolioOutfits(PortfolioOutfitsDAO portfolioOutfitsDAO, @Context CycleAvoidingMappingContext context);

    PortfolioOutfitsDAO portfolioOutfitsToPortfolioOutfitsDAO(PortfolioOutfits portfolioOutfits, @Context CycleAvoidingMappingContext context);

    DeviceInfoDAO deviceInfoToDAO(DeviceInfo deviceInfo);

    DeviceInfo deviceInfoDAOToDeviceInfo(DeviceInfoDAO deviceInfoDAO);

    MeasurementsDAO measurementsToMeasurementDAO(Measurements measurements, @Context CycleAvoidingMappingContext context);

    StitchOptionsDAO stitchOptionsToDAO(StitchOptions stitchOptions);

    Measurements measurementsDAOToMeasurement(MeasurementsDAO measurements, @Context CycleAvoidingMappingContext context);

    default List<OrderDAO> orderObjectListToDAOList(List<Order> orderList, @Context CycleAvoidingMappingContext context) {
        if (orderList == null) {
            return null;
        }
        List<OrderDAO> orderDAOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderDAO orderDAO = orderObjectToDao(order, context);
            orderDAOList.add(orderDAO);
        }
        return orderDAOList;
    }

    default List<SampleImageReferenceDAO> sampleImageRefToDAOList(List<SampleImageReference> sampleImageReferences) {
        if (sampleImageReferences == null) {
            return null;
        }
        List<SampleImageReferenceDAO> referenceDAOList = new ArrayList<>();
        for (SampleImageReference sampleImageReference : sampleImageReferences) {
            SampleImageReferenceDAO imageReferenceDAO = sampleImageRefToDAO(sampleImageReference);
            referenceDAOList.add(imageReferenceDAO);
        }
        return referenceDAOList;
    }

    default List<CustomerDAO> customerObjectListToDAOList(List<Customer> customerList,
                                                          @Context CycleAvoidingMappingContext context) {
        if (customerList == null) {
            return null;
        }
        List<CustomerDAO> customerDAOList = new ArrayList<>();
        for (Customer customer : customerList) {
            CustomerDAO customerDAO = customerObjectToDao(customer, context);
            customerDAOList.add(customerDAO);
        }
        return customerDAOList;
    }

    default List<Order> orderDAOListToObjectList(List<OrderDAO> orderDAOList, @Context CycleAvoidingMappingContext context) {
        if (orderDAOList == null) {
            return null;
        }
        List<Order> orderList = new ArrayList<>();
        for (OrderDAO orderDAO : orderDAOList) {
            Order order = orderaDaoToObject(orderDAO, context);
            orderList.add(order);
        }
        return orderList;
    }

    default List<PortfolioOutfitsDAO> portfolioObjectListToDAOList(List<PortfolioOutfits> portfolioOutfits,
                                                                   @Context CycleAvoidingMappingContext context) {
        if (portfolioOutfits == null) {
            return null;
        }
        List<PortfolioOutfitsDAO> portfolioOutfitsDAOList = new ArrayList<>();
        for (PortfolioOutfits portfolioOutfit : portfolioOutfits) {
            PortfolioOutfitsDAO portfolioOutfitsDAO = portfolioOutfitsToPortfolioOutfitsDAO(portfolioOutfit, context);
            portfolioOutfitsDAOList.add(portfolioOutfitsDAO);
        }
        return portfolioOutfitsDAOList;
    }

    default List<MeasurementRevisionsDAO> measurementRevisionsListToDAOList(List<MeasurementRevisions> measurementRevisions) {
        if (measurementRevisions == null) {
            return null;
        }
        List<MeasurementRevisionsDAO> measurementRevisionsDAOs = new ArrayList<>();
        for (MeasurementRevisions revision : measurementRevisions) {
            MeasurementRevisionsDAO measurementRevisionsDAO = measurementRevisionsToMeasurementRevisionDAO(revision);
            measurementRevisionsDAOs.add(measurementRevisionsDAO);
        }
        return measurementRevisionsDAOs;
    }

    default List<PriceBreakup> priceBreakUpDAOListToPriceBreakUpList(List<PriceBreakupDAO> priceBreakupDAOList,
                                                                     @Context CycleAvoidingMappingContext context) {
        if (priceBreakupDAOList == null) {
            return null;
        }
        List<PriceBreakup> priceBreakups = new ArrayList<>();
        for (PriceBreakupDAO priceBreakupDAO : priceBreakupDAOList) {
            PriceBreakup priceBreakup = priceBreakupDAOToPriceBreakup(priceBreakupDAO, context);
            priceBreakups.add(priceBreakup);
        }
        return priceBreakups;
    }

    default List<PriceBreakupDAO> priceBreakUpListToPriceBreakUpDAOList(List<PriceBreakup> priceBreakupList,
                                                                        @Context CycleAvoidingMappingContext context) {
        if (priceBreakupList == null) {
            return null;
        }
        List<PriceBreakupDAO> priceBreakups = new ArrayList<>();
        for (PriceBreakup priceBreakup : priceBreakupList) {
            PriceBreakupDAO priceBreakupDAO = priceBreakupToPriceBreakupDAO(priceBreakup, context);
            priceBreakups.add(priceBreakupDAO);
        }
        return priceBreakups;
    }

    default List<OrderItemDAO> orderItemListToOrderItemDAOList(List<OrderItem> orderItems,
                                                               @Context CycleAvoidingMappingContext context) {
        if (orderItems == null) {
            return null;
        }
        List<OrderItemDAO> orderItemDAOList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDAO orderItemDAO = orderItemToOrderItemDAO(orderItem, context);
            orderItemDAOList.add(orderItemDAO);
        }
        return orderItemDAOList;
    }

    default List<OrderItem> orderItemDAOListToOrderItemList(List<OrderItemDAO> orderItemDAOList,
                                                            @Context CycleAvoidingMappingContext context) {
        if (orderItemDAOList == null) {
            return null;
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDAO orderItemDAO : orderItemDAOList) {
            OrderItem orderItem = orderItemDAOToOrderItem(orderItemDAO, context);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    default List<StitchOptionsDAO> stitchOptionListToStitchOptionDAOList(List<StitchOptions> stitchOptions) {
        if(CollectionUtils.isEmpty(stitchOptions)) {
            return Collections.emptyList();
        }
        List<StitchOptionsDAO> stitchOptionsDAOS = new ArrayList<>();
        for(StitchOptions stitchOption : stitchOptions) {
            StitchOptionsDAO stitchOptionsDAO = stitchOptionsToDAO(stitchOption);
            stitchOptionsDAOS.add(stitchOptionsDAO);
        }
        return stitchOptionsDAOS;
    }

    default OrderStitchOptions createStitchOptionDaoToObject(OrderStitchOptionDAO orderStitchOptionDAO,
                    @Context CycleAvoidingMappingContext context) {
                if(orderStitchOptionDAO == null) {
                    return null;
                }
                OrderStitchOptions orderStitchOptions = new OrderStitchOptions();
                orderStitchOptions.setId(orderStitchOptionDAO.getId());
                orderStitchOptions.setStitchOptionId(orderStitchOptionDAO.getStitchOptionId());
                orderStitchOptions.setValues(orderStitchOptionDAO.getValues());
                // orderStitchOptions.setOrderItem( orderaDaoToObject( orderStitchOptionDAO.getOrderItemId(), context ) );
                // orderStitchOptions.setOrderItem(new Order()); 

                System.out.println("orderStitchOptions: " + orderStitchOptions.toString());
                System.out.println("orderstitch value " + orderStitchOptions.getValues().toString());

                return orderStitchOptions;
            };
    

    MeasurementRevisions measurementRevisionsDAOToMeasurementRevision(MeasurementRevisionsDAO measurementRevisionsDAO);

    MeasurementRevisionsDAO measurementRevisionsToMeasurementRevisionDAO(MeasurementRevisions measurementRevision);

    PriceBreakup priceBreakupDAOToPriceBreakup(PriceBreakupDAO priceBreakupDAO, @Context CycleAvoidingMappingContext context);

    PriceBreakupDAO priceBreakupToPriceBreakupDAO(PriceBreakup priceBreakup, @Context CycleAvoidingMappingContext context);


}
