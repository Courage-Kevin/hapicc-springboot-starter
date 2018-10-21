package com.hapicc.pojo.lombok;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Lombok 常用注解：
 * <p>
 * - 类注解：
 * 1. @ToString：提供 `toString` 方法
 * 2. @EqualsAndHashCode：提供 `equals`，`hashCode`，`canEqual` 方法
 * 3. @Data：提供所有属性的 Getter 和 Setter 方法，以及上两个注解提供的方法
 * 4. @NoArgsConstructor：提供无参构造器
 * 5. @AllArgsConstructor：提供全参构造器
 * 6. @RequiredArgsConstructor：提供必需参数构造器，必须参数包括 final 或 @NonNull 约束的属性
 * 7. @Slf4j：提供一个名为 log 的 Logger 对象
 * 8. @Builder：提供全参构造器，`builder` 方法，以及 Builder 内部类
 * <p>
 * - 属性注解：
 * 1. @Getter：提供 Getter 方法
 * 2. @Setter：提供 Setter 方法
 * <p>
 * - 方法注解：
 * 1. @Synchronized：对象同步锁
 * 2. @SneakyThrows：抛出异常
 * <p>
 * - 变量定义注解：
 * 1. @Cleanup：关闭流
 */

@Slf4j
@Data
@Builder
public class LombokEntity {

    private Long id;
    private String name;
    private String desc;

    public void testLog() {
        log.info(toString());
    }

    public static void main(String[] args) {
        LombokEntity entity = LombokEntity.builder()
                .id(1L)
                .name("lombok")
                .desc("Common Lombok Annotations!")
                .build();
        entity.testLog();
    }
}
