/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2016 Quantarray, LLC
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

import scala.annotation.implicitNotFound

/**
  * Can add type class.
  *
  * @author Araik Grigoryan
  */
@implicitNotFound("Cannot find CanAdd implementation that adds ${A1} and ${A2}.")
trait CanAdd[A1, A2]
{
  type R

  def plus(addend1: A1, addend2: A2): R
}

