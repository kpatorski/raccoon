package raccoon.neuralnetwork


import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert

import java.util.function.Consumer

class AssertJson {
    static DefineAssertionPath assertThat(String json) {
        new DefineAssertionPath(JsonPath.parse(json))
    }

    static class DefineAssertionPath {
        private final DocumentContext actualJson

        private DefineAssertionPath(DocumentContext actualJson) {
            this.actualJson = actualJson
        }

        DefineRequirements on(String path) {
            new DefineRequirements(actualJson, actualJson.read(path))
        }
    }

    static class DefineRequirements<T> {
        private final DocumentContext actualJson
        private final T jsonElement

        private DefineRequirements(DocumentContext actualJson, T jsonElement) {
            this.actualJson = actualJson
            this.jsonElement = jsonElement
        }

        DefineAssertionPath satisfies(Consumer<T> requirements) {
            Assertions.assertThat(jsonElement).satisfies(requirements)
            new DefineAssertionPath(actualJson)
        }

        DefineAssertionPath satisfies(Matcher<T> requirements) {
            MatcherAssert.assertThat(jsonElement, requirements)
            new DefineAssertionPath(actualJson)
        }
    }
}
