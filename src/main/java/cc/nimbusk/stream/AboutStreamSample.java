package cc.nimbusk.stream;

import cc.nimbusk.stream.bean.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 一个简单的Stream示例用法比较：
 * 过滤分组一所中学里身高在 160cm 以上的男女同学
 */
@Slf4j
public class AboutStreamSample {


    public static void main(String[] args) {
        List<Student> testList = GenerateTestData.genListData();
        traditionalLoop(testList);
        usingJavaStream(testList);
        usingJavaParallelStream(testList);
    }

    public static void traditionalLoop(List<Student> studentList) {
        log.info("Before traditional loop, studentList: {}", studentList);
        Map<String, List<Student>> studentMap = new HashMap<>();
        for (Student student : studentList) {
            if (student.getHigh() > 160) {
                if (!studentMap.containsKey(student.getSex())) {
                    List<Student> students = new ArrayList<>();
                    students.add(student);
                    studentMap.put(student.getSex(), students);
                } else {
                    studentMap.get(student.getSex()).add(student);
                }
            }
        }
        log.info("After traditional loop, studentMap: {}", studentMap);
    }

    public static void usingJavaStream(List<Student> studentList) {
        log.info("Before using Java stream, studentList: {}", studentList);
        Map<String, List<Student>> studentMap = studentList.stream()
                .filter(student -> student.getHigh() > 160)
                .collect(Collectors.groupingBy(Student::getSex));
        log.info("After using Java stream, studentMap: {}", studentMap);
    }

    public static void usingJavaParallelStream(List<Student> studentList) {
        log.info("Before using Java parallel stream, studentList: {}", studentList);
        Map<String, List<Student>> stuMap = studentList.parallelStream()
                .filter(student -> student.getHigh() > 160)
                .collect(Collectors.groupingBy(Student::getSex));
        log.info("After using Java parallel stream, studentMap: {}", stuMap);
    }

}
