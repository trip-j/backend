package com.tripj.domain.item.controller;

import com.tripj.domain.item.model.dto.request.CreateItemRequest;
import com.tripj.domain.item.model.dto.request.UpdateItemRequest;
import com.tripj.domain.item.model.dto.response.CreateItemResponse;
import com.tripj.domain.item.model.dto.response.DeleteItemResponse;
import com.tripj.domain.item.model.dto.response.UpdateItemResponse;
import com.tripj.domain.item.service.ItemService;
import com.tripj.global.code.ErrorCode;
import com.tripj.global.model.RestApiResponse;
import com.tripj.resolver.userinfo.UserInfo;
import com.tripj.resolver.userinfo.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
@Tag(name = "item", description = "아이템 API")
public class ItemController {

    private final ItemService itemService;

    @Operation(
            summary = "아이템 등록 API",
            description = "체크리스트에서 아이템을 등록합니다."
    )
    @PostMapping("")
    public RestApiResponse<CreateItemResponse> createItem(
            @RequestBody CreateItemRequest request,
            @UserInfo UserInfoDto userInfo) {

        return RestApiResponse.success(
                itemService.createItem(request, userInfo.getUserId()));
    }

    @Operation(
            summary = "아이템 수정 API",
            description = "체크리스트에서 아이템을 수정합니다."
    )
    @PostMapping("/{itemId}")
    public RestApiResponse<UpdateItemResponse> updateItem(
            @Validated @RequestBody UpdateItemRequest request,
            @PathVariable Long itemId,
            @UserInfo UserInfoDto userInfo,
            BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return RestApiResponse.error(ErrorCode.E400_BINDING_RESULT, errorMessage);
        }

        return RestApiResponse.success(
                itemService.updateItem(request, itemId, userInfo.getUserId()));
    }

    @Operation(
            summary = "아이템 삭제 API",
            description = "체크리스트에서 아이템을 삭제합니다."
    )
    @DeleteMapping("/{itemId}")
    public RestApiResponse<DeleteItemResponse> deleteItem(
            @PathVariable Long itemId,
            @UserInfo UserInfoDto userInfo) {

        return RestApiResponse.success(
                itemService.deleteItem(itemId, userInfo.getUserId()));
    }

}
