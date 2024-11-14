package cc.nimbusk.stream;

import cc.nimbusk.stream.bean.Student;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateTestData {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static List<Student> genListData() {
        List<Student> list1 = new ArrayList<Student>();
        for (int i = 0; i < 30; i++) {
            Student student1 = new Student();
            student1.setId((long) (i + 1));
            student1.setName("name" + i);
            student1.setAge(i);
            if (i % 2 == 0) {
                student1.setSex("male");
            } else {
                student1.setSex("female");
            }
            student1.setHigh((secureRandom.nextInt(8) + 15) * 10);
            student1.setAge(secureRandom.nextInt(10) + 18);
            list1.add(student1);
        }
        return list1;
    }

}
