package io.wexchain.dcc.marketing.domainservice;

import io.wexchain.dcc.marketing.api.model.request.QueryActivityRequest;
import io.wexchain.dcc.marketing.domain.Activity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ActivityService {

    Activity getActivityById(Long id);

    Activity getActivityByCode(String code);

    List<Activity> queryActivity(QueryActivityRequest request);
}
