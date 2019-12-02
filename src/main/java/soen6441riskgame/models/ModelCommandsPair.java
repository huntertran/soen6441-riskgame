package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

/**
 * a pair of command and params
 * 
 * <p>this model only accept a pair of params for each command, which is create a hard code into the
 * source
 */
public class ModelCommandsPair {
    public String param;
    public String value1;
    public String value2 = "";

    /**
     * constructor
     * 
     * @param newParam  command
     * @param newValue1 value 1
     * @param newValue2 value 2
     */
    public ModelCommandsPair(String newParam, String newValue1, String newValue2) {
        this.param = newParam.trim();
        this.value1 = newValue1.trim();
        this.value2 = newValue2.trim();
    }

    /**
     * a second type with only 1 value
     * 
     * @param newParam  command
     * @param newValue1 value 1
     */
    public ModelCommandsPair(String newParam, String newValue1) {
        this.param = newParam.trim();
        this.value1 = newValue1.trim();
    }

    /**
     * convert back to string
     * 
     * @return the original string
     */
    public String[] toStringArray() {
        List<String> retList = new ArrayList<String>();
        retList.add(this.param);
        if (!this.value1.isEmpty()) {
            retList.add(this.value1);
        }
        if (!this.value2.isEmpty()) {
            retList.add(this.value2);
        }
        return retList.toArray(new String[retList.size()]);
    }
}
