package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

public class ModelCommandsPair {
    public String param;
    public String value1;
    public String value2;

    public ModelCommandsPair(String new_param, String new_value1, String new_value2) {
        this.param = new_param.trim();
        this.value1 = new_value1.trim();
        this.value2 = new_value2.trim();
    }

    public ModelCommandsPair(String new_param, String new_value1) {
        this.param = new_param.trim();
        this.value1 = new_value1.trim();
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