package com.kushtrimh.tomorr.task;

import com.kushtrimh.tomorr.task.data.TaskData;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class Task<T extends TaskData> {
    private Instant createdAt;
    private Instant reinsertedAt;
    private T data;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getReinsertedAt() {
        return reinsertedAt;
    }

    public void setReinsertedAt(Instant reinsertedAt) {
        this.reinsertedAt = reinsertedAt;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task<?> task = (Task<?>) o;
        return Objects.equals(createdAt, task.createdAt) &&
                Objects.equals(reinsertedAt, task.reinsertedAt) && Objects.equals(data, task.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, reinsertedAt, data);
    }

    @Override
    public String toString() {
        return "Task{" +
                "createdAt=" + createdAt +
                ", reinsertedAt=" + reinsertedAt +
                ", data=" + data +
                '}';
    }
}
