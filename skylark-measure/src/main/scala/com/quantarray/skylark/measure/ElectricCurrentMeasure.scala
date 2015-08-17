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

package com.quantarray.skylark.measure

/**
 * Electric current measure.
 *
 * @author Araik Grigoryan
 */
case class ElectricCurrentMeasure(name: String, system: SystemOfUnits, declMultBase: Option[(Double, Measure)]) extends Measure with MeasureCanBecomeAsset
{
  type D = ElectricCurrent.type

  type Repr = ElectricCurrentMeasure

  def dimension = ElectricCurrent

  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = ElectricCurrentMeasure(name, system, Some(mb))

  override def toString = name
}

object ElectricCurrentMeasure
{
  def apply(name: String, system: SystemOfUnits) = new ElectricCurrentMeasure(name, system, None)

  def apply(name: String, system: SystemOfUnits, dmb: (Double, Measure)): ElectricCurrentMeasure = new ElectricCurrentMeasure(name, system, Some(dmb))

  def apply(name: String, dmb: (Double, Measure)): ElectricCurrentMeasure = apply(name, dmb._2.system, dmb)
}