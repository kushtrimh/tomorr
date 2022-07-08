package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Kushtrim Hajrizi
 */
public class NotificationRetryData implements TaskData {
    private String from;
    private String subject;
    private String templateName;
    private Map<String, Object> contextData = new HashMap<>();
    private List<String> to = new ArrayList<>();
    private TaskType taskType = TaskType.NOTIFICATION_RETRY;
    private int delay;
    private TimeUnit delayTimeUnit;

    public NotificationRetryData() {
    }

    public NotificationRetryData(
            String from, String subject, String templateName, Map<String, Object> contextData,
            List<String> to, int delay, TimeUnit delayTimeUnit) {
        this.from = from;
        this.subject = subject;
        this.templateName = templateName;
        this.contextData = contextData;
        this.to = to;
        this.delay = delay;
        this.delayTimeUnit = delayTimeUnit;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getContextData() {
        return contextData;
    }

    public void setContextData(Map<String, Object> contextData) {
        this.contextData = contextData;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public TimeUnit getDelayTimeUnit() {
        return delayTimeUnit;
    }

    public void setDelayTimeUnit(TimeUnit delayTimeUnit) {
        this.delayTimeUnit = delayTimeUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRetryData that = (NotificationRetryData) o;
        return Objects.equals(subject, that.subject) && Objects.equals(templateName, that.templateName) && Objects.equals(contextData, that.contextData) && taskType == that.taskType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, templateName, contextData, taskType);
    }

    @Override
    public String toString() {
        return "NotificationRetryData{" +
                "from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", templateName='" + templateName + '\'' +
                ", contextData=" + contextData +
                ", taskType=" + taskType +
                ", delay=" + delay +
                ", delayTimeUnit=" + delayTimeUnit +
                '}';
    }
}
