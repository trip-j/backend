package com.tripj.domain.board.service;

import com.tripj.domain.board.model.dto.request.CreateBoardRequest;
import com.tripj.domain.board.model.dto.request.GetBoardRequest;
import com.tripj.domain.board.model.dto.response.CreateBoardResponse;
import com.tripj.domain.board.model.dto.response.GetBoardCommentResponse;
import com.tripj.domain.board.model.dto.response.GetBoardResponse;
import com.tripj.domain.board.model.entity.Board;
import com.tripj.domain.board.repository.BoardRepository;
import com.tripj.domain.boardcate.model.entity.BoardCate;
import com.tripj.domain.boardcate.repository.BoardCateRepository;
import com.tripj.domain.comment.repository.CommentRepository;
import com.tripj.domain.like.repository.LikedBoardRepository;
import com.tripj.domain.user.model.entity.User;
import com.tripj.domain.user.repository.UserRepository;
import com.tripj.global.code.ErrorCode;
import com.tripj.global.error.exception.ForbiddenException;
import com.tripj.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCateRepository boardCateRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikedBoardRepository likedBoardRepository;

    /**
     * 게시글 등록
     */
    public CreateBoardResponse createBoard(CreateBoardRequest request,
                                           Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_USER));

        BoardCate boardCate = boardCateRepository.findById(request.getBoardCateId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_BOARD_CATE));

        Board savedBoard = boardRepository.save(request.toEntity(user, boardCate));

        return CreateBoardResponse.of(savedBoard.getId());
    }

    /**
     * 게시글 수정
     */
    public CreateBoardResponse updateBoard(
            CreateBoardRequest request, Long boardId, Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_USER));

        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_BOARD));

        BoardCate boardCate = boardCateRepository.findById(request.getBoardCateId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_BOARD_CATE));

        if (board.getUser().getId().equals(userId)) {
            board.updateBoard(request.getTitle(), request.getContent());
        } else {
            // throws
            throw new ForbiddenException("자신의 게시물만 수정 가능합니다.", ErrorCode.E403_FORBIDDEN);
        }

        return CreateBoardResponse.of(board.getId());
    }

    /**
     * 게시물 삭제
     */
    public CreateBoardResponse deleteBoard(Long userId, Long boardId) {

        Board board = boardRepository.findById(boardId)
           .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_BOARD));

        if (board.getUser().getId().equals(userId)) {
            commentRepository.deleteByBoardId(boardId);
            likedBoardRepository.deleteByBoardId(boardId);
            boardRepository.deleteById(board.getId());
        } else {
            throw new ForbiddenException("자신의 게시물만 삭제 가능합니다.", ErrorCode.E403_FORBIDDEN);
        }

        return CreateBoardResponse.of(board.getId());
    }

    /**
     * 게시글 상세조회
     */
    @Transactional(readOnly = true)
    public GetBoardResponse getBoard(Long boardId) {

        boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_BOARD));

        return boardRepository.getBoardDetail(boardId);

        /*return GetBoardResponse.of(
                boardDetail.getUserId(), boardDetail.getUserName(),
                boardDetail.getProfile(), boardDetail.getBoardId(),
                boardDetail.getTitle(), boardDetail.getContent(),
                boardDetail.getCommentCnt(), boardDetail.getLikeCnt());*/
    }


    /**
     * 게시글 댓글 조회
     */
    public GetBoardCommentResponse getBoardComment(Long boardId) {

        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.E404_NOT_EXISTS_BOARD));

        commentRepository.findByBoardId(boardId);
        return null;
    }

    /**
     * 게시글 리스트 조회 (무한스크롤)
     */
    @Transactional(readOnly = true)
    public Slice<GetBoardResponse> getBoardListScroll(GetBoardRequest request, Pageable pageable) {

        return boardRepository.findAllPaging(request.getLastBoardId(), pageable)
                .map(board -> GetBoardResponse.of(
                        board.getUserId(),
                        board.getUserName(),
                        board.getProfile(),
                        board.getBoardId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getBoardCateName(),
                        board.getRegTime(),
                        board.getCommentCnt(),
                        board.getLikeCnt()));
    }

    /**
     * 게시글 전체 리스트 조회
     */
    public List<GetBoardResponse> getBoardList(Long boardCateId) {
        return boardRepository.getBoardList(boardCateId);
    }

    /**
     * 최신글 전체 리스트 조회
     */
    public List<GetBoardResponse> getBoardLatestList() {
        return boardRepository.getBoardLatestList();
    }

    /**
     * 인기글 전체 리스트 조회
     */
    public List<GetBoardResponse> getBoardPopularList() {
        return boardRepository.getBoardPopularList();
    }
}