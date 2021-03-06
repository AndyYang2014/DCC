package io.wexchain.dcc.marketing.domainservice.impl;

import com.wexmarket.topia.commons.data.page.PageUtils;
import io.wexchain.dcc.marketing.api.model.RewardLogStatisticsInfo;
import io.wexchain.dcc.marketing.api.model.request.GetTotalRewardAmountRequest;
import io.wexchain.dcc.marketing.api.model.request.QueryRewardLogPageRequest;
import io.wexchain.dcc.marketing.domain.Activity;
import io.wexchain.dcc.marketing.domain.RewardLog;
import io.wexchain.dcc.marketing.domainservice.ActivityService;
import io.wexchain.dcc.marketing.domainservice.RewardLogService;
import io.wexchain.dcc.marketing.domainservice.ScenarioService;
import io.wexchain.dcc.marketing.repository.RewardLogRepository;
import io.wexchain.dcc.marketing.repository.query.RewardLogQueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * RewardLogServiceImpl
 *
 * @author zhengpeng
 */
@Service
public class RewardLogServiceImpl implements RewardLogService {

    @Autowired
    private RewardLogRepository rewardLogRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ScenarioService scenarioService;

    @Override
    public Page<RewardLog> queryRewardLogPage(QueryRewardLogPageRequest request) {
        Long activityId = null;
        Long scenarioId = null;
        if (StringUtils.isNotEmpty(request.getActivityCode())) {
            activityId = activityService.getActivityByCode(request.getActivityCode()).getId();
        }
        if (StringUtils.isNotEmpty(request.getScenarioCode())) {
            scenarioId = scenarioService.getScenarioByCode(request.getActivityCode()).getId();
        }

        PageRequest pageRequest = PageUtils.convert(request.getSortPageParam());
        return rewardLogRepository.findAll(
                RewardLogQueryBuilder.query(request.getAddress(), activityId, scenarioId), pageRequest);
    }

    @Override
    public BigDecimal getTotalRewardAmount(GetTotalRewardAmountRequest request) {
        Long activityId = activityService.getActivityByCode(request.getActivityCode()).getId();
        return Optional.ofNullable(rewardLogRepository.findTotalAmount(request.getAddress(), activityId))
                .orElseGet(() -> BigDecimal.ZERO) ;
    }

    @Override
    public RewardLogStatisticsInfo getRewardLogStatisticsInfo(String activityCode) {
        Activity activity = activityService.getActivityByCode(activityCode);
        DateTime from = DateTime.now().minusDays(1).withTimeAtStartOfDay();
        DateTime to = DateTime.now().withTimeAtStartOfDay().minusMillis(1);
        RewardLogStatisticsInfo info = new RewardLogStatisticsInfo();
        info.setActivityCode(activity.getCode());
        info.setTotalAmount(Optional.ofNullable(
                rewardLogRepository.findTotalAmount(activity.getId())).orElse(BigDecimal.ZERO));
        info.setYesterdayAmount(Optional.ofNullable(
                rewardLogRepository.findTotalAmountBetweenDate(
                activity.getId(), from.toDate(), to.toDate())).orElse(BigDecimal.ZERO));
        info.setYesterdayPersonNumber(rewardLogRepository.countByActivityIdAndCreatedTimeBetween(
                activity.getId(), from.toDate(), to.toDate()));
        return info;
    }
}
