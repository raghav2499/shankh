package com.darzee.shankh.service;

import com.darzee.shankh.dao.StitchOptionsDAO;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.StitchOptionsRepo;
import com.darzee.shankh.response.GetStitchOptionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StitchOptionsService {

    @Autowired
    private StitchOptionsRepo stitchOptionsRepo;

    @Autowired
    private DaoEntityMapper mapper;

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
