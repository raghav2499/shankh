package com.darzee.shankh.response;

import com.darzee.shankh.dao.StitchOptionsDAO;
import lombok.Data;

import java.util.List;

@Data
public class StitchOptionDetail {
    private Long id;
    private String type;
    private String key;
    private List<String> value;

    public StitchOptionDetail(StitchOptionsDAO stitchOptionsDAO) {
        this.id = stitchOptionsDAO.getId();
        this.type = stitchOptionsDAO.getType().getName();
        this.key = stitchOptionsDAO.getKey();
        this.value = stitchOptionsDAO.getValue();
    }
}
