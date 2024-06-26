package com.tripj.domain.board.repository;

import com.tripj.domain.board.model.dto.request.GetBoardSearchRequest;
import com.tripj.domain.board.model.dto.response.GetBoardDetailResponse;
import com.tripj.domain.board.model.dto.response.GetBoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface BoardRepositoryCustom {
    Slice<GetBoardResponse> findAllPaging(Long lastBoardId, Pageable pageable);

    GetBoardDetailResponse getBoardDetail(Long boardId);

    List<GetBoardResponse> getBoardList(Long boardCateId);

    List<GetBoardResponse> getBoardLatestList();

    List<GetBoardResponse> getBoardPopularList();

    List<GetBoardResponse> getAllBoardList(GetBoardSearchRequest request);

    List<GetBoardResponse> getMyBoardList(Long userId);

    List<GetBoardResponse> getMyLikedBoard(Long userId);

}
