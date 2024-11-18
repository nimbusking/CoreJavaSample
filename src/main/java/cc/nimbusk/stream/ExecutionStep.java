package cc.nimbusk.stream;

import cc.nimbusk.stream.bean.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 一个debug程序来剖析一下一个Stream执行的整体流程
 * @author nimbusk
 */
@Slf4j
public class ExecutionStep {

    public static void main(String[] args) {
        List<Student> students = GenerateTestData.genListData();
        log.info("Before test students list:{}", students);
        //
        students.stream()
                .filter(student -> student.getHigh() > 160)
                .mapToInt(Student::getAge)
                .max()
                .ifPresent(System.out::println);


        // 配套生成
        List<Long> idList = Arrays.asList(10L, 22L, 22L, 33L, 1L, 4L, 10L, 4L, 100L, 200L, 300L, 102L, 20L, 30L, 11L);
        List<Student> debugList = idList.stream()
                .distinct()
                .map(id -> new Student(id, "name" + id, null, 20, 0))
                .filter(student -> student.getId() > 10)
                .filter(student -> student.getId() < 100)
                .sorted(Comparator.comparing(Student::getId))
                .collect(Collectors.toList());
        log.info("After test students list:{}", debugList);
    }

}
