package util.service.imp;

import util.service.CustomUtil;

/**
 * @author helinfeng
 */
public class CustomUtilsImpl implements CustomUtil {

    private final static String[] TELFIRST = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153,185,186".split(",");
    private static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
    @Override
    public String getPhone() {
        int index=getNum(0,TELFIRST.length-1);
        String first=TELFIRST[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }
}
