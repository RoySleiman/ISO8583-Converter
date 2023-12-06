package main.java.dto;

public class DataElement {

  private int number;
  private String name;
  private int length;

  public DataElement(int number, String name, int length) {
    this.number = number;
    this.name = name;
    this.length = length;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
