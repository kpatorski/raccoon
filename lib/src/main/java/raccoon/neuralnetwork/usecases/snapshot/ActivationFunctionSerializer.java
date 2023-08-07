package raccoon.neuralnetwork.usecases.snapshot;

import com.google.gson.*;
import raccoon.neuralnetwork.core.activationfunction.ActivationFunction;
import raccoon.neuralnetwork.core.activationfunction.ActivationFunctions;
import raccoon.neuralnetwork.core.activationfunction.FunctionId;

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
