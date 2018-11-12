package com.example.wly.dailyhabit_android.Info;

import java.util.Date;

public class Goal {
    private int goalId;
    private String goalName;
    private Boolean goalStatus;
    private int goalType;
    private String inspiration;
    private String startDate;
    private String endDate;
    private String repeatTime;
    private int recordedTimes;
    private Boolean isReminding;
    private String remindingTime;
    private Boolean isRecoededToday;

    public Goal() {

    }

    public Goal(int goalId, String goalName, Boolean goalStatus, int goalType, String inspiration, String startDate, String endDate,
                String repeatTime, int recordedTimes, Boolean isReminding, String remindingTime, Boolean isRecoededToday) {
        this.goalId = goalId;
        this.goalName = goalName;
        this.goalStatus = goalStatus;
        this.goalType = goalType;
        this.inspiration = inspiration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.repeatTime = repeatTime;
        this.recordedTimes = recordedTimes;
        this.isReminding = isReminding;
        this.remindingTime = remindingTime;
        this.isRecoededToday = isRecoededToday;
    }
    public Goal(int goalId, String goalName, Boolean goalStatus, int goalType, String inspiration, String startDate, String endDate,
                String repeatTime, int recordedTimes, Boolean isReminding, String remindingTime) {
        this(goalId, goalName, goalStatus, goalType, inspiration, startDate, endDate, repeatTime, recordedTimes, isReminding, remindingTime, null);
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Boolean getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(Boolean goalStatus) {
        this.goalStatus = goalStatus;
    }

    public int getGoalType() {
        return goalType;
    }

    public void setGoalType(int goalType) {
        this.goalType = goalType;
    }

    public String getInspiration() {
        return inspiration;
    }

    public void setInspiration(String inspiration) {
        this.inspiration = inspiration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(String repeatTime) {
        this.repeatTime = repeatTime;
    }

    public int getRecordedTimes() {
        return recordedTimes;
    }

    public void setRecordedTimes(int recordedTimes) {
        this.recordedTimes = recordedTimes;
    }

    public Boolean getReminding() {
        return isReminding;
    }

    public void setReminding(Boolean reminding) {
        isReminding = reminding;
    }

    public String getRemindingTime() {
        return remindingTime;
    }

    public void setRemindingTime(String remindingTime) {
        this.remindingTime = remindingTime;
    }

    public Boolean getRecoededToday() {
        return isRecoededToday;
    }

    public void setRecoededToday(Boolean recoededToday) {
        isRecoededToday = recoededToday;
    }
}
