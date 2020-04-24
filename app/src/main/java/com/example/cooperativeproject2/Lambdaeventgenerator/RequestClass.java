package com.example.cooperativeproject2.Lambdaeventgenerator;

public class RequestClass {

        String taskName; //일정이름
        int date;  //일정날짜
        int time; //일정시간
        String startLocation; //출발지
        String endLocation; //도착지
        int alarmTime; //알람설정시간

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
        public String getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(String startLocation) {
            this.startLocation = startLocation;
        }
        public String getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(String endLocation) {
            this.endLocation = endLocation;
        }
        public int getAlarmTime() {
            return alarmTime;
        }

        public void setAlarmTime(int alarmTime) {
            this.alarmTime = alarmTime;
        }

        public RequestClass(String taskName, int date, int time, String startLocation, String endLocation, int alarmTime) {
            this.taskName = taskName;
            this.date = date;
            this.time = time;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.alarmTime = alarmTime;
        }

        public RequestClass() {
        }
    }

