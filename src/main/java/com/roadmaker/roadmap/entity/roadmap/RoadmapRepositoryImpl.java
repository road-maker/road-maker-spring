package com.roadmaker.roadmap.entity.roadmap;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roadmaker.commons.exception.NotFoundException;
import com.roadmaker.roadmap.dto.RoadmapDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.roadmaker.roadmap.entity.roadmap.QRoadmap.roadmap;

@Slf4j @Repository
public class RoadmapRepositoryImpl extends QuerydslRepositorySupport implements RoadmapRepositoryCustom{
    @Autowired
    private JPAQueryFactory queryFactory;

    public RoadmapRepositoryImpl() {
        super(Roadmap.class);
    }

    @Override
    public List<RoadmapDto> findyBySearchOption(PageRequest pageRequest, String keyword) {

        JPQLQuery<Roadmap> query = queryFactory.selectFrom(roadmap)
                .where(eqKeyword(keyword));
//
//        log.info("query: {}", query);
//
//        List<Roadmap> roadmaps = this.getQuerydsl().applyPagination(pageRequest, query).fetch();
//
//        log.info("roadmaps: {}", roadmaps);
//
//        List<RoadmapDto> roadmapDtos = new ArrayList<>();
//        roadmaps.forEach(roadmap1 ->  {
//            RoadmapDto roadmapDto = RoadmapDto.of(roadmap1, roadmap1.getMember());
//            roadmapDtos.add(roadmapDto);
//        });
//
//        log.info("roadmapDtos: {}", roadmapDtos);
//
//        Page<RoadmapDto> page = new PageImpl<>(roadmapDtos, pageRequest, query.fetchCount());
//
//        log.info("page: {}", page);
//
//        return page.getContent();

        List<Roadmap> roadmaps = Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageRequest, query).fetch(); //

        log.debug("roadmaps: {}",roadmaps);

        List<RoadmapDto> roadmapDtos = new ArrayList<>();
        roadmaps.forEach(roadmap1 ->
            {RoadmapDto roadmapDto = RoadmapDto.of(roadmap1, roadmap1.getMember());
                    roadmapDtos.add(roadmapDto);
        });
        return new PageImpl<RoadmapDto>(roadmapDtos, pageRequest, query.fetchCount()).getContent();
    }

    private BooleanExpression eqKeyword(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return null;
        }
        return roadmap.title.containsIgnoreCase(keyword);
    }
}
