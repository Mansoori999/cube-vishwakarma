package com.vinners.trumanms.core.executors;

import io.reactivex.Scheduler;

public interface PostExecutionThread {
    Scheduler getScheduler();
}