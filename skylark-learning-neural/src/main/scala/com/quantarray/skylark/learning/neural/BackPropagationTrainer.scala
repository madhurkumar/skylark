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

import breeze.linalg.DenseMatrix

/**
 * Back propagation trainer.
 *
 * @author Araik Grigoryan
 */
case class BackPropagationTrainer(learningRate: Double, momentum: Double) extends Trainer
{
  type Matrix = DenseMatrix[Double]

  type Weights = NetPropMap[Double]

  type Biases = NetPropMap[Double]

  override def train[N <: Net](net: N, numberOfEpochs: Int, dataSet: SupervisedDataSet)(implicit cbf: NetCanBuildFrom[N, net.C, net.T, N]): N =
  {
    val weights = net.weights

    val biases = net.biases

    val newWeightsBiases = (0 until numberOfEpochs).foldLeft((weights, biases))((wb, epochIndex) =>
    {
      train(net.activation, wb, dataSet.samples)
    })

    //    dataSet.samples.foldLeft((weights, biases))((wsbs, sample) =>
    //    {
    //      //train(net.activation, wsbs, sample)
    //      wsbs
    //    })

    net // FIXME: Build new net
  }

  /**
   * Constructs matrix representation of biases and weights.
   */
  private def matrices(wsbs: (Weights, Biases)): (Seq[Matrix], Seq[Matrix]) =
  {
    val weights = wsbs._1
    val biases = wsbs._2

    val ws = weights.keys.map(layerIndex =>
    {
      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      DenseMatrix(weights(layerIndex).values.toSeq: _*)
    })

    val bs = weights.keys.map(layerIndex =>
    {
      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      DenseMatrix(biases(layerIndex + 1).values.toSeq: _*)
    })

    (ws.toSeq, bs.toSeq)
  }

  private def zeros(wsbs: (Weights, Biases)): (Seq[Matrix], Seq[Matrix]) =
  {
    val weights = wsbs._1
    val biases = wsbs._2

    val ws = weights.keys.map(layerIndex =>
    {
      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      DenseMatrix(weights(layerIndex).values.map(_.map(_ => 0.0)).toSeq: _*)
    })

    val bs = weights.keys.map(layerIndex =>
    {
      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      DenseMatrix(biases(layerIndex + 1).values.map(_.map(_ => 0.0)).toSeq: _*)
    })

    (ws.toSeq, bs.toSeq)
  }

  private def train(activation: Activation, wsbs: (Weights, Biases), samples: Seq[SupervisedDataSample]): (Weights, Biases) =
  {
    val nablaBsNablaWs = samples.foldLeft[Option[(Seq[Matrix], Seq[Matrix])]](None)((nablaBsNablaWs, sample) =>
    {
      val (deltaNablaBs, deltaNablaWs) = train(activation, wsbs, sample)
      // (3x1, 2x1)
      // (3x4, 2x3)

      Some(nablaBsNablaWs.fold((deltaNablaBs, deltaNablaWs))(nBsnWs => (nBsnWs._1.zip(deltaNablaBs).map(m => m._1 + m._2), nBsnWs._2.zip(deltaNablaWs).map(m => m._1 + m._2))))
    })

    //    val newWB = samples.foldLeft(wsbs)((wb, sample) =>
    //    {
    //      val (deltaNablaBs, deltaNablaWs) = train(activation, wb, sample)
    //      // (3x1, 2x1)
    //      // (3x4, 2x3)
    //
    //
    //      wb
    //    })

    //nablaBsNablaWs.fold(wsbs)(nBsnWs => (nBsnWs._1.zip()))

    matrices(wsbs)

    wsbs
  }

  private def train(activation: Activation, wsbs: (Weights, Biases), sample: SupervisedDataSample): (Seq[Matrix], Seq[Matrix]) =
  {
    val weights = wsbs._1
    val biases = wsbs._2

    // Forward-propagate the input
    val aszs = weights.keys.foldLeft((List(DenseMatrix(sample.input: _*)), List.empty[Matrix]))((aszs, layerIndex) =>
    {
      val as = aszs._1
      val zs = aszs._2

      // m by 1 vector, where m is the number of inputs to the layer identified by the layerIndex
      val a = as.head

      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      val w = DenseMatrix(weights(layerIndex).values.toSeq: _*)

      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      val b = DenseMatrix(biases(layerIndex + 1).values.toSeq: _*)

      val z = (w.t * a: Matrix) + b

      val newA = z.map(activation)

      (newA :: as, z :: zs)
    })

    // Backward-propagate errors
    val as = aszs._1
    val zs = aszs._2

    // Activation output
    val a = as.head
    // Target output
    val y = DenseMatrix(sample.target: _*)
    val z = zs.head
    val delta = QuadraticObjective.d(z, a, y) :* z.map(activation.d)

    val nablaB = delta
    val nablaW: Matrix = delta * as.tail.head.t

    val (nablaBs, nablaWs, _) = (as.tail.tail, zs.tail, 2 until as.size).zipped.foldLeft((List(nablaB), List(nablaW), delta))((nablaBsNablaWsDelta, azLayerIndexes) =>
    {
      val nablaBs = nablaBsNablaWsDelta._1
      val nablaWs = nablaBsNablaWsDelta._2
      val delta = nablaBsNablaWsDelta._3

      val a = azLayerIndexes._1
      val z = azLayerIndexes._2
      val layerIndex = azLayerIndexes._3

      val w = DenseMatrix(weights(layerIndex - 1).values.toSeq: _*)

      val wd: Matrix = w * delta

      val newDelta = wd :* z.map(activation.d)

      val nablaB = newDelta
      val dx: Matrix = newDelta * a.t
      val nablaW = dx

      (nablaB :: nablaBs, nablaW :: nablaWs, newDelta)
    })

    (nablaBs, nablaWs)
  }
}
