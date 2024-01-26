package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderStitchOptionDAO;
import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.entity.OrderItem;
import com.darzee.shankh.entity.StitchOptions;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.OrderItemRepo;
import com.darzee.shankh.repo.OrderStitchOptionsRepo;
import com.darzee.shankh.repo.StitchOptionsRepo;
import com.darzee.shankh.request.CreateStitchOptionRequest;
import com.darzee.shankh.request.innerObjects.StitchDetails;
import com.darzee.shankh.response.GetStitchOptionsResponse;
import com.darzee.shankh.response.StitchSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public StitchSummary createStitchOptions(CreateStitchOptionRequest createStitchOptionRequest) {

        validateCreateStitchOptionRequest(createStitchOptionRequest);
        Long orderItemId = createStitchOptionRequest.getOrderItemId();
        List<StitchDetails> stitchDetails = createStitchOptionRequest.getStitchDetails();
        List<OrderStitchOptionDAO> orderStitchOptionDAOs = new ArrayList<>();

        for (StitchDetails stitchDetail : stitchDetails) {
            OrderStitchOptionDAO orderStitchOptionDAO = new OrderStitchOptionDAO(stitchDetail.getStitchOptionId(),
                    stitchDetail.getValues(), orderItemId);
            orderStitchOptionDAOs.add(orderStitchOptionDAO);
        }
        orderStitchOptionsRepo.saveAll(mapper.orderStitchOptionDAOListToOrderStitchOptionList(orderStitchOptionDAOs));
        return new StitchSummary(orderItemId, orderStitchOptionDAOs);
    }

    private void validateCreateStitchOptionRequest(CreateStitchOptionRequest request) {
        Optional<OrderItem> orderItem = orderItemRepo.findById(request.getOrderItemId());
        if(orderItem.get() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Order Item ID");
        }
        List<Long> stitchOptionIdInRequest = request.getStitchDetails().stream()
                .map(stitchDetails -> stitchDetails.getStitchOptionId())
                .collect(Collectors.toList());
        List<StitchOptions> stitchOptions = stitchOptionsRepo.findAllByIdIn(stitchOptionIdInRequest);
        if(stitchOptions == null || stitchOptions.size() != request.getStitchDetails().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some Stitch Option ID is invalid");
        }
    }

    public GetStitchOptionsResponse getStitchOptions(Integer outfitId) {
        OutfitType outfitType = OutfitType.getOutfitOrdinalEnumMap().get(outfitId);
        if(outfitType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Outfit Id");
        }
        List<StitchOptionsDAO> stitchOptions = mapper.stitchOptionListToStitchOptionDAOList(
                stitchOptionsRepo.findAllByOutfitTypeAndIsValid(outfitType, Boolean.TRUE));
        GetStitchOptionsResponse stitchOptionsResponse = new GetStitchOptionsResponse(stitchOptions);
        return stitchOptionsResponse;
    }
}
