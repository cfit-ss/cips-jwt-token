package com.cips.data.Service;

import com.cips.data.Entity.Task;
import org.quartz.SchedulerException;

import java.util.List;

public interface QuertzTaskService {

    List<Task> queryQuertzTask();

    void pauseTaskById(String id) throws SchedulerException;

    void resumeTaskById(String id) throws SchedulerException;

}
