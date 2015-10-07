package itba.undiaparadar.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import itba.undiaparadar.model.PositiveAction;

/**
 * Created by mpurita on 9/26/15.
 */
public class PositiveActionSerializer implements JsonDeserializer<List<PositiveAction>> {

    @Override
    public List<PositiveAction> deserialize(final JsonElement json, final Type typeOfT,
                                            final JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        } else {
            final JsonElement response = json.getAsJsonObject().get("response");
            final JsonArray docs = response.getAsJsonObject().getAsJsonArray("docs");

            final List<PositiveAction> positiveActions = new ArrayList<>();
            for (final JsonElement positiveAction : docs) {
                positiveActions.add((PositiveAction) context.deserialize(positiveAction, PositiveAction.class));
            }

            return positiveActions;
        }
    }
}
