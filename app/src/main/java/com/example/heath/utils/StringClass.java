package com.example.heath.utils;



/**
 * Created by lenovo on 2018/6/15.
 */

public class StringClass {
    public static String Heart_rate_high = "心率过速可以通过以下方法来预防与调整，" +
            "在座位上，上身正直，头颈部固定不动，将眼球先尽量向左看，然后尽量向右看，每分钟可转换30次，共转动2—3分钟；" +
            "然后双眼视力集中，注视自己的鼻尖1分钟。假如心动过速仍不能控制，可行重复作2--3次。饭菜尽可能清淡，多吃维生素丰富的新鲜蔬菜和水果" +
            "少吃甜食，肥腻食物，和刺激神经兴奋的食物。";
    public static String Heart_rate_low = "心率过低可以通过以下方法来调整，平时可服用益气养心的药膳,如人参粥,大枣粥,莲子粥等.厚腻,炙燥,辛辣,生冷食物都" +
            "应慎食或节食,且不可过饥,过饱.并要做到合理营养,平衡饮食.清淡饮食是一种少盐,少油,少动物性食物的饮食.心脏病人由于胃肠道容易瘀血水肿," +
            "影响消化吸收,故应食用易消化的食物,心功能不好者应少吃多餐,少吃油煎食物.为了增强心脏功能,必需的热量供应要保证,还须注意食物中蛋白质," +
            "维生素 B 族,C 和重要无机盐 的供给。";
    public static String Heart_rate_normal = "您的心率趋于正常范围,请继续保持良好的生活习惯";

    public static String Blood_pressure_high = "血压偏高可以通过生活方式干预来使血压降低，饮食上要注意低盐，" +
            "饭菜尽可能清淡，不吃咸" +
            "菜，豆腐乳，各种腌制品等食盐含量过高的食物。要规律作息，" +
            "保持心情平和，不要过度劳累和熬夜，避免情绪激动，适当运动锻炼，控制体重，戒烟戒酒。";
    public static String Blood_pressure_low = "血压偏低可以通过生活方式干预来使血压升高，可以通过补充营养" +
            "比如牛奶，牛羊肉，黑木耳，花生米，红小豆等调理，多吃瘦肉，鸡蛋，牛奶等精蛋白饮食，吃盐口味偏重一点，" +
            "多喝水补充血容量，多活动增加心脏的循环血量，规律作息。";
    public static String Blood_pressure_normal = "您的血压趋于正常范围,请继续保持良好的生活习惯";

    public static String Air_quality_bad = "车内空气质量差，如果条件允许，建议打开车窗通风。";
    public static String Air_quality_great = "车内空气质量良好";

    public static String Body_weight_high = "肾得利牌减肥药，一粒就见效";
    public static String Body_weight_low = "体重低于标准值可以通过改变食法来调整，以下是如何能在不影响健康的前提下增加体重的方法" +
            "，少吃多餐，而不是增加每餐的饭量，选择富维他命，矿物质及卡路里的食物，吃想吃的东西，不要只吃甜品，应多吃些普通的食物，如芝麻饼，" +
            "乳酪，葡萄干面包等，放松心情，别让自己过度紧张。";
    public static String Body_weight_normal = "您的体重趋于正常范围,请继续保持良好的生活习惯";

    public static String Oxygen_high = "血氧饱和度偏高，血粘度高，夏天眼睑肿，有头晕表现，建议到医院检查是否有高血压或糖尿病及贫血的可能。";
    public static String Oxygen_low = "血氧饱和度低可见于贫血，肺功能障碍，通气功能障碍等，建议去医院呼吸科检查明确，平时多吃红枣，枸杞等补血的食物，" +
            "适当输氧等，保持环境通风。";
    public static String Oxygen_normal = "您的血氧饱和度趋于正常范围,请继续保持良好的生活习惯";

    public static String Body_temperature_high = "您的体温高于正常范围,有发烧的症状,如身体感觉不适,强烈建议您及时就医!";
    public static String Body_temperature_low = "您的体温低于正常范围,请继续保持良好的生活习惯";
    public static String Body_temperature_normal = "您的体温低于正常范围,强烈建议您及时就医,不要继续驾驶,核心体温降低可导致冷漠嗜睡，手脚笨拙，精神错乱，易激动，虚幻，呼吸减慢或停止，心跳减慢，不规则等!";

    public static String Health = "您今天元气满满,真是愉快健康的一天.您本次测试身体状况良好，希望您继续保持良好的生活习惯，祝您身体健康心情愉快。";
    public static String Sub_health = "您的身体存在些许异常，我们为您准备了一些建议,请您及时查看下面的反馈信息,以及您的周报信息.";
    public static String Unhealth = "您的身体状况很差，您目前不太适合长时间驾驶,希望您及时去医院做更加详细的体检,及时获的良好的治疗.";


    public static String xinlv(int data) {
        String string = null;
        if (data > 140) {
            string = Heart_rate_high;
        }
        if (data < 60) {
            string = Heart_rate_low;
        }
        if (data >= 60 && data <= 120)
            string = Heart_rate_normal;
        return string;
    }

    public static String jiankang(String data) {
        if (data.contains("不健康"))
            return  Unhealth;
        if (data.contains("亚健康"))
            return Sub_health;
        if (data.contains("健康")) {
            return Health;
        }



        return null;
    }

    public static String xueyang(int data) {
        String string=null;
        if (data>=98){
            string=Oxygen_high;
        }
        if (data>=95&&data<98)
            string=Oxygen_normal;
        if (data<95)
            string=Oxygen_low;
        return string;
    }
    public static String tiwenjianyi(float data){
        String string=null;
        if (data>=38){
            string=Body_temperature_high;

        }
        if (data>35&&data<38)
            string=Body_temperature_normal;
        if (data<=35){
            string=Body_temperature_low;
        }

        return string;
    }


}
