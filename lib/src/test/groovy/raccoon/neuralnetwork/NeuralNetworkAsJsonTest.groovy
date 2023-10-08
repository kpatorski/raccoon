package raccoon.neuralnetwork

import spock.lang.Specification

import static AssertJson.assertThat
import static org.hamcrest.Matchers.*

class NeuralNetworkAsJsonTest extends Specification {
    /**
     * <img src="./network-hidden-layer.png" width=483/>
     */
    def "neural network is converted from and converted to JSON"() {
        given:
        def json = """
            {
              "inputLayer": {
                "inputs": [
                  {
                    "outgoingLinks": [
                      {
                        "id": "aaa1c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.1
                      },
                      {
                        "id": "aaa2c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.2
                      },
                      {
                        "id": "aaa3c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.3
                      }
                    ]
                  },
                  {
                    "outgoingLinks": [
                      {
                        "id": "bbb1c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.4
                      },
                      {
                        "id": "bbb2c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.5
                      },
                      {
                        "id": "bbb3c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.6
                      }
                    ]
                  }
                ]
              },
              "neuronsLayers": {
                "layers": [
                  {
                    "neurons": [
                      {
                        "bias": 0.10,
                        "activationFunction": "LINEAR",
                        "outgoingLinks": [
                          {
                            "id": "ccc1c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.1
                          },
                          {
                            "id": "ccc2c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.2
                          }
                        ],
                        "incomingLinks": [
                          {
                            "id": "aaa1c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.1
                          },
                          {
                            "id": "bbb1c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.4
                          }
                        ]
                      },
                      {
                        "bias": 0.11,
                        "activationFunction": "LINEAR",
                        "outgoingLinks": [
                          {
                            "id": "ddd1c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.1
                          },
                          {
                            "id": "ddd2c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.2
                          }
                        ],
                        "incomingLinks": [
                          {
                            "id": "aaa2c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.2
                          },
                          {
                            "id": "bbb2c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.5
                          }
                        ]
                      },
                      {
                        "bias": 0.12,
                        "activationFunction": "LINEAR",
                        "outgoingLinks": [
                          {
                            "id": "eee1c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.3
                          },
                          {
                            "id": "eee2c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.4
                          }
                        ],
                        "incomingLinks": [
                          {
                            "id": "aaa3c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.3
                          },
                          {
                            "id": "bbb3c2bf-a7ef-4203-af65-1c05428935d9",
                            "weight": 0.6
                          }
                        ]
                      }
                    ]
                  }
                ]
              },
              "outputLayer": {
                "outputs": [
                  {
                    "bias": 0.20,
                    "activationFunction": "LINEAR",
                    "incomingLinks": [
                      {
                        "id": "ccc1c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.1
                      },
                      {
                        "id": "ddd1c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.1
                      },
                      {
                        "id": "eee1c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.3
                      }
                    ]
                  },
                  {
                    "bias": 0.21,
                    "activationFunction": "LINEAR",
                    "incomingLinks": [
                      {
                        "id": "ccc2c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.2
                      },
                      {
                        "id": "ddd2c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.2
                      },
                      {
                        "id": "eee2c2bf-a7ef-4203-af65-1c05428935d9",
                        "weight": 0.4
                      }
                    ]
                  }
                ]
              }
            }
        """

        and:
        def network = NetworkFacade.deserializeFromJson(json)

        expect:
        assertThat(NetworkFacade.serializeToJson(network))
                .on('$.inputLayer.inputs[0].outgoingLinks[*].weight').satisfies(containsInAnyOrder(0.1d, 0.2d, 0.3d))
                .on('$.inputLayer.inputs[1].outgoingLinks[*].weight').satisfies(containsInAnyOrder(0.4d, 0.5d, 0.6d))

                .on('$.neuronsLayers.layers[0].neurons[0].bias').satisfies(equalTo(0.1d))
                .on('$.neuronsLayers.layers[0].neurons[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[0].neurons[0].outgoingLinks[*].weight').satisfies(containsInAnyOrder(0.1d, 0.2d))
                .on('$.neuronsLayers.layers[0].neurons[0].incomingLinks[*].weight').satisfies(containsInAnyOrder(0.1d, 0.4d))

                .on('$.neuronsLayers.layers[0].neurons[1].bias').satisfies(equalTo(0.11d))
                .on('$.neuronsLayers.layers[0].neurons[1].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[0].neurons[1].outgoingLinks[*].weight').satisfies(containsInAnyOrder(0.1d, 0.2d))
                .on('$.neuronsLayers.layers[0].neurons[1].incomingLinks[*].weight').satisfies(containsInAnyOrder(0.2d, 0.5d))

                .on('$.neuronsLayers.layers[0].neurons[2].bias').satisfies(equalTo(0.12d))
                .on('$.neuronsLayers.layers[0].neurons[2].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[0].neurons[2].outgoingLinks[*].weight').satisfies(containsInAnyOrder(0.3d, 0.4d))
                .on('$.neuronsLayers.layers[0].neurons[2].incomingLinks[*].weight').satisfies(containsInAnyOrder(0.3d, 0.6d))

                .on('$.outputLayer.outputs[0].bias').satisfies(equalTo(0.2d))
                .on('$.outputLayer.outputs[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.outputLayer.outputs[0].incomingLinks[*].weight').satisfies(containsInAnyOrder(0.1d, 0.1d, 0.3d))

                .on('$.outputLayer.outputs[1].bias').satisfies(equalTo(0.21d))
                .on('$.outputLayer.outputs[1].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.outputLayer.outputs[1].incomingLinks[*].weight').satisfies(containsInAnyOrder(0.2d, 0.2d, 0.4d))
    }
}
