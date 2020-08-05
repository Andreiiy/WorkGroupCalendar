package com.appoftatar.workgroupcalendar.models;

public class ManHoursInformationOfMonth {
    private Integer numberMonth;
    private Integer timeShifts;
    private Integer additionalTime;

    public ManHoursInformationOfMonth() {
    }

    public Integer getNumberMonth() {
        return numberMonth;
    }

    public Integer getTimeShifts() {
        return timeShifts;
    }

    public Integer getAdditionalTime() {
        return additionalTime;
    }

    public void setNumberMonth(Integer numberMonth) {
        this.numberMonth = numberMonth;
    }

    public void setTimeShifts(Integer timeShifts) {
        this.timeShifts = timeShifts;
    }

    public void setAdditionalTime(Integer additionalTime) {
        this.additionalTime = additionalTime;
    }
}
