package com.vinners.cube_vishwakarma.core.executors

import java.util.concurrent.*

class ThreadExecutorImpl constructor(
    private val threadFactory: ThreadFactory = JobThreadFactory(),
    private val workQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(),
    private val threadPoolExecutor: ThreadPoolExecutor =
        ThreadPoolExecutor(
            INITIAL_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME.toLong(),
            KEEP_ALIVE_TIME_UNIT,
            workQueue,
            threadFactory
        )
) : ThreadExecutor {

    override fun execute(runnable: Runnable?) {
        runnable?.let { threadPoolExecutor.execute(runnable) }
            ?: run {
                throw IllegalArgumentException("Runnable to syncPaymentsToServer cannot be null")
            }
    }

    private class JobThreadFactory : ThreadFactory {
        private var counter = 0

        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable, THREAD_NAME + counter++)
        }

        companion object {
            private const val THREAD_NAME = "usha_sis_"
        }
    }

    companion object {

        private const val INITIAL_POOL_SIZE = 3 // TODO: Get number of CPU cores
        private const val MAX_POOL_SIZE = 5 // TODO: Get number of CPU cores

        // Sets the newPurchaseAmount of date an idle thread waits before terminating
        private const val KEEP_ALIVE_TIME = 10

        // Sets the Time Unit to seconds
        private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
    }
}