package cc.nimbusk.spring.cycle;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CycleProblemSuccess {

    private final static Map<String, Object> singletonMap = new HashMap<String, Object>();

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        log.info("Test1 after init:[{}]", getBean(Test1.class).getTest2());
        log.info("Test2 after init:[{}]", getBean(Test2.class).getTest1());
    }


    private static <T> T getBean(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        String beanName = clazz.getSimpleName().toLowerCase();

        if (singletonMap.containsKey(beanName)) {
            return (T) singletonMap.get(beanName);
        }

        // 初始化对象，但是未对属性赋值
        Object object = clazz.newInstance();
        singletonMap.put(beanName, object);

        // 通过反射来属性填充
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> filedClazz = field.getType();
            String fieldName = filedClazz.getSimpleName().toLowerCase();
            // 给成员变量赋值 如果 singletonMap 中有半成品就获取缓存内容，否则再通过调用getBean方法递归的创建
            field.set(object, singletonMap.containsKey(fieldName) ? singletonMap.get(fieldName) : getBean(filedClazz));
        }

        return (T) object;


    }


}


@Getter
@Setter
class Test1 {
    private Test2 test2;
}

@Getter
@Setter
class Test2 {
    private Test1 test1;
}
