import psycopg2
from psycopg2.extras import execute_values
from datetime import datetime

# Define the mappings between name and display name
param_mapping = {
    "kurta_length": "Kurta Length",
    "shoulder": "Shoulder",
    "upper_chest": "Upper Chest",
    "bust": "Bust",
    "waist": "Waist",
    "seat": "Seat",
    "arm_hole": "Arm Hole",
    "sleeve_length": "Sleeve Length",
    "sleeve_circumference": "Sleeve Circum",
    "front_neck_depth": "Front Neck Depth",
    "back_neck_depth": "Back Neck Depth",
    "pyjama_length": "Pajama Length",
    "pyjama_hip": "Pajama Hip",
    "knee": "Knee",
    "ankle": "Ankle",
    "length": "Length",
    "bicep": "Bicep",
    "elbow_round": "Elbow Round",
    "cross_front": "Cross Front",
    "cross_back": "Cross Back",
    "dart_point": "Dart Point",
    "blouse_length": "Blouse Length",
    "below_bust": "Below Bust",
    "apex_to_apex_length": "Apex to Apex",
    "shoulder_to_apex_length": "Shoulder to Apex",
    "above_head": "Above Head",
    "pardi_shoulder": "Shoulder",
    "around_shoulder": "Around Shoulder",
    "pardi_length": "Pardi Length",
    "pardi_gher": "Pardi Gher",
    "kas": "Kas",
    "shirt_length": "Shirt Length",
    "neck": "Neck",
    "chest": "Chest",
    "bottom_waist": "Bottom Waist",
    "bottom_seat": "Bottom Seat",
    "calf": "Calf",
    "bottom": "Bottom",
    "pant_length": "Pant Length",
    "fly": "Fly",
    "in_seam": "In-Seam",
    "waist_circum": "Waist Circum",
    "hip_circum": "Hip Circum",
    "waist_to_knee": "Waist To Knee",
    "bottom_length": "Bottom Length",
    "waist_lower": "Waist Lower",
    "thigh_circum": "Thigh Circum",
    "thigh": "Thigh",
    "kameez_length": "Length"
}

# Database credentials
DB_HOST = 'ec2-13-233-164-214.ap-south-1.compute.amazonaws.com'
DB_PORT = '5432'
DB_NAME = 'darzee_stage'
DB_USER = 'postgres'
DB_PASSWORD = '0nDsUUY7pMyNCgMnvipI'

# Establish a connection to the database
conn = psycopg2.connect(host=DB_HOST, port=DB_PORT, dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD)

# Create a cursor
cursor = conn.cursor()

# Define the SQL statement to insert data
sql = """
    INSERT INTO measurement_param (id, name, display_name, file_name, created_at, updated_at)
    VALUES (%s, %s, %s, %s, %s, %s)
"""

# Populate data into the measurement_param table
id_counter = 1  # Start ID counter
for name, display_name in param_mapping.items():
    file_name = name  # Assuming file_name is the same as name
    created_at = updated_at = datetime.now()
    values = (id_counter, name, display_name, file_name, created_at, updated_at)
    cursor.execute(sql, values)
    id_counter += 1  # Increment ID counter

# Commit the transaction
conn.commit()

# Close the cursor and connection
cursor.close()
conn.close()
