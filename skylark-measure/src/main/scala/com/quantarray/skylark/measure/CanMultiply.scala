package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can multiply type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanMultiply implementation that multiply ${M1} and ${M2}, resulting in ${R}.")
trait CanMultiply[M1, M2, R]
{
  def times(multiplicand: M1, multiplier: M2): R

  def unit(multiplicand: M1, multiplier: M2): Double = 1.0
}