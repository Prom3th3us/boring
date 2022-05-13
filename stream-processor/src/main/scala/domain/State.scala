package domain

trait State[Event, S <: State[Event, S]] {
  def +(event: Event): S
}
