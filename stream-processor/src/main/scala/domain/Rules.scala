package domain

trait Rules[Event, State, Command, Error] {
  def validator(context: State): Command => Either[Error, Seq[Event]]
}
