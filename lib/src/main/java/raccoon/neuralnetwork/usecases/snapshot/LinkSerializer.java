package raccoon.neuralnetwork.usecases.snapshot;

import com.google.gson.*;
import raccoon.neuralnetwork.core.Link;

import java.lang.reflect.Type;

class LinkSerializer implements JsonSerializer<Link>, JsonDeserializer<Link> {
    @Override
    public JsonElement serialize(Link link, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(link.weight().value());
    }

    @Override
    public Link deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        Link.Weight weight = new Link.Weight(json.getAsDouble());
        return Link.ofWeight(weight);
    }
}
