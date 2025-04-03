/*
 * Copyright (c) 2025 Paul (Thor) Thordarson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package slm

import terminus.Terminal

case class Menu(head: String, choices: Array[Menu.Choice])

object Menu:
  case class Choice(value: String | Menu, display: String)

extension (menu: Menu)
  def ttyClear: Terminal ?=> Unit = {
    val lines = menu.choices.length + menu.head.count(_ == '\n') + 1
    Terminal.cursor.move(1, -1 * lines)
    Terminal.erase.down()
    Terminal.cursor.column(1)
  }

  def ttyRender(choiceIndex: Int): Terminal ?=> Unit = {
    Terminal.write(s"${menu.head}\r\n")

    menu.choices.indices
      .foreach(idx => menu.choices(idx).ttyRender(choiceIndex == idx))
    Terminal.flush()
  }

extension (choice: Menu.Choice)
  def ttyRender(selected: Boolean): Terminal ?=> Unit = {
    if selected then
      Terminal.format.bold(Terminal.write(s"> ${choice.display}\r\n"))
    else Terminal.write(s"  ${choice.display}\r\n")
  }
