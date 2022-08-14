package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.log.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    List<PointLog> findAllByMemberId(Long id);
}
