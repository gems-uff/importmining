package br.uff.ic.logger

enum class Severity(val identifier: Int) {
    Debug(4),
    Info(3),
    Warn(2),
    Error(1),
    Fatal(0),
    Off(-1)
}