import psycopg2
from psycopg2.extras import execute_values
from datetime import datetime

DB_HOST = 'ec2-13-233-164-214.ap-south-1.compute.amazonaws.com'
DB_PORT = '5432'
DB_NAME = 'darzee_stage'
DB_USER = 'postgres'
DB_PASSWORD = '0nDsUUY7pMyNCgMnvipI'

# Establish a connection to the database
conn = psycopg2.connect(host=DB_HOST, port=DB_PORT, dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD)

# Create a cursor
cursor = conn.cursor()

# Define the SELECT query for fetching data from the order_amount table
select_query = """
    SELECT id, total_amount, order_id
    FROM order_amount
"""

# Execute the SELECT query
cursor.execute(select_query)

# Fetch all rows from the result
order_amount_rows = cursor.fetchall()

# Define the INSERT query for inserting data into the price_breakup table
insert_query = """
    INSERT INTO price_breakup (
        component, value, quantity, order_item_id, is_deleted
    ) VALUES %s
"""

# Process the rows and prepare the data for bulk insertion
price_breakup_data = []
for row in order_amount_rows:
    # Fetch order_item_id for the corresponding order
    order_id = row[2]
    order_item_id_query = f"SELECT id FROM order_item WHERE order_id = {order_id}"
    cursor.execute(order_item_id_query)
    order_item_id = cursor.fetchone()[0]

    # Prepare data for price_breakup
    data = ('Stitching Cost', row[1], 1, order_item_id, False)
    price_breakup_data.append(data)

# Execute the INSERT query with bulk insertion
execute_values(cursor, insert_query, price_breakup_data)

# Commit the changes
conn.commit()

# Close the cursor and connection
cursor.close()
conn.close()