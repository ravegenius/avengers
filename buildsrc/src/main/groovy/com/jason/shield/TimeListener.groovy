package com.jason.shield

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

class TimeListener implements TaskExecutionListener, BuildListener {

    private overTime = 50
    private taskBeforeExecuteTimeInMs
    private taskAfterExecuteTimeInMs
    private times = []

    @Override
    void beforeExecute(Task task) {
        taskBeforeExecuteTimeInMs = System.currentTimeMillis()
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        taskAfterExecuteTimeInMs = System.currentTimeMillis()
        def ms = taskAfterExecuteTimeInMs - taskBeforeExecuteTimeInMs
        times.add([ms, task.path])
        task.project.logger.warn "${task.path} spend ${ms}ms"
    }

    @Override
    void buildFinished(BuildResult result) {
        println ">>>>> Statistical Time End"
        println "Task spend time over " + overTime + "ms:"
        for (time in times) {
            if (time[0] >= overTime) {
                printf "%7sms  %s\n", time
            }
        }
    }

    @Override
    void buildStarted(Gradle gradle) {
        println ">>>>> Statistical Time Start"
    }

    @Override
    void projectsEvaluated(Gradle gradle) {}

    @Override
    void projectsLoaded(Gradle gradle) {}

    @Override
    void settingsEvaluated(Settings settings) {}
}