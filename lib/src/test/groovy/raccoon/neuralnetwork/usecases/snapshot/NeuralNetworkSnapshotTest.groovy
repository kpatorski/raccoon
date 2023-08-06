package raccoon.neuralnetwork.usecases.snapshot

import spock.lang.Specification

import static org.hamcrest.Matchers.containsInAnyOrder
import static raccoon.neuralnetwork.AssertJson.assertThat

class NeuralNetworkSnapshotTest extends Specification {
    def takeSnapshot = new TakeSnapshot()
    def restoreSnapshot = new RestoreSnapshot()

    def "neural network is restored from and stored as a snapshot"() {
        given:
        def snapshot = """
            {
              "inputLayer": {
                "inputs": [
                  {
                    "outgoingLinks": [
                      1.1,
                      1.2
                    ]
                  },
                  {
                    "outgoingLinks": [
                      2.1,
                      2.2
                    ]
                  }
                ]
              },
              "neuronsLayers": {
                "layers": [
                  {
                    "neurons": [
                      {
                        "outgoingLinks": [
                          3.1,
                          3.2
                        ],
                        "incomingLinks": [
                          4.1,
                          4.2
                        ]
                      },
                      {
                        "outgoingLinks": [
                          5.1
                        ],
                        "incomingLinks": [
                          5.1
                        ]
                      }
                    ]
                  },
                  {
                    "neurons": [
                      {
                        "outgoingLinks": [
                          6.1,
                          6.2
                        ],
                        "incomingLinks": [
                          7.1,
                          7.2
                        ]
                      },
                      {
                        "outgoingLinks": [
                          8.1,
                          8.2
                        ],
                        "incomingLinks": [
                          9.1,
                          9.2
                        ]
                      },
                      {
                        "outgoingLinks": [
                          10.1,
                          10.2
                        ],
                        "incomingLinks": [
                          11.1,
                          11.2
                        ]
                      }
                    ]
                  }
                ]
              },
              "outputLayer": {
                "outputs": [
                  {
                    "incomingLinks": [
                      12.1,
                      12.2
                    ]
                  },
                  {
                    "incomingLinks": [
                      13.1,
                      13.2
                    ]
                  }
                ]
              }
            }
        """

        and:
        def network = restoreSnapshot.from(snapshot)

        expect:
        assertThat(takeSnapshot.of(network))
                .on('$.inputLayer.inputs[0].outgoingLinks[*]').satisfies(containsInAnyOrder(1.1d, 1.2d))
                .on('$.inputLayer.inputs[1].outgoingLinks[*]').satisfies(containsInAnyOrder(2.1d, 2.2d))

                .on('$.neuronsLayers.layers[0].neurons[0].outgoingLinks[*]').satisfies(containsInAnyOrder(3.1d, 3.2d))
                .on('$.neuronsLayers.layers[0].neurons[0].incomingLinks[*]').satisfies(containsInAnyOrder(4.1d, 4.2d))
                .on('$.neuronsLayers.layers[0].neurons[1].outgoingLinks[*]').satisfies(containsInAnyOrder(5.1d))
                .on('$.neuronsLayers.layers[0].neurons[1].incomingLinks[*]').satisfies(containsInAnyOrder(5.1d))

                .on('$.neuronsLayers.layers[1].neurons[0].outgoingLinks[*]').satisfies(containsInAnyOrder(6.1d, 6.2d))
                .on('$.neuronsLayers.layers[1].neurons[0].incomingLinks[*]').satisfies(containsInAnyOrder(7.1d, 7.2d))
                .on('$.neuronsLayers.layers[1].neurons[1].outgoingLinks[*]').satisfies(containsInAnyOrder(8.1d, 8.2d))
                .on('$.neuronsLayers.layers[1].neurons[1].incomingLinks[*]').satisfies(containsInAnyOrder(9.1d, 9.2d))
                .on('$.neuronsLayers.layers[1].neurons[2].outgoingLinks[*]').satisfies(containsInAnyOrder(10.1d, 10.2d))
                .on('$.neuronsLayers.layers[1].neurons[2].incomingLinks[*]').satisfies(containsInAnyOrder(11.1d, 11.2d))

                .on('$.outputLayer.outputs[0].incomingLinks[*]').satisfies(containsInAnyOrder(12.1d, 12.2d))
                .on('$.outputLayer.outputs[1].incomingLinks[*]').satisfies(containsInAnyOrder(13.1d, 13.2d))
    }
}
