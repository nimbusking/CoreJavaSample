package cc.nimbusk.stream;

import cc.nimbusk.stream.bean.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

    }

}
