package entity;

import lombok.Data;

import java.util.Map;

/**
 * @author helinfeng
 */
@Data
public class SingleCommonCase {
    public String url;
    public String method;
    public Map<String,String> headers;
    public Map<String,Object> params;
}
