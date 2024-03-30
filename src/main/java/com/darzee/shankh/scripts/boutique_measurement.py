import psycopg2
from psycopg2.extras import execute_values
from datetime import datetime
import json
import enum

class OutfitType(enum.Enum):
    TYPE_0 = 0
    TYPE_1 = 1
    TYPE_2 = 2
    TYPE_3 = 3
    TYPE_4 = 4
    TYPE_5 = 5
    TYPE_6 = 6
    TYPE_7 = 7
    TYPE_8 = 8
    TYPE_9 = 9
    TYPE_10 = 10
    TYPE_11 = 11
    TYPE_12 = 12
    TYPE_13 = 13
    TYPE_14 = 14
    TYPE_15 = 15

class OutfitSide(enum.Enum):
    SIDE_0 = 0
    SIDE_1 = 1

# Define the data to be inserted
data = [
    (OutfitType.TYPE_0, OutfitSide.SIDE_0, ["kurta_length", "shoulder", "upper_chest", "bust", "waist", "seat", "arm_hole", "sleeve_length", "sleeve_circumference", "front_neck_depth", "back_neck_depth"]),
    (OutfitType.TYPE_0, OutfitSide.SIDE_1, ["pyjama_length", "pyjama_hip", "knee", "ankle"]),
    (OutfitType.TYPE_1, OutfitSide.SIDE_0, ["length", "shoulder", "upper_chest", "bust", "waist", "seat", "arm_hole", "sleeve_length", "bicep", "elbow_round", "sleeve_circumference", "front_neck_depth", "back_neck_depth", "cross_front", "cross_back", "dart_point"]),
    (OutfitType.TYPE_2, OutfitSide.SIDE_0, ["blouse_length", "bust", "upper_chest", "below_bust", "shoulder", "arm_hole", "sleeve_length", "sleeve_circumference", "bicep", "elbow_round", "apex_to_apex_length", "shoulder_to_apex_length", "front_neck_depth", "back_neck_depth", "cross_front", "cross_back", "dart_point"]),
    (OutfitType.TYPE_3, OutfitSide.SIDE_0, ["shirt_length", "neck", "shoulder", "chest", "waist", "seat", "sleeve_length", "sleeve_circumference"]),
    (OutfitType.TYPE_3, OutfitSide.SIDE_1, ["bottom_waist", "bottom_seat", "calf", "bottom", "pant_length", "fly"]),
    (OutfitType.TYPE_4, OutfitSide.SIDE_1, ["bottom_waist", "bottom_seat", "thigh", "calf", "bottom", "pant_length", "fly", "in_seam", "crotch"]),
    (OutfitType.TYPE_5, OutfitSide.SIDE_0, ["gown_length", "shoulder", "upper_chest", "bust", "waist", "seat", "arm_hole", "sleeve_length", "sleeve_circumference", "front_neck_depth", "back_neck_depth"]),
    (OutfitType.TYPE_6, OutfitSide.SIDE_0, ["kameez_length", "upper_chest", "bust", "below_bust", "waist", "hip_circum", "shoulder", "arm_hole", "bicep", "sleeve_length", "sleeve_circumference", "cross_front", "cross_back", "front_neck_depth", "back_neck_depth"]),
    (OutfitType.TYPE_6, OutfitSide.SIDE_1, ["salwar_length", "bottom_waist", "thigh", "knee", "calf", "mohri", "crotch"]),
    (OutfitType.TYPE_7, OutfitSide.SIDE_0, ["shirt_length", "neck", "shoulder", "chest", "waist", "seat", "sleeve_length", "bicep", "elbow_round", "sleeve_circumference", "arm_hole"]),
    (OutfitType.TYPE_8, OutfitSide.SIDE_1, ["waist", "length"]),
    (OutfitType.TYPE_9, OutfitSide.SIDE_0, ["length", "neck", "chest", "waist"]),
    (OutfitType.TYPE_10, OutfitSide.SIDE_0, ["above_head", "pardi_shoulder", "around_shoulder", "pardi_length", "pardi_gher", "kas"]),
    (OutfitType.TYPE_10, OutfitSide.SIDE_1, ["lenga_shoulder", "bust", "waist", "hips", "lenga_length", "lenga_gher"]),
    (OutfitType.TYPE_11, OutfitSide.SIDE_0, ["length", "chest", "waist"]),
    (OutfitType.TYPE_12, OutfitSide.SIDE_0, ["waist_circum", "hip_circum", "waist_to_knee", "length"]),
    (OutfitType.TYPE_13, OutfitSide.SIDE_0, ["length", "upper_chest", "bust", "below_bust", "waist", "hip_circum", "shoulder", "arm_hole", "bicep", "sleeve_length", "sleeve_circumference", "cross_front", "cross_back", "front_neck_depth", "back_neck_depth"]),
    (OutfitType.TYPE_13, OutfitSide.SIDE_1, ["salwar_length", "bottom_waist", "thigh", "knee", "calf", "mohri", "crotch", "nikker_length", "sharara_circum"]),
    (OutfitType.TYPE_14, OutfitSide.SIDE_0, ["shirt_length", "neck", "shoulder", "chest", "waist", "hip_circum", "sleeve_length", "bicep", "elbow_round", "sleeve_circum", "arm_hole"]),
    (OutfitType.TYPE_14, OutfitSide.SIDE_1, ["bottom_waist", "bottom_hip_circum", "thigh", "calf", "bottom", "bottom_length", "fly", "inseam", "crotch"]),
    (OutfitType.TYPE_15, OutfitSide.SIDE_0, ["shirt_length", "neck", "shoulder", "chest", "waist", "hip_circum", "sleeve_length", "bicep", "elbow_round", "sleeve_circumference", "arm_hole"]),
    (OutfitType.TYPE_15, OutfitSide.SIDE_1, ["bottom_waist", "bottom_hip_circum", "thigh", "calf", "bottom", "bottom_length", "fly", "inseam", "crotch"]),
]

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
    INSERT INTO boutique_measurement (id, boutique_id, outfit_type, outfit_side, param, created_at, updated_at)
    VALUES (%s, %s, %s, %s, %s, %s, %s)
"""

# Populate data into the boutique_measurement table
id_counter = 1  # Start ID counter
for outfit_type, outfit_side, param in data:
    param_json = json.dumps(param)
    created_at = updated_at = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    values = (id_counter, 0, outfit_type.value, outfit_side.value, param_json, created_at, updated_at)
    cursor.execute(sql, values)
    id_counter += 1  # Increment ID counter

# Commit the transaction
conn.commit()

# Commit the transaction
conn.commit()

# Close the cursor and connection
cursor.close()
conn.close()