package com.moyeobus.infra.persistence.survey.repository

import com.moyeobus.infra.persistence.survey.dto.SurveyProjection
import com.moyeobus.infra.persistence.survey.entity.SurveyAnswerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SurveyAnswerJpaRepository : JpaRepository<SurveyAnswerEntity, Long> {

    @Query(
        """
    SELECT 
        o.reason AS reason,
        COUNT(a) AS count,
        CONCAT(
            ROUND(
                (COUNT(a) * 100.0) /
                (SELECT COUNT(a2)
                 FROM SurveyAnswerEntity a2
                 WHERE a2.departureLocalGovId = :localGovId
                    OR a2.destinationLocalGovId = :localGovId),
                1
            ),
            '%'
        ) AS ratio
    FROM SurveyAnswerEntity a
    JOIN SurveyOptionEntity o
      ON a.optionId = o.id
    WHERE a.departureLocalGovId = :localGovId
       OR a.destinationLocalGovId = :localGovId
    GROUP BY o.reason
    ORDER BY count DESC
    """
    )
    fun countByLocalGovId(@Param("localGovId") localGovId: Long): List<SurveyProjection>

}