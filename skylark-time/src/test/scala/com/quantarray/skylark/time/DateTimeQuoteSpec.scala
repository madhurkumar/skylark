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

package com.quantarray.skylark.time

import com.quantarray.skylark.time.DateTimeQuote._
import org.scalatest.{FlatSpec, Matchers}

class DateTimeQuoteSpec extends FlatSpec with Matchers
{
  "Valid ISO 8601 date" should "compile and equal itself" in
    {
      val wayBackWhen = d"2015-07-01"

      wayBackWhen should equal(d"2015-07-01")
    }

  "Valid ISO 8601 date/time" should "compile and equal itself" in
    {
      val wayBackWhen = dt"2015-07-01T03:33:59Z"

      wayBackWhen should equal(dt"2015-07-01T03:33:59Z")
    }
}