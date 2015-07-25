/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2015 Quantarray, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quantarray.skylark.learning.neural

import org.scalatest.{FlatSpec, Matchers}

/**
 * Feed-forward net spec.
 *
 * @author Araik Grigoryan
 */
class FeedForwardNetSpec extends FlatSpec with Matchers
{
  "FeedForwardNet" should "trainable" in
    {
      val net = FeedForwardNet(SigmoidActivation, 4, 3, 2)

      net.connections.size should be((4 + 1) * 3 + (3 + 1) * 2) // +1s are to account for the Biases

      val weights = net.weights
      weights.size should be(2)
      weights(0).size should be(4)
      weights(1).size should be(3)

      val biases = net.biases
      biases.size should be(2)
      biases(1).size should be(3)
      biases(2).size should be(2)

      val dataSet = new SupervisedDataSet
      {
        override def samples: Seq[SupervisedDataSample] =
          Seq(
            new SupervisedDataSample
            {
              override def input: Seq[Double] = Seq(1, 1, 0, 0)

              override def target: Seq[Double] = Seq(1, 0)
            }
          )
      }

      val trainer = BackPropagationTrainer(0.5, 0.5)

      val trainedNet = trainer.train(net, 6, dataSet)

      trainedNet.connections.size should equal(net.connections.size)
    }
}
