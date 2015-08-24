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

package com.quantarray.skylark.timeseries

/**
 * Time series.
 *
 * @author Araik Grigoryan
 */
trait TimeSeries[V]
{
  /**
   * Entity key. An arbitrary representation of the structure of time-series data by which a value could be looked up.
   *
   * Examples:
   *
   * "Earth:North America:USA:Los Angeles:Temperature"
   * "CL_2021Z.price" ... Price of December 2021 WTI futures contract
   */
  def entityKey: String

  def set: TimeSeriesSet

  def points: Seq[TimeSeriesPoint[V]]

  def isEmpty: Boolean = points.isEmpty

  def nonEmpty: Boolean = points.nonEmpty

  def isSingular: Boolean = points.size == 1

  def nonSingular: Boolean = !isSingular

  override def toString = s"($entityKey, $set) -> $points"
}

object TimeSeries
{


  def apply[V](entityKey: String, set: TimeSeriesSet, points: Seq[TimeSeriesPoint[V]]): TimeSeries[V] =
  {
    val eksps = (entityKey, set, points)

    new TimeSeries[V]
    {
      val entityKey: String = eksps._1

      val set: TimeSeriesSet = eksps._2

      val points: Seq[TimeSeriesPoint[V]] = eksps._3
    }
  }

  def empty[V](entityKey: String, set: TimeSeriesSet): TimeSeries[V] = apply(entityKey, set, Seq.empty)

}

