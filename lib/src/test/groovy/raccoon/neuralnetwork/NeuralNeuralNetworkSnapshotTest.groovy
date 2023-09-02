package raccoon.neuralnetwork

import spock.lang.Specification

import static AssertJson.assertThat
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.equalTo

class NeuralNeuralNetworkSnapshotTest extends Specification {
    /**
     * <img src="./network-hidden-layer.png" width=483/>
     */
    def "neural network is restored from and stored as a snapshot"() {
        given:
        def snapshot = """
            {
              "inputLayer": {
                "inputs": [
                  {
                    "outgoingLinks": [
                      0.1,
                      0.2,
                      0.3
                    ]
                  },
                  {
                    "outgoingLinks": [
                      0.4,
                      0.5,
                      0.6
                    ]
                  }
                ]
              },
              "neuronsLayers": {
                "layers": [
                  {
                    "neurons": [
                      {
                        "activationFunction": "LINEAR",
                        "outgoingLinks": [
                          0.1,
                          0.2
                        ],
                        "incomingLinks": [
                          0.1,
                          0.4
                        ]
                      },
                      {
                        "activationFunction": "LINEAR",
                        "outgoingLinks": [
                          0.1,
                          0.2
                        ],
                        "incomingLinks": [
                          0.2,
                          0.5
                        ]
                      },
                      {
                        "activationFunction": "LINEAR",
                        "outgoingLinks": [
                          0.3,
                          0.4
                        ],
                        "incomingLinks": [
                          0.3,
                          0.6
                        ]
                      }
                    ]
                  }
                ]
              },
              "outputLayer": {
                "outputs": [
                  {
                    "activationFunction": "LINEAR",
                    "incomingLinks": [
                      0.1,
                      0.1,
                      0.3
                    ]
                  },
                  {
                    "activationFunction": "LINEAR",
                    "incomingLinks": [
                      0.2,
                      0.2,
                      0.4
                    ]
                  }
                ]
              }
            }
        """

        and:
        def network = RestoreSnapshot.from(snapshot)

        expect:
        assertThat(TakeSnapshot.of(network))
                .on('$.inputLayer.inputs[0].outgoingLinks[*]').satisfies(containsInAnyOrder(0.1d, 0.2d, 0.3d))
                .on('$.inputLayer.inputs[1].outgoingLinks[*]').satisfies(containsInAnyOrder(0.4d, 0.5d, 0.6d))

                .on('$.neuronsLayers.layers[0].neurons[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[0].neurons[0].outgoingLinks[*]').satisfies(containsInAnyOrder(0.1d, 0.2d))
                .on('$.neuronsLayers.layers[0].neurons[0].incomingLinks[*]').satisfies(containsInAnyOrder(0.1d, 0.4d))

                .on('$.neuronsLayers.layers[0].neurons[1].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[0].neurons[1].outgoingLinks[*]').satisfies(containsInAnyOrder(0.1d, 0.2d))
                .on('$.neuronsLayers.layers[0].neurons[1].incomingLinks[*]').satisfies(containsInAnyOrder(0.2d, 0.5d))

                .on('$.neuronsLayers.layers[0].neurons[2].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[0].neurons[2].outgoingLinks[*]').satisfies(containsInAnyOrder(0.3d, 0.4d))
                .on('$.neuronsLayers.layers[0].neurons[2].incomingLinks[*]').satisfies(containsInAnyOrder(0.3d, 0.6d))

                .on('$.outputLayer.outputs[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.outputLayer.outputs[0].incomingLinks[*]').satisfies(containsInAnyOrder(0.1d, 0.1d, 0.3d))
                .on('$.outputLayer.outputs[1].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.outputLayer.outputs[1].incomingLinks[*]').satisfies(containsInAnyOrder(0.2d, 0.2d, 0.4d))
    }
}
