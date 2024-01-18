import csv
import psycopg2
from psycopg2.extras import Json

DB_HOST = 'ec2-13-233-164-214.ap-south-1.compute.amazonaws.com'
DB_PORT = '5432'
DB_NAME = 'darzee_stage'
DB_USER = 'postgres'
DB_PASSWORD = '0nDsUUY7pMyNCgMnvipI'

CSV_FILE_PATH = 'C://Users/HP/Desktop/stitch_options_upload.csv'

conn = psycopg2.connect(host=DB_HOST, port=DB_PORT, dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD)
cursor = conn.cursor()

with open(CSV_FILE_PATH, 'r') as csv_file:
    csv_reader = csv.DictReader(csv_file)

    # Define the INSERT query for populating the stitch_options table
    insert_query = """
        INSERT INTO stitch_options (id, is_valid, outfit_type, key, value, type, outfit_side)
        VALUES (nextval('stitch_options_seq'), TRUE, %s, %s, %s, %s, %s)
    """

    # Process the rows and insert data into the stitch_options table
    for row in csv_reader:
        outfit_type = row['outfit_type'].strip()
        key = row['key'].strip()
        value = [item.strip() for item in row['value'].split(",")]
        stype = row['type'].strip()
        outfit_side = int(row['outfit_side'].strip())  # Assuming outfit_side is an integer

        # Execute the INSERT query
        cursor.execute(insert_query, (outfit_type, key, Json(value), stype, outfit_side))

# Commit the changes
conn.commit()

# Close the cursor and connection
cursor.close()
conn.close()
