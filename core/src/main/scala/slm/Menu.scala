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
