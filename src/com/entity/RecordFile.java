package com.entity;

public class RecordFile implements Comparable{
    private String recordId;
    private int patientId;
    private String creatTime;

    public RecordFile(String recordId, int patientId, String creatTime) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.creatTime = creatTime;
    }

    @Override
    public int compareTo(Object o){
        RecordFile recordFile = (RecordFile) o;
        int oTag = Integer.parseInt(recordFile.recordId.split("\\.")[0].split("-")[1]);
        int thisTag = Integer.parseInt(this.recordId.split("\\.")[0].split("-")[1]);
        if(thisTag > oTag){
            return 1;
        }else if(oTag > thisTag){
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "RecordFile{" +
                "recordId='" + recordId + '\'' +
                ", patientId=" + patientId +
                ", creatTime='" + creatTime + '\'' +
                '}';
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }
}
