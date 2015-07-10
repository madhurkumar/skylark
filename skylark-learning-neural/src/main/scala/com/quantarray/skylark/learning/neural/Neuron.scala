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
 * Neuron.
 *
 * @author Araik Grigoryan
 */
case class Neuron(index: Int, activation: NeuralActivation, layer: Nucleus) extends NeuralCell
{
  type Repr = Neuron

  type L = Nucleus

  val isBias: Boolean = index == 0 // FIXME: Better logic for determining bias

  val nonBias: Boolean = !isBias

  override def toString: String = s"Neuron(isBias=$isBias) $index in $layer"
}