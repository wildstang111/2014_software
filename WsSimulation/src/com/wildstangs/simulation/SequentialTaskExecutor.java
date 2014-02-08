package com.wildstangs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Nathan
 */
public class SequentialTaskExecutor {

    public interface SequentialTaskExecutorCompletionListener {

        public void executionCompleted(SequentialTaskExecutor executor);
    }

    private SequentialTaskExecutorCompletionListener listener;

    private List<TimerTask> tasks;
    private List<Long> durations;
    private List<Long> periods;
    private List<Boolean> runOnce;
    private Timer timer;
    private int currentTaskIndex = 0;
    private long currentTaskStartTime;
    private boolean currentTaskStarted = false;
    private boolean running;

    public SequentialTaskExecutor() {
        tasks = new ArrayList<TimerTask>();
        durations = new ArrayList<Long>();
        periods = new ArrayList<Long>();
        runOnce = new ArrayList<Boolean>();
        timer = new Timer();
    }

    // This listener will be notified when we have finished executing all the tasks
    public void setCompletionListener(SequentialTaskExecutorCompletionListener listener) {
        this.listener = listener;
    }

    // Used for tasks that should be run for a certain duration at certain intervals
    public void addTask(TimerTask task, long duration, long period) {
        tasks.add(task);
        durations.add(new Long(duration));
        periods.add(new Long(period));
        runOnce.add(false);
    }
    
    // Used for tasks that should run once and then continue to the next task
    public void addTask(TimerTask task){
        tasks.add(task);
        durations.add(new Long(0));
        periods.add(new Long(0));
        runOnce.add(true);
    }

    public void executeScheduledTasks() {
        running = true;
        Thread t = new Thread() {
            @Override
            public void run() {
                while (running == true) {
                    // Start the current task if it hasn't been started yet
                    if (!currentTaskStarted) {
                        if (runOnce.get(currentTaskIndex) == true) {
                            // Schedule run-once tasks to run once
                            System.out.println("run-once task started: " + tasks.get(currentTaskIndex).getClass().getName());
                            timer.schedule(tasks.get(currentTaskIndex), 0);
                        } else {
                            // Schedule periodic tasks to run periodically
                            System.out.println("periodic task started: " + tasks.get(currentTaskIndex).getClass().getName());
                            timer.scheduleAtFixedRate(tasks.get(currentTaskIndex), 0, periods.get(currentTaskIndex));
                            currentTaskStartTime = System.currentTimeMillis();
                            currentTaskStarted = true;
                        }
                    }

                    // Check if the task has executed for the desired duration
                    if (runOnce.get(currentTaskIndex) == true) {
                        currentTaskIndex++;
                        currentTaskStarted = false;
                    } else {
                        if (System.currentTimeMillis() > currentTaskStartTime + durations.get(currentTaskIndex)) {
                            // Stop the task and advance to the next one if we should
                            System.out.println("Stopping task: " + tasks.get(currentTaskIndex).getClass().getName());
                            tasks.get(currentTaskIndex).cancel();
                            currentTaskIndex++;
                            currentTaskStarted = false;
                        }
                    }
                    // If we've reached the end of our list of tasks, stop execution and 
                    // notify the completion listener
                    if (currentTaskIndex >= tasks.size()) {
                        // End execution
                        running = false;
                        if (listener != null) {
                            listener.executionCompleted(SequentialTaskExecutor.this);
                        }
                        return;
                    }
                }
            }
        };
        t.start();
    }

    public void cancelExecution() {
        System.out.println("Execution cancelled");
        running = false;
        tasks.get(currentTaskIndex).cancel();
        currentTaskIndex = 0;
    }
}
