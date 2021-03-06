package io.wexchain.dcc.marketing.domainservice;

import io.wexchain.dcc.marketing.api.model.EcoRewardRankVo;
import io.wexchain.dcc.marketing.api.model.EcoRewardStatisticsInfo;
import io.wexchain.dcc.marketing.domain.RewardRound;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RewardRoundService {

    RewardRound createRewardRound(Date bonusDay);

    Optional<RewardRound> findRewardRoundByBonusDay(Date bonusDay);

    EcoRewardStatisticsInfo getEcoRewardStatisticsInfo(String address);

    List<EcoRewardRankVo> queryEcoRewardRankList(DateTime roundTime);

}
