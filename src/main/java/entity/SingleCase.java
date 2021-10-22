package entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author helinfeng
 */
@Data
public class SingleCase {
    private String url;
    private String caseName;
    private String method;
    private Map<String,String> headers;
    private Map<String,Object> params;
    private HashMap<String,String> extract;
    private String assertFilePath;
    private String className;
}

