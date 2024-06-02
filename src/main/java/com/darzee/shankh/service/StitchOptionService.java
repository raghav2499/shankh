package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderStitchOptionDAO;
import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.entity.OrderStitchOptions;
import com.darzee.shankh.entity.StitchOptions;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.OrderItemRepo;
import com.darzee.shankh.repo.OrderRepo;
import com.darzee.shankh.repo.OrderStitchOptionsRepo;
import com.darzee.shankh.repo.StitchOptionsRepo;
import com.darzee.shankh.request.CreateStitchOptionRequest;
import com.darzee.shankh.request.innerObjects.StitchDetails;
import com.darzee.shankh.response.GetOrderStitchOptionResponse;
import com.darzee.shankh.response.GetStitchOptionsResponse;
import com.darzee.shankh.response.OrderStitchOptionDetail;
import com.darzee.shankh.response.StitchSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StitchOptionService {

    @Autowired
    private OrderStitchOptionsRepo orderStitchOptionsRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private StitchOptionsRepo stitchOptionsRepo;

    @Autowired
    private DaoEntityMapper mapper;
    @Autowired
    private OrderRepo orderRepo;

    @Transactional
    public StitchSummary createStitchOptions(CreateStitchOptionRequest createStitchOptionRequest) {

        validateStitchOptionRequest(createStitchOptionRequest);
        List<StitchDetails> stitchDetails = createStitchOptionRequest.getStitchDetails();
        List<OrderStitchOptionDAO> orderStitchOptionDAOs = new ArrayList<>();

        for (StitchDetails stitchDetail : stitchDetails) {
            OrderStitchOptionDAO orderStitchOptionDAO = new OrderStitchOptionDAO(stitchDetail.getStitchOptionId(), stitchDetail.getValues());
            orderStitchOptionDAOs.add(orderStitchOptionDAO);
        }
        orderStitchOptionDAOs = mapper.orderStitchOptionListToOrderStitchOptionDAOList(orderStitchOptionsRepo.saveAll(mapper.orderStitchOptionDAOListToOrderStitchOptionList(orderStitchOptionDAOs)));
        return new StitchSummary(orderStitchOptionDAOs);
    }

    @Transactional
    public StitchSummary updateStitchOptions(Long orderItemId, CreateStitchOptionRequest updateStitchOptionRequest) {
        validateStitchOptionRequest(updateStitchOptionRequest);
        List<StitchDetails> stitchDetails = updateStitchOptionRequest.getStitchDetails();
        List<OrderStitchOptionDAO> existingStitchOptions = mapper.orderStitchOptionListToOrderStitchOptionDAOList(orderStitchOptionsRepo.findAllByOrderItemIdAndIsValid(orderItemId, Boolean.TRUE));
        for (OrderStitchOptionDAO stitchOption : existingStitchOptions) {
            stitchOption.setIsValid(Boolean.FALSE);
        }
        List<OrderStitchOptionDAO> newStitchOptions = new ArrayList<>();
        for (StitchDetails newStitchDetail : stitchDetails) {
            OrderStitchOptionDAO orderStitchOptionDAO = new OrderStitchOptionDAO(newStitchDetail.getStitchOptionId(), newStitchDetail.getValues(), orderItemId);
            newStitchOptions.add(orderStitchOptionDAO);
        }
        if (!CollectionUtils.isEmpty(existingStitchOptions)) {
            orderStitchOptionsRepo.saveAll(mapper.orderStitchOptionDAOListToOrderStitchOptionList(existingStitchOptions));
        }
        newStitchOptions = mapper.orderStitchOptionListToOrderStitchOptionDAOList(orderStitchOptionsRepo.saveAll(mapper.orderStitchOptionDAOListToOrderStitchOptionList(newStitchOptions)));
        return new StitchSummary(newStitchOptions);
    }

    @Transactional
    public void addOrderItemId(List<Long> orderStitchOptionRef, Long orderItemId) {
        List<OrderStitchOptionDAO> orderStitchOptionDAOs = mapper.orderStitchOptionListToOrderStitchOptionDAOList(orderStitchOptionsRepo.findAllByIdIn(orderStitchOptionRef));
        for (OrderStitchOptionDAO orderStitchOption : orderStitchOptionDAOs) {
            orderStitchOption.setOrderItemId(orderItemId);
        }
        orderStitchOptionsRepo.saveAll(mapper.orderStitchOptionDAOListToOrderStitchOptionList(orderStitchOptionDAOs));
    }

    private void validateStitchOptionRequest(CreateStitchOptionRequest request) {
        List<Long> stitchOptionIdInRequest = request.getStitchDetails().stream().map(stitchDetails -> stitchDetails.getStitchOptionId()).collect(Collectors.toList());
        List<StitchOptions> stitchOptions = stitchOptionsRepo.findAllByIdIn(stitchOptionIdInRequest);
        if (stitchOptions == null || stitchOptions.size() != request.getStitchDetails().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some Stitch Option ID is invalid");
        }
        for(StitchDetails stitchDetail : request.getStitchDetails()) {
            if(CollectionUtils.isEmpty(stitchDetail.getValues())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Stitch option value");
            }
        }
    }

    public GetStitchOptionsResponse getStitchOptions(Integer outfitId) {
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitId);
        if (outfitType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Outfit Id");
        }
        List<StitchOptionsDAO> stitchOptions = mapper.stitchOptionListToStitchOptionDAOList(stitchOptionsRepo.findAllByOutfitTypeAndIsValidOrderByPriority(outfitType, Boolean.TRUE));
        GetStitchOptionsResponse stitchOptionsResponse = new GetStitchOptionsResponse(stitchOptions);
        return stitchOptionsResponse;
    }

    public ResponseEntity getOrderItemStitchOptionsResponse(Long orderItemId) {
        Map<String, List<OrderStitchOptionDetail>> groupedOrderStitchOptionDetail = getOrderItemStitchOptions(orderItemId);
        GetOrderStitchOptionResponse response = new GetOrderStitchOptionResponse(groupedOrderStitchOptionDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Map<String, List<OrderStitchOptionDetail>> getOrderItemStitchOptions(Long orderItemId) {
        List<OrderStitchOptions> orderStitchOptions = orderStitchOptionsRepo.findAllByOrderItemIdAndIsValid(orderItemId, Boolean.TRUE);
        List<OrderStitchOptionDetail> orderStitchOptionDetails = new ArrayList<>();
        List<OrderStitchOptionDAO> orderStitchOptionDAOs = mapper.orderStitchOptionListToOrderStitchOptionDAOList(orderStitchOptions);
        for (OrderStitchOptionDAO orderStitchOptionDAO : orderStitchOptionDAOs) {
            StitchOptionsDAO stitchOptionsDAO = mapper.stitchOptionsToDAO(stitchOptionsRepo.findById(orderStitchOptionDAO.getStitchOptionId()).get());
            orderStitchOptionDetails.add(new OrderStitchOptionDetail(orderStitchOptionDAO, stitchOptionsDAO));
        }
        Map<String, List<OrderStitchOptionDetail>> groupedOrderStitchOptionDetail = orderStitchOptionDetails.stream().collect(Collectors.groupingBy(OrderStitchOptionDetail::getOutfitSide));
        return groupedOrderStitchOptionDetail;
    }
}
