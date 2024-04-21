package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.request.GetFileLinkRequest;
import com.darzee.shankh.response.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class FileLinkService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BucketService bucketService;

    public ResponseEntity<GetFileResponse> getFileLinkResponse(GetFileLinkRequest fileLinkRequest) throws Exception {
        String link = getFileLink(fileLinkRequest);
        GetFileResponse response = new GetFileResponse(link);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getFileLink(GetFileLinkRequest request) throws Exception {
        String entityType = request.getEntityType();
        switch(entityType) {
            case "invoice":
                Long boutiqueId = request.getMetaData() != null ? request.getMetaData().getBoutiqueId() : null;
                OrderDAO orderDAO = orderService.findOrder(request.getEntityId(), boutiqueId);
                Long entityId = Optional.ofNullable(orderDAO.getBoutiqueOrderId()).orElse(orderDAO.getId());
                return bucketService.getInvoiceShortLivedLink(entityId, boutiqueId);
            case "item_details":
                return bucketService.getItemDetailsShortLivedLink(request.getEntityId());
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This entity_type is not supported");
        }
    }
}
