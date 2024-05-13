package com.tripj.domain.item.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateItemResponse {

    @Schema(description = "아이템 Id", example = "1")
    private Long itemId;

    public static UpdateItemResponse of(Long itemId) {
        return new UpdateItemResponse(itemId);
    }
}
