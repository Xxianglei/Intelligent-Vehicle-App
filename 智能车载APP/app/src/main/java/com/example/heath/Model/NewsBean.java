package com.example.heath.Model;

import java.util.List;

/**
 * Created by 丽丽超可爱 on 2018/5/11.
 */

/**
 * {
 "status":"0",
 "msg":"ok",
 "result":{
 "channel":"头条",
 "num":"10",
 "list":[
 {
 "title":"中国开闸放水27天解救越南旱灾",
 "time":"2016-03-16 07:23",
 "pic":"http://api.jisuapi.com/news/upload/20160316/105123_31442.jpg",
 "content":"原标题：防总：应越南请求 中方启动澜沧江水电站水量应急调度",
 "url":"http://mil.sina.cn/zgjq/2016-03-16/detail-ifxqhmve9235380.d.html?vt=4&pos=108",
 }
 ]
 }
 }
 */
public class NewsBean {
    //注意变量名与字段名一致
    private int status;
    private String msg;
    private ResultBean ResultBean;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NewsBean.ResultBean getResultBean() {
        return ResultBean;
    }

    public void setResultBean(NewsBean.ResultBean resultBean) {
        ResultBean = resultBean;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", ResultBean=" + ResultBean +
                '}';
    }

    public class ResultBean{
        private String channel ;
        private String num;
        private String phone;
        List<NewBean> news;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<NewBean> getNews() {
            return news;
        }

        public void setNews(List<NewBean> news) {
            this.news = news;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "channel='" + channel + '\'' +
                    ", num='" + num + '\'' +
                    ", phone='" + phone + '\'' +
                    ", news=" + news +
                    '}';
        }
    }
    public class NewBean {
        private String title;
        private String time;
        private String pic;
        private String content;
        private String picurl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        @Override
        public String toString() {
            return "NewBean{" +
                    "title='" + title + '\'' +
                    ", time='" + time + '\'' +
                    ", pic='" + pic + '\'' +
                    ", content='" + content + '\'' +
                    ", picurl='" + picurl + '\'' +
                    '}';
        }
    }
}
