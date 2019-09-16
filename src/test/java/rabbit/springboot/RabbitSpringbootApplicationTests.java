package rabbit.springboot;

import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rabbit.springboot.entity.Order;
import rabbit.springboot.producer.RabbitSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitSpringbootApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitSender rabbitSender;

    //线程不安全类
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testSend1() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("num", "1111");
        properties.put("age", "10");
        properties.put("time1", new Date().getTime());
        properties.put("time2", simpleDateFormat.format(new Date()));
        rabbitSender.send("hello RabbitMq for spring boot", properties);
    }

    @Test
    public void testSend2() throws Exception {
        Order order = new Order();
        order.setId("00007");
        order.setName("name");
        rabbitSender.sendOrder(order);
    }

}
