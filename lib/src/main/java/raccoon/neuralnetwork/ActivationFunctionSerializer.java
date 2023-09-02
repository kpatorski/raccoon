package raccoon.neuralnetwork;

import com.google.gson.*;
import raccoon.neuralnetwork.activationfunction.ActivationFunction;
import raccoon.neuralnetwork.activationfunction.ActivationFunctions;
import raccoon.neuralnetwork.activationfunction.FunctionId;

import java.lang.reflect.Type;

class ActivationFunctionSerializer implements JsonSerializer<ActivationFunction>, JsonDeserializer<ActivationFunction> {
    @Override
    public JsonElement serialize(ActivationFunction function, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(function.id().name());
    }

    @Override
    public ActivationFunction deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        FunctionId functionId = FunctionId.valueOf(json.getAsString());
        return ActivationFunctions.of(functionId);
    }
}
