# ISO8583 Message Processor

A Java program to process ISO8583 v87 transaction messages, converting raw data into an easy-to-read log and displaying each data element clearly.


# Quick Run

Right click on Iso8583Converter (under src) and choose Run Iso8583Converter.main()


# Project Structure

1. Iso8583Converter.java: Main class to execute ISO8583 processing.

2. Iso8583MessageService.java: Service class handling ISO8583 message processing.

3. Iso8583Util.java: Utility class containing helper methods and data structures.

4. dto/: Directory containing Data Transfer Object classes.

5. util/: Directory containing additional utility classes.


# Functionality

1. parseISO8583RequestMessage: This function is responsible for parsing the raw ISO 8583 request message. It extracts the total length, message type, bitmap, and data elements and diplay them clearly.

2. parseISO8583Response: This function parses the raw ISO 8583 response message. It extracts the total length, message type, bitmap, and data elements.

3. createISO8583Response(parsedDataRequest): Generates a sample ISO 8583 response based on the provided ParsedDataRequest. Modifies the bitmap and includes specific logic for certain data elements.


#Notes

1. The code dynamically retrieves the necessary data elements based on the binary set in the parsed bitmap from the raw content of the request.

2. In the loopback response, we assume the success of the cash withdrawal by setting a response code as approved.

3. During the response parsing, we adjusted the bitmap by enabling DE38 and DE39, as they are mandatory in the response.






