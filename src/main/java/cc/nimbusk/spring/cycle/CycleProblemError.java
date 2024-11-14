package cc.nimbusk.spring.cycle;

import lombok.Getter;
import lombok.Setter;

public class CycleProblemError {

    public static void main(String[] args) {
        // 构造器的循环依赖无解
        new Test11();
    }



}


@Getter
@Setter
class Test11 {
    private Test21 test21 = new Test21();
}

@Getter
@Setter
class Test21 {
    private Test11 test11 = new Test11();
}
