package com.vinners.cube_vishwakarma.core.executors;

import io.reactivex.Scheduler;

public interface PostExecutionThread {
    Scheduler getScheduler();
}