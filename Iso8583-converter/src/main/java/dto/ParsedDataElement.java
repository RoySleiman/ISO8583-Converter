package main.java.dto;
public class ParsedDataElement {
  private int number;
  private String name;
  private String value;

  public ParsedDataElement( int number, String name, String value) {
    this.number = number;
    this.name = name;
    this.value = value;
  }

  public int getNumber() {
    return number;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
