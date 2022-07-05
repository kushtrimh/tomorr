package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
public class NotificationRetryData implements TaskData {
    private String from;
    private String subject;
    private String templateName;
    private Map<String, Object> contextData = new HashMap<>();
    private List<String> to = new ArrayList<>();

    public NotificationRetryData() {
    }

    public NotificationRetryData(
            String from, String subject, String templateName, Map<String, Object> contextData, List<String> to) {
        this.from = from;
        this.subject = subject;
        this.templateName = templateName;
        this.contextData = contextData;
        this.to = to;
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
        return TaskType.NOTIFICATION_RETRY;
    }

    @Override
    public String toString() {
        return "NotificationRetryData{" +
                "from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", templateName='" + templateName + '\'' +
                ", contextData=" + contextData +
                '}';
    }
}
