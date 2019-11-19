package soen6441riskgame.models.serializers;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.NameOnlySerializable;
import soen6441riskgame.singleton.GameBoard;

public class NameOnlyJsonAdapter extends TypeAdapter<NameOnlySerializable> {

    @Override
    public void write(JsonWriter out, NameOnlySerializable value) throws IOException {
        out.beginObject();

        out.name(value.getPropertyName());
        out.value(value.getPropertyValue());

        out.endObject();
    }

    @Override
    public Continent read(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        String name = in.nextString();

        ArrayList<Continent> continents = GameBoard.getInstance().getGameBoardMap().getContinents();
        for (Continent continent : continents) {
            if (continent.getName().equals(name)) {
                return continent;
            }
        }

        return null;
    }

}