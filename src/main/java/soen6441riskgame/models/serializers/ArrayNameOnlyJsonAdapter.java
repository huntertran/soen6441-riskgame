package soen6441riskgame.models.serializers;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ArrayNameOnlyJsonAdapter extends TypeAdapter<ArrayList<NameOnlySerializable>> {

    @Override
    public void write(JsonWriter out, ArrayList<NameOnlySerializable> value) throws IOException {
        Gson gson =  new Gson();

        out.beginObject();

        out.name("test");
        out.value(gson.toJson(value));

        out.endObject();
    }

    @Override
    public ArrayList<NameOnlySerializable> read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}