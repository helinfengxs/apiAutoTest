package entity;

import lombok.Data;

import java.util.List;

/**
 * @author helinfeng
 */
@Data
public class RunResult {
//    public static  long milliseconds;
//    public static  int passCount;
//    public static  int failCount;


    private String params;

    private String className;

    private String url;

    private String description;

    private String spendTime;

    private String status;

    private List<String> log;

    private Object expectedValue;
}
