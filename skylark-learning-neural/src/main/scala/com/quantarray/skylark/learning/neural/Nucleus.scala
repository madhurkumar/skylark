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

/**
 * Nucleus: a compact collection of neurons forming a layer inside a neural net.
 *
 * @author Araik Grigoryan
 */
case class Nucleus(index: Int, activation: NeuralActivation, numberOfNeurons: Int) extends NeuralLayer
{
  type C = Neuron

  lazy val cells = (1 to numberOfNeurons).map(Neuron(_, activation, this))

  override def toString: String = s"layer $index"
}