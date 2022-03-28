package raccoon.shared.result

import spock.lang.Specification

class ResultTest extends Specification {
    def "should be an empty success"() {
        given:
        def result = Result.emptySuccess()

        expect:
        result.isSuccess()
        !result.isFailure()
    }

    def "should be success"() {
        given:
        def result = Result.success("any success")

        expect:
        result.isSuccess()
        result.success().get() == "any success"
        result.onSuccess() { success -> success == "any success" }
        result.onFailure { failure -> assert false }
    }

    def "should be failure"() {
        given:
        def result = Result.failure("any failure")

        expect:
        result.isFailure()
        result.failure().get() == "any failure"
        result.onFailure { failure -> assert failure == "any failure" }
        result.onSuccess { success -> assert false }
    }
}
