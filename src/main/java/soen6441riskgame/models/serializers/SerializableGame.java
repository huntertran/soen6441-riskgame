package soen6441riskgame.models.serializers;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;

public class SerializableGame {

    @Expose
    private ArrayList<Country> countries;

    public String serialize() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                                     .serializeNulls()
                                     .excludeFieldsWithoutExposeAnnotation()
                                     .registerTypeAdapter(Continent.class, new NameOnlyJsonAdapter())
                                     .create();

        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static class Builder {
        private ArrayList<Country> countries;

        public Builder withCountries(ArrayList<Country> countries) {
            this.countries = countries;
            return this;
        }

        public SerializableGame build() {
            SerializableGame serializableGame = new SerializableGame();
            serializableGame.countries = this.countries;
            return serializableGame;
        }
    }
}