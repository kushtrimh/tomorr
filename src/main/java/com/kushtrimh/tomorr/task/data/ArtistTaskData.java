package com.kushtrimh.tomorr.task.data;

import com.kushtrimh.tomorr.task.TaskType;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ArtistTaskData implements TaskData {
    private String artistId;
    private String nextNode;
    private TaskType taskType;

    public ArtistTaskData() {
    }

    public ArtistTaskData(String artistId, String nextNode, TaskType taskType) {
        this.artistId = artistId;
        this.nextNode = nextNode;
        this.taskType = taskType;
    }

    public static ArtistTaskData fromArtistId(String artistId, TaskType taskType) {
        return new ArtistTaskData(artistId, null, taskType);
    }

    public static ArtistTaskData fromNextNode(String nextNode, TaskType taskType) {
        return new ArtistTaskData(null, nextNode, taskType);
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getNextNode() {
        return nextNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistTaskData that = (ArtistTaskData) o;
        return Objects.equals(artistId, that.artistId) && Objects.equals(nextNode, that.nextNode) && taskType == that.taskType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistId, nextNode, taskType);
    }

    @Override
    public String toString() {
        return "ArtistData{" +
                "artistId='" + artistId + '\'' +
                ", nextNode='" + nextNode + '\'' +
                ", taskType=" + taskType +
                '}';
    }
}
