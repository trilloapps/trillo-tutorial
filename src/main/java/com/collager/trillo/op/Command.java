package com.collager.trillo.op;

abstract public class Command {
  String type;
  public Command(String type) {
    this.type = type;
  }
}
