package com.darzee.shankh.mapper;

import com.darzee.shankh.dao.*;
import com.darzee.shankh.entity.*;
import com.darzee.shankh.response.TailorLoginResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DaoEntityMapper {

    BoutiqueDAO boutiqueObjectToDao(Boutique boutique, @Context CycleAvoidingMappingContext context);

    Boutique boutiqueDaoToObject(BoutiqueDAO boutique, @Context CycleAvoidingMappingContext context);

    TailorDAO tailorObjectToDao(Tailor tailor, @Context CycleAvoidingMappingContext context);

    Tailor tailorDaoToObject(TailorDAO tailoDAO, @Context CycleAvoidingMappingContext context);

    OrderDAO orderObjectToDao(Order order, @Context CycleAvoidingMappingContext context);

    Order orderaDaoToObject(OrderDAO orderDAO, @Context CycleAvoidingMappingContext context);

    CustomerDAO customerObjectToDao(Customer customer, @Context CycleAvoidingMappingContext context);

    Customer customerDaoToObject(CustomerDAO customerDAO, @Context CycleAvoidingMappingContext context);

    BoutiqueLedgerDAO boutiqueLedgerObjectToDAO(BoutiqueLedger boutiqueLedger, @Context CycleAvoidingMappingContext context);

    BoutiqueLedger boutiqueLedgerDAOToObject(BoutiqueLedgerDAO boutiqueLedgerDAO, @Context CycleAvoidingMappingContext context);

    ObjectImages objectImageDAOToObjectImage(ObjectImagesDAO objectImages);

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


}
