package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/4/29.
 */

public class TimeData {


        private String content;
        private String time;

        public TimeData(String time, String content) {
            this.content = content;
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

}
