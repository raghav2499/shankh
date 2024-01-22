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

# Define the columns to be copied
columns_to_copy = [
    'trial_date', 'delivery_date', 'special_instructions', 'order_type',
    'outfit_type', 'is_deleted', 'inspiration', 'is_priority_order',
    'created_at', 'updated_at', 'status'
]

# Define the SELECT query for fetching data from the orders table
select_query = f"SELECT id, {', '.join(columns_to_copy)} FROM orders"

# Execute the SELECT query
cursor.execute(select_query)

# Fetch all rows from the result
rows = cursor.fetchall()

# Define the INSERT query for inserting data into the order_item table
insert_query = """
    INSERT INTO order_item (
        id, trial_date, delivery_date, special_instructions, order_type,
        outfit_type, is_deleted, inspiration, is_priority_order,
        created_at, updated_at, status, order_id, quantity
    ) VALUES %s
"""

# Process the rows and prepare the data for bulk insertion
data_to_insert = []
for row in rows:
    # Assuming each order corresponds to one item
    data = (row[0],) + row[1:] + (row[0], 1)
    data_to_insert.append(data)

# Execute the INSERT query with bulk insertion
execute_values(cursor, insert_query, data_to_insert)

# Commit the changes
conn.commit()

# Close the cursor and connection
cursor.close()
conn.close()
