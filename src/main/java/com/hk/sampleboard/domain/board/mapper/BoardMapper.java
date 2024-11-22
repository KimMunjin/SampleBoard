package com.hk.sampleboard.domain.board.mapper;

import com.hk.sampleboard.domain.board.vo.Board;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    Long insertBoard(Board board);
}
