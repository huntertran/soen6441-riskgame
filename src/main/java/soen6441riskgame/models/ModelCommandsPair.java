package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

public class ModelCommandsPair {
    public String param;
    public String value1;
    public String value2 = "";

    public ModelCommandsPair(String newParam, String newValue1, String newValue2) {
        this.param = newParam.trim();
        this.value1 = newValue1.trim();
        this.value2 = newValue2.trim();
    }

    public ModelCommandsPair(String newParam, String newValue1) {
        this.param = newParam.trim();
        this.value1 = newValue1.trim();
    }

    public String[] toStringArray() {
        List<String> retList = new ArrayList<String>();
        retList.add(this.param);
        if(!this.value1.isEmpty()) {
            retList.add(this.value1);
        }
        if(!this.value2.isEmpty()) {
            retList.add(this.value2);
        }
        return retList.toArray(new String[retList.size()]);
    }
}
