from odf.opendocument import OpenDocumentSpreadsheet
from odf.table import Table, TableRow, TableCell

def read_ods(file_path):
    # Open the ODS document
    doc = OpenDocumentSpreadsheet(file_path)
    
    # Get the first sheet in the ODS file
    sheets = doc.getElementsByType(Table)
    if not sheets:
        raise ValueError("No sheets found in the ODS file.")
    sheet = sheets[0]  # Assuming you want to read the first sheet
    
    # List to store the rows of data
    data = []
    
    # Iterate through rows in the sheet
    for row in sheet.getElementsByType(TableRow):
        row_data = []
        
        # Iterate through cells in the row
        for cell in row.getElementsByType(TableCell):
            # Extract text content from the cell
            text = cell.firstChild.data if cell.firstChild else ""
            row_data.append(text)
        
        # Only add rows that contain data (avoid headers)
        if len(row_data) > 0:
            data.append(row_data)
    
    return data

# Path to your ODS file
file_path = 'dns_records.ods'

# Read and print the data
ods_data = read_ods(file_path)

# Display the data as rows (this will be a list of lists)
for row in ods_data:
    print(row)
